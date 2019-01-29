package com.example.user.pushapp.microservices.book.model;

import java.time.LocalDateTime;

public class Book {

    private String phone;

    private String bookDate;

    private int placeID;

    public Book(String phone, String bookDate, int placeID) {
        this.phone = phone;
        this.bookDate = bookDate;
        this.placeID = placeID;
    }
}
