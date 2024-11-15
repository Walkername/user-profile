package ru.walkername.user_profile.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user_profile")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 5, max = 20, message = "Username should be greater than 5 and less than 20 characters")
    @Column(name = "username")
    private String username;

    // TODO: create name field, like: Timur Walker ...

    // TODO: create email field

    @Column(name = "description")
    private String description;

    //private String photoUrl;

    //private Movie favouriteMovie;

    //@ManyToMany()
    //private List<Movie> movies;

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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