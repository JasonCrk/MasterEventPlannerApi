package com.LP2.EventScheduler.validation.isImage;

import com.LP2.EventScheduler.util.FileUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class IsImageValidator implements ConstraintValidator<IsImage, MultipartFile> {

    @Autowired
    private FileUtils fileUtils;

    private final String[] imageExtensions = {".jpg", ".png", ".jpeg"};

    @Override
    public void initialize(IsImage constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null) return true;
        String fileExtension = fileUtils.getExtensionFile(multipartFile.getOriginalFilename());
        return Arrays.asList(imageExtensions).contains(fileExtension);
    }
}
