package com.fruitshop.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String gender;
	private Date birthDay;
	private String avatar;
}
