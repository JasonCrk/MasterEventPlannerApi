package com.LP2.EventScheduler.service.event;

import com.LP2.EventScheduler.dto.event.CreateEventDTO;
import com.LP2.EventScheduler.dto.event.JoinEventDTO;
import com.LP2.EventScheduler.dto.event.UpdateEventDTO;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.EntityWithMessageResponse;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.event.EventDetails;
import com.LP2.EventScheduler.response.event.EventItem;

import java.util.UUID;

public interface EventService {
    ListResponse<EventItem> searchPublicEvents(
            String searchQuery,
            EventSortingOptions sortBy,
            String categoryName
    );
    ListResponse<EventItem> searchForEventsYouParticipateIn(
            EventSortingOptions sortBy,
            String categoryName,
            User authUser
    );
    EventDetails getEventDetails(UUID eventId, User authUser);
    EntityWithMessageResponse<EventItem> scheduleEvent(CreateEventDTO eventData, User user);
    MessageResponse joinEvent(UUID eventId, JoinEventDTO joinData, User user);
    MessageResponse updateEvent(UUID eventId, UpdateEventDTO eventData, User user);
}
