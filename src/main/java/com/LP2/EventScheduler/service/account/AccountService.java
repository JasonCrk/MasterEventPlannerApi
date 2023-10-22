package com.LP2.EventScheduler.service.account;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.account.AccountDetails;

import java.util.UUID;

public interface AccountService {
    AccountDetails retrieveAccount(UUID accountId);
    MessageResponse updateAccount(UpdateAccountDTO accountData, User authUser);
}
