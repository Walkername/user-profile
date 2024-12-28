package ru.walkername.user_profile.util;

public class UserNotUpdatedException extends RuntimeException {

    public UserNotUpdatedException(String msg) {
        super(msg);
    }

}
