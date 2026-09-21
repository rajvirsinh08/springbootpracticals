package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Note;

@RestController
@RequestMapping("/api")
public class NoteController {

    // In-memory Note list
    private List<Note> notes = new ArrayList<>();

    // ID counter
    private int nextId = 1;

    //public get
    @GetMapping("/public/data")
    public String publicData() {

        return "This is public data";
    }

    //secure get 
    @GetMapping("/secure/data")
    public List<Note> getNotes() {

        return notes;
    }

    //secure post
    @PostMapping("/secure/data")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {

        // Generate ID automatically
        note.setId(nextId);

        nextId++;

        notes.add(note);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(note);
    }

    //secure put 
    @PutMapping("/secure/data/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Integer id,@RequestBody Note noteDetails) {

        for (Note note : notes) {

            if (note.getId().equals(id)) {

                note.setText(noteDetails.getText());

                return ResponseEntity.ok(note);
            }
        }

        return ResponseEntity.notFound().build();
    }

    //secure delete
    @DeleteMapping("/secure/data/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Integer id) {

        for (Note note : notes) {

            if (note.getId().equals(id)) {

                notes.remove(note);

                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}