package com.LP2.EventScheduler.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotificationByToken(NotificationMessage notificationMessage) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setImage(notificationMessage.getImage())
                .setBody(notificationMessage.getMessage())
                .build();

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        firebaseMessaging.send(message);
    }
}
