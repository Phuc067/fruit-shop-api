package com.fruitshop.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Random;

public class RandomUtils {

	
	public static final String getUniqueId() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4)
				.appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2)
				.appendValue(ChronoField.HOUR_OF_DAY, 2).appendValue(ChronoField.MINUTE_OF_HOUR, 2)
				.appendValue(ChronoField.SECOND_OF_MINUTE, 2).toFormatter();

		String timestamp = formatter.format(LocalDateTime.now());

		StringBuilder randomChars = new StringBuilder();

		Random random = new Random();
		for( int i = 0;i<6 ;i++)
		{
			int index = random.nextInt(chars.length());
			randomChars.append(chars.charAt(index));
		}
		String uniqueID = timestamp + randomChars;


		return uniqueID;
	}
	
}
