package com.example.pr3.repository;

import com.example.pr3.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartment(String department);
    long countByDepartment(String department);
    List<Employee> findBySalaryGreaterThan(double salary);

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(
            @Param("min") double min,
            @Param("max") double max
    );
}