package com.fruitshop.utils;

import java.time.Duration;
import java.time.Instant;

public class TimeUtils {
	
	private static final Instant gmt_7(Instant time)
	{
		Duration sevenHours = Duration.ofHours(7); 
	    Instant newInstant = time.plus(sevenHours);
	    return newInstant;
	}
	
	public static final Instant getInstantNow() {
		Instant currentInstant = Instant.now(); 
        return gmt_7(currentInstant);
	}
}
