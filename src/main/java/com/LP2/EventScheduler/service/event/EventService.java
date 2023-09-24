package com.LP2.EventScheduler.service.event;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.event.EventItem;

public interface EventService {
    ListResponse<EventItem> searchPublicEvents(
            String searchQuery,
            EventSortingOptions sortBy,
            String categoryName
    );
}
