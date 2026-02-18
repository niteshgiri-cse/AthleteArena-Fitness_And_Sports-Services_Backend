package com.niteshgiri.AthleteArena.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    @Column(unique = true,nullable = false)
    private String username;
    @Email
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;
    @Size(min = 8, max = 16, message = "Password should be length 8 to 16 character ")
    private String password;
}
