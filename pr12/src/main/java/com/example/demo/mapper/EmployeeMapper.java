package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;

public class EmployeeMapper {
	
	//Entity - DTO
	public static EmployeeDTO toDto(Employee employee)
	{
		EmployeeDTO dto = new EmployeeDTO();
		dto.setEmpId(employee.getEmpId());
		dto.setEmpName(employee.getEmpName());
		dto.setDepartment(employee.getDepartment());
		return dto;
	}
	
	//DTO - Entity
	public static Employee toEntity(EmployeeDTO employeeDTO)
	{
		Employee employee = new Employee();
		employee.setEmpId(employeeDTO.getEmpId());
		employee.setEmpName(employeeDTO.getEmpName());
		employee.setDepartment(employeeDTO.getDepartment());
		return employee;
	}

}
