package com.LP2.EventScheduler.response.user;

import com.LP2.EventScheduler.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userName", target = "username")
    SimpleUserResponse toSimpleResponse(User user);

    @Mapping(source = "userName", target = "username")
    UserUsernameResponse toUsernameResponse(User user);
}
