package com.LP2.EventScheduler.response.account;

import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.Invitation;

import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.user.UserMapper;
import com.LP2.EventScheduler.response.user.UserUsernameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(expression = "java(invitation.isPresent() ? invitation.get().getStatus().toString() : null)", target = "invitationSent")
    AccountDetails toDetailResponse(Account account, Optional<Invitation> invitation);

    default UserUsernameResponse userToUserUsernameResponse(User user) {
        return Mappers.getMapper(UserMapper.class).toUsernameResponse(user);
    }

    default void mapUserToAccountDetails (User user, AccountDetails accountDetails) {
        accountDetails.setUser(userToUserUsernameResponse(user));
    }
}
