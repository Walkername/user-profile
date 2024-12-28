package ru.walkername.user_profile.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotEmpty(message = "Nickname should not be empty")
    @Size(min = 5, max = 20, message = "Nickname should be greater than 5 and less than 20 characters")
    @Column(name = "username")
    private String username;

    // TODO: create name field, like: Timur Walker ...

    // TODO: create email field

    @Column(name = "description")
    @Size(max = 500, message = "Description should be less than 500 characters")
    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
