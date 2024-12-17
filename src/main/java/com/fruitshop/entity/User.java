package com.fruitshop.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{

	private static final long serialVersionUID = -5865425017557784567L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public User(Integer id){
		this.id = id;
	}
	
	private String firstName;
	private String lastName;
	private String phone;
	@Temporal(TemporalType.DATE)
	private Date birthDay;
	private String gender;
	private String avatar;
	
	
	@OneToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private Login login;
}
