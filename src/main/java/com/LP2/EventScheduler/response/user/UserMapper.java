package com.LP2.EventScheduler.response.user;

import com.LP2.EventScheduler.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "account.picture", target = "picture")
    SimpleUserResponse toSimpleResponse(User user);
}
