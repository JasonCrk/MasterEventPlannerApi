package com.LP2.EventScheduler.scheduler.job;

import com.LP2.EventScheduler.exception.EventNotFoundException;
import com.LP2.EventScheduler.exception.FailedNotificationSendingException;
import com.LP2.EventScheduler.firebase.FirebaseCloudMessagingService;
import com.LP2.EventScheduler.firebase.NotificationMessage;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.Participation;
import com.LP2.EventScheduler.repository.EventRepository;

import com.google.firebase.messaging.FirebaseMessagingException;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class SendNotificationsAboutTimeLeftForEvent extends QuartzJobBean {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FirebaseCloudMessagingService messagingService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap data = context.getMergedJobDataMap();

        UUID eventId = UUID.fromString(data.getString("eventId"));

        Event event = this.eventRepository
                .findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        LocalDateTime eventRealizationDate = LocalDateTime.parse(data.getString("eventRealizationDate"));
        LocalDateTime currentTime = LocalDateTime.now();

        String notificationMessage = "The time remaining for the \"" + event.getName() + "\" event is ";

        if (ChronoUnit.MINUTES.between(currentTime, eventRealizationDate) == 0L) {
            notificationMessage = "It's time for the \"" + event.getName() +  "\" event";
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

        for (Participation participation : event.getParticipants()) {
            try {
                NotificationMessage notification = new NotificationMessage();
                notification.setTitle("Master Event Planner");
                notification.setMessage(notificationMessage);
                notification.setRecipientToken(participation.getToken());

                this.messagingService.sendNotificationByToken(notification);
            } catch (FirebaseMessagingException e) {
                throw new FailedNotificationSendingException();
            }
        }
    }
}
