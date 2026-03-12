package ru.shop.tyzhprogramist.tyzhprogramist.exception;

public class CategoryCycleException extends RuntimeException {
    public CategoryCycleException(String message) {
        super(message);
    }
}