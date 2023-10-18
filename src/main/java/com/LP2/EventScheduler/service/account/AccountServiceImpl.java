package com.LP2.EventScheduler.service.account;

import com.LP2.EventScheduler.dto.account.UpdateAccountDTO;
import com.LP2.EventScheduler.exception.FailedImageUploadException;
import com.LP2.EventScheduler.firebase.FirebaseStorageService;
import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.repository.AccountRepository;
import com.LP2.EventScheduler.response.MessageResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final FirebaseStorageService storageService;

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

        account.setAbout(accountData.getAbout());
        account.setLocation(accountData.getLocation());

        this.accountRepository.save(account);

        return new MessageResponse("The account has been updated successfully");
    }
}
