package com.LP2.EventScheduler.service.event;

import com.LP2.EventScheduler.dto.event.CreateEventDTO;
import com.LP2.EventScheduler.dto.event.JoinEventDTO;
import com.LP2.EventScheduler.dto.event.UpdateEventDTO;
import com.LP2.EventScheduler.email.EmailService;
import com.LP2.EventScheduler.email.mails.EventCanceledEmail;
import com.LP2.EventScheduler.exception.*;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.Participation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.EventStatus;
import com.LP2.EventScheduler.model.enums.Visibility;
import com.LP2.EventScheduler.repository.*;
import com.LP2.EventScheduler.response.EntityWithMessageResponse;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.event.EventDetails;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.response.event.EventMapper;
import com.LP2.EventScheduler.scheduler.SchedulerService;
import com.LP2.EventScheduler.scheduler.job.SendNotificationsAboutTimeLeftForEvent;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    private final Scheduler scheduler;
    private final SchedulerService schedulerService;
    private final EmailService emailService;

    @Override
    public ListResponse<EventItem> searchPublicEvents(
            String searchQuery,
            EventSortingOptions sortBy,
            String categoryName
    ) {
        Category category = null;

        if (categoryName != null)
            category = this.categoryRepository
                    .findByName(categoryName)
                    .orElseThrow(CategoryNotFoundException::new);

        List<Event> events = this.eventRepository.searchEvents(searchQuery, sortBy, Visibility.PUBLIC, category);

        List<EventItem> mappedEvents = EventMapper.INSTANCE.toList(events);

        return new ListResponse<>(mappedEvents);
    }

    @Override
    public ListResponse<EventItem> searchForEventsYouParticipateIn(
            EventSortingOptions sortBy,
            String categoryName,
            User authUser
    ) {
        Category category = null;

        if (categoryName != null)
            category = this.categoryRepository
                    .findByName(categoryName)
                    .orElseThrow(CategoryNotFoundException::new);

        List<Event> events = this.eventRepository.searchEventsCreatedAndParticipating(sortBy, category, authUser);

        List<EventItem> mappedEvents = EventMapper.INSTANCE.toList(events);

        return new ListResponse<>(mappedEvents);
    }

    @Override
    public EventDetails getEventDetails(UUID eventId, User authUser) {
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        boolean isEventOwner = event.getCoordinator().getId().equals(authUser.getId());

        if (event.getVisibility().equals(Visibility.ONLY_CONNECTIONS) && !isEventOwner) {
            boolean usersConnectionExist = this.connectionRepository.existsConnectionBetweenUsers(event.getCoordinator(), authUser);
            if (!usersConnectionExist)
                throw new ConnectionNotFoundException("The event is only for connections");
        }

        boolean isUserParticipatingInEvent;

        if (isEventOwner)
            isUserParticipatingInEvent = false;
        else
            isUserParticipatingInEvent = this.participationRepository.existsByUserAndEvent(authUser, event);

        return EventMapper.INSTANCE.toDetail(event, isUserParticipatingInEvent);
    }

    @Override
    public EntityWithMessageResponse<EventItem> scheduleEvent(CreateEventDTO eventData, User user) {
        Category category = this.categoryRepository
                .findById(eventData.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        Event newEvent = Event.builder()
                .name(eventData.getName())
                .description(eventData.getDescription())
                .local(eventData.getLocal())
                .realizationDate(eventData.getRealizationDate())
                .category(category)
                .coordinator(user)
                .visibility(eventData.getVisibility())
                .finishDate(eventData.getFinishDate())
                .build();

        Event savedEvent = this.eventRepository.save(newEvent);

        int hourOfRealizationDateOfEvent = eventData.getRealizationDate().getHour();
        int minuteOfRealizationDateOfEvent = eventData.getRealizationDate().getMinute();

        JobDataMap jobData = new JobDataMap();

        jobData.put("eventId", savedEvent.getId().toString());
        jobData.put("eventRealizationDate", savedEvent.getRealizationDate().toString());
        jobData.put("eventFinishDate", savedEvent.getFinishDate().toString());

        try {
            JobDetail scheduleNotificationsJobDetail = this.schedulerService.buildJobDetail(
                    new SendNotificationsAboutTimeLeftForEvent(),
                    jobData,
                    savedEvent.getId().toString(),
                    "job detail to schedule notifications",
                    "schedule-notifications"
            );
            Trigger scheduleNotificationsTrigger = this.schedulerService.buildTriggerWithCronSchedule(
                    scheduleNotificationsJobDetail,
                    "schedule-notifications",
                    "trigger to schedule notifications",
                    CronScheduleBuilder.cronSchedule(String.format("0 %s %s * * ?",
                            minuteOfRealizationDateOfEvent,
                            hourOfRealizationDateOfEvent))
            );

            this.scheduler.scheduleJob(scheduleNotificationsJobDetail, scheduleNotificationsTrigger);
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
            throw new ScheduleCreationException("Failed to create notification scheduler");
        }

        EventItem mappedEvent = EventMapper.INSTANCE.toResponse(savedEvent);

        return new EntityWithMessageResponse<>(
                "The event has been created successfully",
                mappedEvent
        );
    }

    @Override
    public MessageResponse joinEvent(UUID eventId, JoinEventDTO joinData, User user) {
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (event.getCoordinator().getId().equals(user.getId()))
            throw new ResourceBelongsTheUserException("Cannot join an event you created");

        if (event.getVisibility().equals(Visibility.ONLY_CONNECTIONS)) {
            boolean usersConnectionExist = this.connectionRepository.existsConnectionBetweenUsers(event.getCoordinator(), user);
            if (!usersConnectionExist)
                throw new ConnectionNotFoundException("The event is only for connections");
        }

        if (!event.getStatus().equals(EventStatus.PENDING))
            throw new UnexpectedResourceValueException("The event must be in a PENDING state");

        Optional<Participation> userParticipationToEvent = this.participationRepository.findByUserAndEvent(user, event);

        if (userParticipationToEvent.isPresent()) {
            this.participationRepository.delete(userParticipationToEvent.get());
            return new MessageResponse("Participation removed");
        }

        Participation userParticipation = Participation.builder()
                .event(event)
                .user(user)
                .token(joinData.getToken())
                .build();

        this.participationRepository.save(userParticipation);

        return new MessageResponse("Participation added");
    }

    @Override
    public MessageResponse removeEvent(UUID eventId, User user){
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!event.getCoordinator().getId().equals(user.getId()))
            throw new IsNotOwnerException("You are not the event coordinator");

        if (!event.getStatus().equals(EventStatus.CANCELLED))
            throw new UnexpectedResourceValueException("Event must have status CANCELLED to be deleted");

        this.eventRepository.delete(event);

        return new MessageResponse("Event remove successfully");
    }

    @Transactional
    @Override
    public MessageResponse cancelEvent(UUID eventId, User authUser) {
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!event.getCoordinator().getId().equals(authUser.getId()))
            throw new IsNotOwnerException("You are not the coordinator of this event");

        if (!event.getStatus().equals(EventStatus.PENDING))
            throw new UnexpectedResourceValueException("The event must be in a pending state");

        event.setStatus(EventStatus.CANCELLED);
        this.eventRepository.save(event);

        JobKey eventJob = JobKey.jobKey(
                event.getId().toString(),
                "schedule-notifications"
        );

        try {
            boolean isEventSchedulerRemoved = this.scheduler.deleteJob(eventJob);

            if (!isEventSchedulerRemoved)
                throw new FailedEventSchedulerRemovedException();
        } catch (SchedulerException e) {
            throw new FailedEventSchedulerRemovedException();
        }

        List<Participation> participations = event.getParticipants();

        for (Participation participation : participations) {
            Map<String, String> emailData = new HashMap<>();
            emailData.put("coordinatorUsername", participation.getUser().getUserName());
            emailData.put("eventName", event.getName());

            CompletableFuture.runAsync(() -> this.emailService.sendEmail(
                    participation.getUser().getEmail(),
                    new EventCanceledEmail(),
                    emailData
            ));
        }

        return new MessageResponse("The event has been canceled");
    }

    @Override
    public MessageResponse updateEvent(UUID eventId, UpdateEventDTO eventData, User user) {
        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!event.getCoordinator().getId().equals(user.getId()))
            throw new IsNotOwnerException("You are not the event coordinator");

        if (eventData.getCategory() != null) {
            Category category = this.categoryRepository
                    .findById(eventData.getCategory())
                    .orElseThrow(CategoryNotFoundException::new);
            event.setCategory(category);
        }

        if (eventData.getName() != null)
            event.setName(eventData.getName());

        if (eventData.getDescription() != null)
            event.setDescription(eventData.getDescription());

        if (eventData.getRealizationDate() != null)
            event.setRealizationDate(eventData.getRealizationDate());

        if (eventData.getFinishDate() != null)
            event.setFinishDate(eventData.getFinishDate());

        if (eventData.getLocal() != null)
            event.setLocal(eventData.getLocal());

        if (eventData.getVisibility() != null)
            event.setVisibility(eventData.getVisibility());

        this.eventRepository.save(event);

        return new MessageResponse("Event updated successfully");
    }
    @Override
    public ListResponse<EventItem> getUserPublicEvents(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Event> publicEvents = eventRepository.findByCoordinatorAndVisibilityOrderByCreatedAtDesc(user, Visibility.PUBLIC);

        return new ListResponse<>(EventMapper.INSTANCE.toList(publicEvents));
    }
}
