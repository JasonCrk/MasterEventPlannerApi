package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.event.CreateEventDTO;
import com.LP2.EventScheduler.dto.event.JoinEventDTO;
import com.LP2.EventScheduler.dto.event.UpdateEventDTO;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.EntityWithMessageResponse;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.service.event.EventService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping
    public ResponseEntity<EntityWithMessageResponse<EventItem>> scheduleEvent(
            @Valid @RequestBody CreateEventDTO eventData,
            @RequestAttribute("user") User user
    ) {
        return new ResponseEntity<>(this.eventService.scheduleEvent(eventData, user), HttpStatus.CREATED);
    }

    @PostMapping(path = "/{eventId}/join")
    public ResponseEntity<MessageResponse> joinTheEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User user,
            @Valid @RequestBody JoinEventDTO joinData
    ) {
        return new ResponseEntity<>(this.eventService.joinEvent(eventId, joinData, user), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<MessageResponse> updateEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User user,
            @Valid @RequestBody UpdateEventDTO eventData
    ) {
        return new ResponseEntity<>(this.eventService.updateEvent(eventId, eventData, user), HttpStatus.OK);
    }
}
