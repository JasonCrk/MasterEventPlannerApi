package com.LP2.EventScheduler.response.connection;

import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.user.UserMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ConnectionMapper {

    ConnectionMapper INSTANCE = Mappers.getMapper(ConnectionMapper.class);

    @Mapping(expression = "java(connection.getId())", target = "id")
    @Mapping(expression = "java(connection.getConnector().getId().equals(authUser.getId()) ? userMapper.toSimpleResponse(connection.getConnecting()) : userMapper.toSimpleResponse(connection.getConnector()))", target = "user")
    ConnectionResponse toResponse(Connection connection, User authUser, UserMapper userMapper);

    default List<ConnectionResponse> toList(List<Connection> connections, User authUser, UserMapper userMapper) {
        return connections.stream()
                .map(connection -> toResponse(connection, authUser, userMapper))
                .collect(Collectors.toList());
    }
}
