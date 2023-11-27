package com.LP2.EventScheduler.service.account;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.exception.AccountNotFoundException;
import com.LP2.EventScheduler.exception.FailedImageUploadException;
import com.LP2.EventScheduler.firebase.FirebaseStorageService;
import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.repository.AccountRepository;
import com.LP2.EventScheduler.repository.InvitationRepository;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.account.AccountDetails;
import com.LP2.EventScheduler.response.account.AccountMapper;

import com.LP2.EventScheduler.response.user.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final InvitationRepository invitationRepository;

    private final FirebaseStorageService storageService;

    @Override
    public AccountDetails retrieveAccount(UUID accountId, User authUser) {
        Account account = this.accountRepository
                .findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Optional<Invitation> invitationSent = Optional.empty();

        if (!account.getUser().getId().equals(authUser.getId())) {
            invitationSent = this.invitationRepository
                    .findInvitationBetweenUsers(authUser, account.getUser());
        }

        return AccountMapper.INSTANCE.toDetailResponse(account, invitationSent);
    }

    @Override
    public MessageResponse updateAccount(UpdateAccountDTO accountData, User authUser) {

        Account account = authUser.getAccount();

        if (accountData.getBanner() != null) {
            try {
                String uploadBannerUrl = this.storageService.uploadImage(accountData.getBanner());
                account.setBanner(uploadBannerUrl);
            } catch (IOException e) {
                throw new FailedImageUploadException("There was an error updating the banner");
            }
        }

        if (accountData.getPicture() != null) {
            try {
                String uploadPictureUrl = this.storageService.uploadImage(accountData.getPicture());
                account.setPicture(uploadPictureUrl);
            } catch (IOException e) {
                throw new FailedImageUploadException("There was an error updating the picture");
            }
        }

        if (accountData.getAbout() != null)
            account.setAbout(accountData.getAbout());

        this.accountRepository.save(account);

        return new MessageResponse("The account has been updated successfully");
    }
}
