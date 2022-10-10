package com.bootcamp.capstone.practice.ExpenseDb.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Expenseline;

public interface ExpenselinesRepository extends CrudRepository<Expenseline, Integer>{

}
