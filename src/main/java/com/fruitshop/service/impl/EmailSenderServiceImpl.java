package com.fruitshop.service.impl;

import com.fruitshop.constant.SessionConstant;
import com.fruitshop.model.TimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fruitshop.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

  @Autowired
  private JavaMailSender mailSender;

  @Override
  public void sendVerificationEmail(String toEmail, String username, String verificationCode) throws MessagingException {
MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("Fruit shop");
		helper.setTo(toEmail);
		helper.setSubject("Xác thực tài khoản Fruit Shop");

		TimeFormat time = new TimeFormat(SessionConstant.OTP_EXPIRE_TIME);

		String htmlBody = "<div>\r\n" + "    <h1 style=\"color: darkcyan;\">Mã đặt xác thực tài khoản</h1>\r\n"
				+ "    <p>Xin dùng mã này để xác thực cho tài khoản Fruit Shop " + username + " .</p>\r\n"
				+ "    <p>Đây là mã của bạn: <strong> " + verificationCode + "</strong></p>\r\n"
				+ "    <p>Nếu bạn không sử dụng liên kết này trong vòng " + time.toString()
				+ ", nó sẽ hết hạn. Để nhận liên kết đặt lại mật khẩu mới, hãy\r\n" + "        truy cập: </p>\r\n"
				+ "    <p>Xin cám ơn,</p>\r\n" + "    <p>Nhóm tài khoản Hat Shop</p>\r\n" + "</div>";

		helper.setText(htmlBody, true);
		mailSender.send(message);
  }
}
