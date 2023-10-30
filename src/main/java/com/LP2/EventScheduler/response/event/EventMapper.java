package com.LP2.EventScheduler.response.event;

import com.LP2.EventScheduler.model.Event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(expression = "java(event.getCreatedAt() == null ? null : event.getCreatedAt().toString())", target = "createdAt")
    @Mapping(
            expression = "java(event.getRealizationDate() == null ? null : event.getRealizationDate().toString())",
            target = "realizationDate"
    )
    List<EventItem> toList(List<Event> events);

    @Mapping(expression = "java(event.getCreatedAt() == null ? null : event.getCreatedAt().toString())", target = "createdAt")
    @Mapping(
            expression = "java(event.getRealizationDate() == null ? null : event.getRealizationDate().toString())",
            target = "realizationDate"
    )
    EventItem toResponse(Event event);

    @Mapping(source = "isUserParticipating", target = "participating")
    @Mapping(expression = "java(event.getCreatedAt() == null ? null : event.getCreatedAt().toString())", target = "createdAt")
    @Mapping(expression = "java(event.getFinishDate() == null ? null : event.getFinishDate().toString())", target = "finishDate")
    @Mapping(
            expression = "java(event.getRealizationDate() == null ? null : event.getRealizationDate().toString())",
            target = "realizationDate"
    )
    EventDetails toDetail(Event event, boolean isUserParticipating);
}
