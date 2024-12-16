package com.fruitshop.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {
	void sendVerificationEmail(String toEmail,String username, String verificationCode) throws MessagingException;
}
