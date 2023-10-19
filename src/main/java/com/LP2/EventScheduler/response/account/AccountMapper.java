package com.LP2.EventScheduler.response.account;

import com.LP2.EventScheduler.model.Account;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDetails toDetailResponse(Account account);
}
