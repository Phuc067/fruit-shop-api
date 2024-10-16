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
@Table(name = "logins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login implements Serializable{

	private static final long serialVersionUID = 4244998233461933536L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String username;
	private String password;
	private String email;
	private String OTP;
	private Boolean state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;
}
