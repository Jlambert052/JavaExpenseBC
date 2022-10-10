package com.bootcamp.capstone.practice.ExpenseDb.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Expense;

public interface ExpensesRepository extends CrudRepository<Expense, Integer>{

	Iterable<Expense> findByStatus(String status);
	Iterable<Expense> findByEmployeeId(int id);
}
