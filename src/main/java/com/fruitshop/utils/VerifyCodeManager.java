package com.fruitshop.utils;

import com.fruitshop.repository.LoginRepository;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class VerifyCodeManager {
	private Timer timer = new Timer();

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public ScheduledFuture<?> scheduleVerificationCleanup(long delayMilliseconds, String username , LoginRepository loginRepository) {
    	return executor.schedule(new VerificationCleanupTask(username, loginRepository), delayMilliseconds, TimeUnit.MILLISECONDS);
    }
}
