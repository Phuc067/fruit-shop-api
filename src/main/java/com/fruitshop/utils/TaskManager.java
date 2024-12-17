package com.fruitshop.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class TaskManager {
	private static Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();


    public static Boolean addTask(String key, ScheduledFuture<?> scheduledFuture)
    {
    	try {
        	scheduledTasks.put(key, scheduledFuture);
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

    }

    public static void cancelTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(taskId);
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }
}
