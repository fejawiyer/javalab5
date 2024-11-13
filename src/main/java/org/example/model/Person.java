package org.example.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Data
@RequiredArgsConstructor
public class Person {
    @NonNull private String name;
    @NonNull private String surname;
    @NonNull private String phone;
    @NonNull private Boolean subscribed;
    private ArrayList<Book> favoriteBooks;
}