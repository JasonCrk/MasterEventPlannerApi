package com.LP2.EventScheduler.scheduler.job;

import com.LP2.EventScheduler.firebase.FirebaseCloudMessagingService;
import com.LP2.EventScheduler.firebase.NotificationMessage;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.Participation;
import com.LP2.EventScheduler.model.enums.EventStatus;
import com.LP2.EventScheduler.repository.EventRepository;
import com.LP2.EventScheduler.repository.ParticipationRepository;

import com.google.firebase.messaging.FirebaseMessagingException;

import jakarta.transaction.Transactional;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SendNotificationsAboutTimeLeftForEvent extends QuartzJobBean {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private FirebaseCloudMessagingService messagingService;

    @Autowired
    private Scheduler scheduler;

    @Transactional
    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap data = context.getMergedJobDataMap();

        UUID eventId = UUID.fromString(data.getString("eventId"));

        Event event = this.eventRepository
                .findById(eventId)
                .orElse(null);

        if (event == null) return;

        LocalDateTime eventRealizationDate = LocalDateTime.parse(data.getString("eventRealizationDate"));
        LocalDateTime eventFinishDate = LocalDateTime.parse(data.getString("eventFinishDate"));
        LocalDateTime currentTime = LocalDateTime.now();

        String notificationMessage = "The time remaining for the \"" + event.getName() + "\" event is ";

        if (ChronoUnit.MINUTES.between(eventFinishDate, currentTime) >= 0L) {
            event.setStatus(EventStatus.FINALIZED);
            this.eventRepository.save(event);

            JobKey eventJob = JobKey.jobKey(
                    event.getId().toString(),
                    "schedule-notifications"
            );

            try {
                boolean isEventSchedulerDeleted = this.scheduler.deleteJob(eventJob);
                if (!isEventSchedulerDeleted)
                    System.out.println("Failed when trying to delete the event scheduler");
            } catch (SchedulerException e) {
                System.out.println("Failed when trying to delete the event scheduler");
            }

            return;
        }

        if (ChronoUnit.MINUTES.between(currentTime, eventRealizationDate) == 0L) {
            notificationMessage = "It's time for the \"" + event.getName() +  "\" event";

            event.setStatus(EventStatus.IN_PROGRESS);
            this.eventRepository.save(event);
        } else if (ChronoUnit.DAYS.between(currentTime, eventRealizationDate) == 1L) {
            notificationMessage += "24 hours";
        } else if (ChronoUnit.WEEKS.between(currentTime, eventRealizationDate) == 1L) {
            notificationMessage += "one week";
        } else if (ChronoUnit.MONTHS.between(currentTime, eventRealizationDate) == 1L) {
            notificationMessage += "one month";
        } else if (ChronoUnit.YEARS.between(currentTime, eventRealizationDate) == 1L) {
            notificationMessage += "one year";
        } else
            return;

        List<Participation> eventParticipations = this.participationRepository.findByEvent(event);

        for (Participation participation : eventParticipations) {
            try {
                NotificationMessage notification = new NotificationMessage();
                notification.setTitle("Master Event Planner");
                notification.setMessage(notificationMessage);
                notification.setRecipientToken(participation.getToken());
                notification.setData(new HashMap<>());

                this.messagingService.sendNotificationByToken(notification);
            } catch (FirebaseMessagingException e) {
                System.out.println("Ha ocurrido un fallo en el envío de notificaciones");
            }
        }
    }
}
