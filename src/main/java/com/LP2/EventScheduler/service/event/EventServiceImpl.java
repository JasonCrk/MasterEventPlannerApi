package com.LP2.EventScheduler.service.event;

import com.LP2.EventScheduler.dto.event.CreateEventDTO;
import com.LP2.EventScheduler.exception.CategoryNotFoundException;
import com.LP2.EventScheduler.exception.ScheduleCreationException;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.Visibility;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.LP2.EventScheduler.repository.EventRepository;
import com.LP2.EventScheduler.response.EntityWithMessageResponse;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.response.event.EventMapper;
import com.LP2.EventScheduler.scheduler.SchedulerService;
import com.LP2.EventScheduler.scheduler.job.SendNotificationsAboutTimeLeftForEvent;

import lombok.RequiredArgsConstructor;

import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private final Scheduler scheduler;
    private final SchedulerService schedulerService;

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
                .build();

        Event savedEvent = this.eventRepository.save(newEvent);

        int hourOfRealizationDateOfEvent = eventData.getRealizationDate().getHour();
        int minuteOfRealizationDateOfEvent = eventData.getRealizationDate().getMinute();

        JobDataMap jobData = new JobDataMap();

        jobData.put("eventId", savedEvent.getId());
        jobData.put("eventRealizationDate", savedEvent.getId());

        try {
            JobDetail scheduleNotificationsJobDetail = this.schedulerService.buildJobDetail(
                    new SendNotificationsAboutTimeLeftForEvent(),
                    jobData,
                    "job detail to schedule notifications",
                    "schedule-notifications-job"
            );
            Trigger scheduleNotificationsTrigger = this.schedulerService.buildTriggerWithCronSchedule(
                    scheduleNotificationsJobDetail,
                    "schedule-notifications-trigger",
                    "trigger to schedule notifications",
                    CronScheduleBuilder.cronSchedule(minuteOfRealizationDateOfEvent + " " + hourOfRealizationDateOfEvent + " * * * ?")
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
}
