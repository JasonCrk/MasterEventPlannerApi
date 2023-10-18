package com.LP2.EventScheduler.dto.account;

import com.LP2.EventScheduler.validation.isImage.IsImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDTO {

    @IsImage
    private MultipartFile picture;

    @IsImage
    private MultipartFile banner;

    private String location;

    private String about;
}
