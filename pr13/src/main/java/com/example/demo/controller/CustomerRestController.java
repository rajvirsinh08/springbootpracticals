package com.example.demo.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
	
	private final CustomerRepository customerRepository;

	public CustomerRestController(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}
	
	//add customer
	@PostMapping
	public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer)
	{
		Customer savedCustomer = customerRepository.save(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
	}
	
	//GetById Customer
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id)
	{
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isPresent())
		{
			return ResponseEntity.ok(customer.get());
		}
		return ResponseEntity.noContent().build();
	}
	
	//update customer
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@Valid @PathVariable Integer id,@RequestBody Customer customerDetails)
	{
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if(optionalCustomer.isPresent())
		{
			Customer customer = optionalCustomer.get();
			customer.setName(customerDetails.getName());
			customer.setEmail(customerDetails.getEmail());
			customer.setAge(customerDetails.getAge());
			Customer updatedCustomer = customerRepository.save(customer);
			return ResponseEntity.ok(updatedCustomer);
		}
		return ResponseEntity.noContent().build();
	}
	
	//delete customer
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@Valid @PathVariable Integer id)
	{
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if(optionalCustomer.isPresent())
		{
			Customer customer = optionalCustomer.get();
			customerRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}

}
