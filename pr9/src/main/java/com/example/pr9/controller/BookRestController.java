package com.example.pr9.controller;

import com.example.pr9.entity.Book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final List<Book> bookList = new ArrayList<>();

    private int nextId = 1;

    // POST /api/books
    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book) {

        book.setId(nextId++);

        bookList.add(book);

        URI location = URI.create("/api/books/" + book.getId());

        return ResponseEntity
                .created(location)
                .body(book);
    }

    // GET /api/books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(bookList);
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable int id) {

        for (Book book : bookList) {

            if (book.getId() == id) {
                return ResponseEntity.ok(book);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable int id,
            @RequestBody Book updatedBook) {

        for (Book book : bookList) {

            if (book.getId() == id) {

                if (updatedBook.getTitle() != null) {
                    book.setTitle(updatedBook.getTitle());
                }

                if (updatedBook.getAuthor() != null) {
                    book.setAuthor(updatedBook.getAuthor());
                }

                book.setPrice(updatedBook.getPrice());

                return ResponseEntity.ok(book);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable int id) {

        for (Book book : bookList) {

            if (book.getId() == id) {

                bookList.remove(book);

                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}