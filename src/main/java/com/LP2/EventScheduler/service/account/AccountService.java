package com.LP2.EventScheduler.service.account;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;

public interface AccountService {
    MessageResponse updateAccount(UpdateAccountDTO accountData, User authUser);
}
