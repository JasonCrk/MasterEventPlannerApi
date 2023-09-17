package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.service.event.EventService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping(path = "/seach")
    public List<?> searchPublicEvents() {
        return eventService.searchPublicEvents();
    }
}
