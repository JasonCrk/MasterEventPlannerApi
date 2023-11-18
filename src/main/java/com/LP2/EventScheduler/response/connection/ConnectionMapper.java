package com.LP2.EventScheduler.response.connection;

import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;
import com.LP2.EventScheduler.response.user.UserMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    ConnectionMapper INSTANCE = Mappers.getMapper(ConnectionMapper.class);

    @Mapping(expression = "java(connection.getId())", target = "id")
    @Mapping(expression = "java(userToSimpleUserResponse(connection.getConnector().getId().equals(authUser.getId()) ? connection.getConnecting() : connection.getConnector()))", target = "user")
    ConnectionResponse toResponse(Connection connection, User authUser);

    default SimpleUserResponse userToSimpleUserResponse(User user) {
        return Mappers.getMapper(UserMapper.class).toSimpleResponse(user);
    }

    default List<ConnectionResponse> toList(List<Connection> connections, User authUser) {
        return connections.stream()
                .map(connection -> toResponse(connection, authUser))
                .collect(Collectors.toList());
    }
}
