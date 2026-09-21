package com.example.pr4.controller;

import com.example.pr4.entity.Country;
import com.example.pr4.repository.CountryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/countries")
public class CountryRestController {

    private final CountryRepository countryRepository;

    public CountryRestController(
            CountryRepository countryRepository) {

        this.countryRepository = countryRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Country> createCountry(
            @RequestBody Country country) {

        if (country.getCapital() != null) {
            country.getCapital().setCountry(country);
        }

        Country savedCountry =
                countryRepository.save(country);

        return new ResponseEntity<>(
                savedCountry,
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {

        return ResponseEntity.ok(
                countryRepository.findAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountry(
            @PathVariable int id) {

        Optional<Country> country =
                countryRepository.findById(id);

        if (country.isPresent()) {
            return ResponseEntity.ok(country.get());
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(
            @PathVariable int id) {

        Optional<Country> country =
                countryRepository.findById(id);

        if (country.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        countryRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}