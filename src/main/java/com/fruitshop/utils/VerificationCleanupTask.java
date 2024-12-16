package com.fruitshop.utils;

import com.fruitshop.entity.Login;
import com.fruitshop.repository.LoginRepository;

import java.util.TimerTask;

public class VerificationCleanupTask extends TimerTask {
	private String username;

    private LoginRepository loginRepository;


    public VerificationCleanupTask(String username, LoginRepository loginRepository) {
		super();
		this.username = username;
		this.loginRepository = loginRepository;
	}

	@Override

    public void run() {
       loginRepository.removeVerificationCode(username);
    }
}
