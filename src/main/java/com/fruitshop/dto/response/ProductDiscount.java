package com.fruitshop.dto.response;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "ProductDiscountMapping",
        classes = @ConstructorResult(
            targetClass = ProductDiscount.class,
            columns = {
                @ColumnResult(name = "value", type = Integer.class),
                @ColumnResult(name = "expiry_date", type = Instant.class)
            }
        )
    )
public class ProductDiscount {
	 private Float value;
	 private Timestamp expiredDate;
}
