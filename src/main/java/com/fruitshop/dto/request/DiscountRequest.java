package com.fruitshop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant effectiveDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant expiryDate;
  private Double value;
  private List<Integer> products;

   public void setEffectiveDate(String effectiveDate) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      this.effectiveDate = LocalDateTime.parse(effectiveDate, formatter).toInstant(ZoneOffset.UTC);
  }

  public void setExpiryDate(String expiryDate) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      this.expiryDate = LocalDateTime.parse(expiryDate, formatter).toInstant(ZoneOffset.UTC);
  }
}
