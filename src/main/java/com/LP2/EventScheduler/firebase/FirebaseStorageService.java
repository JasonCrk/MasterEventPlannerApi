package com.LP2.EventScheduler.firebase;

import com.LP2.EventScheduler.util.FileUtils;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    @Autowired(required = false)
    private Storage storage;

    @Autowired
    private FileUtils fileUtils;

    @Value("${firebase.enabled}")
    private boolean firebaseEnabled;

    @Value("${firebase.cloud-storage.bucket-name}")
    private String bucketName;

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        if (!firebaseEnabled) return null;

        final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + this.bucketName + "/o/%s?alt=media";

        String fileName = multipartFile.getOriginalFilename();
        String extensionFile = this.fileUtils.getExtensionFile(fileName);

        fileName = UUID.randomUUID().toString().concat(extensionFile);

        File file = this.fileUtils.convertToFile(multipartFile, fileName);

        String contentType = this.fileUtils.getImageContentType(extensionFile);

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        this.storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String mediaUrl = String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        file.delete();

        return mediaUrl;
    }
}
