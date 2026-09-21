package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.AdminData;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final List<AdminData> data = new ArrayList<>();

    public AdminController() {
        data.add(new AdminData(1, "Initial Policy"));
        data.add(new AdminData(2, "Security Policy"));
    }

    @GetMapping("/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminData>> getAdminData() {

        return ResponseEntity.ok(data);
    }

    @PostMapping("/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminData> addData(
            @RequestBody AdminData newData) {

        int newId = data.size() + 1;

        newData.setId(newId);

        data.add(newData);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newData);
    }

    @PutMapping("/data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateData(
            @PathVariable Integer id,
            @RequestBody AdminData updatedData) {

        for (AdminData item : data) {

            if (item.getId().equals(id)) {

                item.setLabel(updatedData.getLabel());

                return ResponseEntity.ok(item);
            }
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Data not found");
    }

    @DeleteMapping("/data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteData(
            @PathVariable Integer id) {

        boolean removed = data.removeIf(
                item -> item.getId().equals(id)
        );

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}