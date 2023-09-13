package com.LP2.EventScheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegisterDTO {

    @NotBlank(message = "The username is required")
    @Size(min = 3, max = 60)
    private String username;

    @Email(message = "The email is invalid")
    @NotBlank(message = "The email is required")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "The password is required")
    @Size(min = 5)
    private String password;
}
