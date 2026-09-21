package com.example.pr6.repository;

import com.example.pr6.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository
        extends JpaRepository<Course, Integer> {

}