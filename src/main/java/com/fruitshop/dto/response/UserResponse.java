package com.fruitshop.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Integer id;

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String gender;
	private String username;
	private Date birthDay;
	private String avatar;
}
