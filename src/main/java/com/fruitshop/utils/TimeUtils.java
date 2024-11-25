package com.fruitshop.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

	private static final Instant gmt_7(Instant time) {
		Duration sevenHours = Duration.ofHours(7);
		Instant newInstant = time.plus(sevenHours);
		return newInstant;
	}

	public static final Instant getInstantNow() {
		Instant currentInstant = Instant.now();
		return gmt_7(currentInstant);
	}

	public static final Instant stringTimeToInstant(String dateTimeString) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

		return instant;
	}
}
