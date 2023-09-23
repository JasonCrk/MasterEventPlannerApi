package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.service.event.EventService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping(path = "/search", name = "searchPublicEvents")
    public ResponseEntity<ListResponse<EventItem>> searchPublicEvents(
            @RequestParam String searchQuery,
            @RequestParam(required = false) EventSortingOptions sortBy,
            @RequestParam(required = false, name = "category") String categoryName
    ) {
        return ResponseEntity.ok(
                eventService.searchPublicEvents(searchQuery, sortBy, categoryName)
        );
    }
}
