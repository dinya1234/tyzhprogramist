package ru.shop.tyzhprogramist.tyzhprogramist.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}