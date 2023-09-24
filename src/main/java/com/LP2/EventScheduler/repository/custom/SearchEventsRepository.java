package com.LP2.EventScheduler.repository.custom;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.enums.Visibility;

import java.util.List;

public interface SearchEventsRepository {
    List<Event> searchEvents(
            String query,
            EventSortingOptions sortBy,
            Visibility visibility,
            Category category
    ) throws IllegalArgumentException;
}
