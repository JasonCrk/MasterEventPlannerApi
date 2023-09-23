package com.LP2.EventScheduler.service.event;

import com.LP2.EventScheduler.exception.CategoryNotFoundException;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.enums.Visibility;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.LP2.EventScheduler.repository.EventRepository;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.response.event.EventMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

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
}
