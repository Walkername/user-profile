package ru.walkername.user_profile.dto;

import ru.walkername.user_profile.models.User;

public class UserDetails extends User {

    private int movieId;

    private double rating;

    public UserDetails() {

    }

    public UserDetails(User user, int movieId, double rating) {
        super(user.getUsername(), user.getDescription());
        this.movieId = movieId;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
