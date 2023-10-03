package com.LP2.EventScheduler.response.event;

import com.LP2.EventScheduler.model.Event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "coordinator.account.picture", target = "coordinator.picture")
    @Mapping(expression = "java(event.getRealizationDate().toString())", target = "realizationDate")
    @Mapping(expression = "java(event.getCreatedAt().toString())", target = "createdAt")
    List<EventItem> toList(List<Event> events);

    @Mapping(source = "coordinator.account.picture", target = "coordinator.picture")
    @Mapping(expression = "java(event.getRealizationDate().toString())", target = "realizationDate")
    @Mapping(expression = "java(event.getCreatedAt().toString())", target = "createdAt")
    EventItem toResponse(Event event);
}
