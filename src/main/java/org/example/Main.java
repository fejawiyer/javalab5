package org.example;

import com.google.gson.Gson;
import org.example.model.Book;
import org.example.model.Person;
import org.example.service.BookService;
import org.example.service.MusicService;
import org.example.service.PersonService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        MusicService service;
        PersonService personService;
        BookService bookService;
        try {
            DataSourceManager manager = DataSourceManager.getInstance();
            service = new MusicService(manager.getDataSource());
            personService = new PersonService(manager.getDataSource());
            bookService = new BookService(manager.getDataSource());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Задание 1");
        service.findAll().orElse(new ArrayList<>()).forEach(System.out::println);
        System.out.println("Задание 2");
        service.findMusicWOSymbols("mt").orElse(new ArrayList<>()).forEach(System.out::println);
        System.out.println("Задание 3");
        //service.addMusic(80, "Fear of the Dark");
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader("books.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Задание 4");
        Gson gson = new Gson();
        Person[] visitors = gson.fromJson(buf, Person[].class);
        List<Book> books = Arrays.stream(visitors).flatMap(visitor -> visitor.
                getFavoriteBooks().stream().distinct()).toList();
        personService.createTable();
        personService.insert(List.of(visitors));
        bookService.createTable();
        bookService.insert(books);
        System.out.println("Задание 5");
        bookService.selectSorted().orElse(new ArrayList<>()).forEach(System.out::println);
        System.out.println("Задание 6");
        bookService.selectYear(2000).orElse(new ArrayList<>()).forEach(System.out::println);
        System.out.println("Задание 7");
        bookService.insert(200, "Flowers for Algernon", "Daniel Keyes", 1966, "", "");
        personService.insert(200, "Anatoly", "Churakov", "123-456-789", false);
        bookService.findById(200).ifPresent(System.out::println);
        personService.findById(200).ifPresent(System.out::println);
        System.out.println("Задание 8");
        personService.drop();
        bookService.drop();
    }
}