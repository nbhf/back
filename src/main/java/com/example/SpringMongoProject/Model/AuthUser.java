package com.example.SpringMongoProject.Model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("user")
public class AuthUser {
    @Getter
    @Id
    private String id;
    @Indexed
    private String username;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private boolean active;
}
