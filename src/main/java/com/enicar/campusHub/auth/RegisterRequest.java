package com.enicar.campusHub.auth;
import lombok.Data;

@Data
    public class RegisterRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String role; // "STUDENT" or "CLUB_HEAD"
    }


