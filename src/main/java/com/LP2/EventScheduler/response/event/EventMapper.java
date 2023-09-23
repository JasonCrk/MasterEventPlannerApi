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
    List<EventItem> toList(List<Event> events);
}
