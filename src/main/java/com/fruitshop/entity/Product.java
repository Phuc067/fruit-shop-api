package com.fruitshop.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable{

	private static final long serialVersionUID = 8947300456531341086L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Product(Integer id) {
		this.id = id;
	}
	
	private String title;
	private String description;
	private String origin;
	private Double price;
	private Integer quantity;
	private String image;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryId", referencedColumnName = "id")
	private Category category;
	
	
}
