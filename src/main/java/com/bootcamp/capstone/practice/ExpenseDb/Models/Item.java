package com.bootcamp.capstone.practice.ExpenseDb.Models;

import javax.persistence.*;

@Entity
@Table(name="Items")
public class Item {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(length=40, nullable=false)
	private String name;
	
	@Column(columnDefinition="decimal(9,2) NOT NULL DEFAULT 0")
	private double price;
	
	public Item() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
