package com.bootcamp.capstone.practice.ExpenseDb.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Item;

public interface ItemsRepository extends CrudRepository<Item, Integer>{

}
