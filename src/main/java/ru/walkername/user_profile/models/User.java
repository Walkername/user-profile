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
    @Size(max = 500, message = "Description should be less than 500 characters")
    private String description;

    @Column(name = "average_rating")
    private double averageRating;

    @Column(name = "scores")
    private int scores;

    public User() {

    }

    public User(String username, String description, double averageRating, int scores) {
        this.username = username;
        this.description = description;
        this.averageRating = averageRating;
        this.scores = scores;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
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
