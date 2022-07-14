package com.codefactorygroup.betting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloWorldController {
    private final Map<Integer, String> books;

    public HelloWorldController() {
        this.books = Map.of(1, "Dune",
                2, "Harry Potter",
                3, "Idiotul");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @GetMapping("/hello2")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable("bookId") final Integer bookId) {
        if (!checkBookId(bookId)) {
            return "No book with such ID was found.";
        }
        return books.get(bookId);
    }

    public boolean checkBookId(final Integer bookId) {
        if (books.get(bookId) == null) {
            return false;
        }
        return true;
    }
}
