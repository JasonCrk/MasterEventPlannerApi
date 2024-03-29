package com.LP2.EventScheduler.controller;

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
import com.LP2.EventScheduler.service.event.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Event")
@RestController
@RequestMapping(path = "/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Search public events")
    @GetMapping(path = "/search", name = "searchPublicEvents")
    public ResponseEntity<ListResponse<EventItem>> searchPublicEvents(
            @RequestParam(name = "q") String searchQuery,
            @RequestParam(required = false) EventSortingOptions sortBy,
            @RequestParam(required = false, name = "category") String categoryName
    ) {
        return ResponseEntity.ok(
                eventService.searchPublicEvents(searchQuery, sortBy, categoryName)
        );
    }

    @Operation(summary = "Retrieve events in which the user participates or are theirs")
    @GetMapping(path = "/participating")
    public ResponseEntity<ListResponse<EventItem>> searchForEventsYouParticipateIn(
            @RequestParam(required = false) EventSortingOptions sortBy,
            @RequestParam(required = false, name = "category") String categoryName,
            @RequestAttribute("user") User authUser
    ) {
        return new ResponseEntity<>(
                this.eventService.searchForEventsYouParticipateIn(sortBy, categoryName, authUser),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve public events from a user")
    @GetMapping(path = "/public/{userId}")
    public ResponseEntity<ListResponse<EventItem>> getUserPublicEvents(
            @PathVariable("userId") UUID userId
    ) {
        ListResponse<EventItem> userPublicEvents = eventService.getUserPublicEvents(userId);

        return ResponseEntity.ok(eventService.getUserPublicEvents(userId));
    }

    @Operation(summary = "Retrieve event details by id")
    @GetMapping(path = "/{eventId}")
    public ResponseEntity<EventDetails> getEventDetails(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User authUser
    ) {
        return new ResponseEntity<>(this.eventService.getEventDetails(eventId, authUser), HttpStatus.OK);
    }

    @Operation(summary = "Create a new event")
    @PostMapping
    public ResponseEntity<EntityWithMessageResponse<EventItem>> scheduleEvent(
            @Valid @RequestBody CreateEventDTO eventData,
            @RequestAttribute("user") User user
    ) {
        return new ResponseEntity<>(this.eventService.scheduleEvent(eventData, user), HttpStatus.CREATED);
    }

    @Operation(summary = "The user can join (participate) in an event")
    @PostMapping(path = "/{eventId}/join")
    public ResponseEntity<MessageResponse> joinTheEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User user,
            @Valid @RequestBody JoinEventDTO joinData
    ) {
        return new ResponseEntity<>(this.eventService.joinEvent(eventId, joinData, user), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove event by id")
    @DeleteMapping(path = "/{eventId}/remove")
    public ResponseEntity<MessageResponse> removeEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User user
    ) {
        return new ResponseEntity<>(this.eventService.removeEvent(eventId, user), HttpStatus.OK);
    }

    @Operation(summary = "Change the status of an event to \"cancelled\"")
    @PostMapping(path = "/{eventId}/cancel")
    public ResponseEntity<MessageResponse> cancelEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.eventService.cancelEvent(eventId, authUser));
    }

    @Operation(summary = "Update a event by id")
    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<MessageResponse> updateEvent(
            @PathVariable("eventId") UUID eventId,
            @RequestAttribute("user") User user,
            @Valid @RequestBody UpdateEventDTO eventData
    ) {
        return new ResponseEntity<>(this.eventService.updateEvent(eventId, eventData, user), HttpStatus.OK);
    }
}
