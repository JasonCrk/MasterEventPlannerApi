package com.LP2.EventScheduler.response.invitation;

import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;
import com.LP2.EventScheduler.response.user.UserMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationMapper INSTANCE = Mappers.getMapper(InvitationMapper.class);

    @Mapping(expression = "java(invitation.getNotifiedAt().toString())", target = "notifiedAt")
    @Mapping(source = "inviter", target = "user")
    InvitationResponse toResponse(Invitation invitation);

    @Mapping(expression = "java(invitation.getNotifiedAt().toString())", target = "notifiedAt")
    @Mapping(source = "inviter", target = "user")
    List<InvitationResponse> toList(List<Invitation> invitations);

    default SimpleUserResponse userToSimpleUserResponse(User user) {
        return Mappers.getMapper(UserMapper.class).toSimpleResponse(user);
    }
}
