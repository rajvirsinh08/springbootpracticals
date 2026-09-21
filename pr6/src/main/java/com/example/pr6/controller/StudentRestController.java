package com.example.pr6.controller;

import com.example.pr6.entity.Course;
import com.example.pr6.entity.Student;
import com.example.pr6.repository.CourseRepository;
import com.example.pr6.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentRestController(
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @RequestBody Student student) {

        Set<Course> courses = new HashSet<>();

        if (student.getCourses() != null) {

            for (Course course : student.getCourses()) {

                if (course.getCourseId() == 0) {
                    continue;
                }

                Optional<Course> existingCourse =
                        courseRepository.findById(
                                course.getCourseId()
                        );

                if (existingCourse.isPresent()) {
                    courses.add(existingCourse.get());
                }
            }
        }

        student.setCourses(courses);

        Student savedStudent =
                studentRepository.save(student);

        return new ResponseEntity<>(
                savedStudent,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(
            @PathVariable int id) {

        Optional<Student> student =
                studentRepository.findById(id);

        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/enroll/{courseId}")
    public ResponseEntity<Student> enrollCourse(
            @PathVariable int id,
            @PathVariable int courseId) {

        Optional<Student> studentOptional =
                studentRepository.findById(id);

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Course> courseOptional =
                courseRepository.findById(courseId);

        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOptional.get();

        student.getCourses().add(
                courseOptional.get()
        );

        Student updatedStudent =
                studentRepository.save(student);

        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentCourses(
            @PathVariable int id,
            @RequestBody Student studentDetails) {

        Optional<Student> studentOptional =
                studentRepository.findById(id);

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOptional.get();

        Set<Course> courses = new HashSet<>();

        if (studentDetails.getCourses() != null) {

            for (Course course : studentDetails.getCourses()) {

                Optional<Course> existingCourse =
                        courseRepository.findById(
                                course.getCourseId()
                        );

                if (existingCourse.isPresent()) {
                    courses.add(existingCourse.get());
                }
            }
        }

        student.setCourses(courses);

        Student updatedStudent =
                studentRepository.save(student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}/unenroll/{courseId}")
    public ResponseEntity<Void> unenrollCourse(
            @PathVariable int studentId,
            @PathVariable int courseId) {

        Optional<Student> studentOptional =
                studentRepository.findById(studentId);

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Course> courseOptional =
                courseRepository.findById(courseId);

        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOptional.get();

        student.getCourses().remove(
                courseOptional.get()
        );

        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }
}