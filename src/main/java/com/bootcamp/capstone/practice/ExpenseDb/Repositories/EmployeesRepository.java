package com.bootcamp.capstone.practice.ExpenseDb.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Employee;

public interface EmployeesRepository extends CrudRepository<Employee, Integer>{

	Optional<Employee> findByEmailAndPassword(String email, String password);
}
