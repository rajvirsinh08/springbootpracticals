package com.example.pr6.controller;

import com.example.pr6.entity.Course;
import com.example.pr6.entity.Student;
import com.example.pr6.repository.CourseRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    private final CourseRepository courseRepository;

    public CourseRestController(
            CourseRepository courseRepository) {

        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(
            @RequestBody Course course) {

        Course savedCourse =
                courseRepository.save(course);

        return new ResponseEntity<>(
                savedCourse,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(
            @PathVariable int id) {

        Optional<Course> course =
                courseRepository.findById(id);

        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Set<Student>> getCourseStudents(
            @PathVariable int id) {

        Optional<Course> course =
                courseRepository.findById(id);

        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                course.get().getStudents()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable int id,
            @RequestBody Course courseDetails) {

        Optional<Course> existingCourse =
                courseRepository.findById(id);

        if (existingCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = existingCourse.get();

        if (courseDetails.getCourseName() != null) {
            course.setCourseName(
                    courseDetails.getCourseName()
            );
        }

        Course updatedCourse =
                courseRepository.save(course);

        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable int id) {

        Optional<Course> existingCourse =
                courseRepository.findById(id);

        if (existingCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        courseRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}