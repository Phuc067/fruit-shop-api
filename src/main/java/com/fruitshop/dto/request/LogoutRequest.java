package com.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
  private String accessToken;
  private String refreshToken;
}
