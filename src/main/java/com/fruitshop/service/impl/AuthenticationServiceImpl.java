package com.fruitshop.service.impl;

import com.fruitshop.constant.SessionConstant;
import com.fruitshop.dto.request.RegisterRequest;
import com.fruitshop.dto.request.VerificationRequest;
import com.fruitshop.entity.Role;
import com.fruitshop.repository.*;
import com.fruitshop.service.EmailSenderService;
import com.fruitshop.utils.VerificationCodeGenerator;
import com.fruitshop.utils.VerifyCodeManager;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.response.AuthenticationResponse;
import com.fruitshop.dto.response.UserResponse;
import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;
import com.fruitshop.entity.User;
import com.fruitshop.mapper.UserMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.AuthenticationService;
import com.fruitshop.service.JwtService;
import com.fruitshop.service.RefreshTokenService;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired
  private AuthenticationManager authenticationManager;


  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartDetailRepository cartDetailRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private EmailSenderService emailSenderService;

  @Override
  public ResponseObject login(LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    Login login = (Login) authentication.getPrincipal();

    if (ObjectUtils.isNotEmpty(login) && !login.getState()) {
      return new ResponseObject(HttpStatus.UNAUTHORIZED, "Tài khoản chưa được xác thực, xin vui lòng xác thực tài khoản.", null);
    }

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(login);

    refreshTokenRepository.save(refreshToken);
    String jwtToken = "Bearer " + jwtService.generateToken(login);

    User user = userRepository.findByLogin(login);
    UserResponse userResponse = UserMapper.INSTANT.entityToResponse(user);
    userResponse.setUsername(loginRequest.getUsername());
    int cartItem = 0;
    if (ObjectUtils.isNotEmpty(user)) cartItem = cartDetailRepository.getCountProductByUserId(user.getId());

    return new ResponseObject(HttpStatus.OK, "Đăng nhập thành công.", new AuthenticationResponse(jwtToken, refreshToken.getToken(), userResponse, cartItem));
  }

  @Override
  public ResponseObject register(RegisterRequest registerDto) throws MessagingException {

    if (loginRepository.existsById(registerDto.getUsername())) {
      return new ResponseObject(HttpStatus.CONFLICT, "Tên tài khoản đã tồn tại.", null);
    }
    if (loginRepository.existsByEmail(registerDto.getEmail())) {
      return new ResponseObject(HttpStatus.CONFLICT, "Email đã được sử dụng bởi một tài khoản khác.", null);
    }

    String hashPassword = passwordEncoder.encode(registerDto.getPassword());
    registerDto.setPassword(hashPassword);
    Login login = Login.builder().email(registerDto.getEmail())
        .username(registerDto.getUsername())
        .password(hashPassword).state(false).build();
    Optional<Role> role = roleRepository.findByName("USER");
    if (role.isPresent())
      login.setRole(role.get());

    String verifyCode = VerificationCodeGenerator.generate();
    emailSenderService.sendVerificationEmail(login.getEmail(), login.getUsername(), verifyCode);
    login.setOTP(verifyCode);
    loginRepository.save(login);
    VerifyCodeManager verifyCodeManager = new VerifyCodeManager();
    ScheduledFuture<?> scheduledFuture = verifyCodeManager.scheduleVerificationCleanup(SessionConstant.OTP_EXPIRE_TIME, login.getUsername(),
        loginRepository);

    User user = new User();
    user.setLogin(login);
    userRepository.save(user);

    return new ResponseObject(HttpStatus.CREATED, "Tạo tài khoản thành công, hãy xác thực tài khoản để có thể sử dụng.", registerDto.getEmail());
  }

  @Override
  public ResponseObject verification(VerificationRequest verificationDto) {
    Optional<Login> loginDB = loginRepository.findById(verificationDto.getUsername());
    Login login;
    if (!loginDB.isPresent()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản.", null);
    else login = loginDB.get();

    if (login.getState() == true)
      return new ResponseObject(HttpStatus.ALREADY_REPORTED, "Tài khoản đã được xác thực trước đó.", null);

    String verificationCode = new String();
    if (ObjectUtils.isNotEmpty(login.getOTP())) verificationCode = login.getOTP();
    else return new ResponseObject(HttpStatus.UNAUTHORIZED, "Bạn chưa gửi yêu cầu xác thực.", null);
    if (ObjectUtils.isNotEmpty(verificationDto.getVerificationCode()) && ObjectUtils.isNotEmpty(verificationCode)
        && verificationDto.getVerificationCode().equals(verificationCode)) {
      login.setState(true);
      login.setOTP(null);
      loginRepository.save(login);
      return new ResponseObject(HttpStatus.ACCEPTED, "Xác thực tài khoản thành công.", null);
    }
    return new ResponseObject(HttpStatus.BAD_REQUEST, "OTP không trùng khớp.", null);
  }

  @Override
  public ResponseObject getNewVerification(String username) throws MessagingException {
    Optional<Login> loginDB = loginRepository.findById(username);
    Login login;
    if (!loginDB.isPresent()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản.", null);
    else login = loginDB.get();

    if (login.getState() == true) {
      return new ResponseObject(HttpStatus.ALREADY_REPORTED, "Tài khoản đã được xác thực trước đó.", null);
    }

    String verifyCode = VerificationCodeGenerator.generate();
    emailSenderService.sendVerificationEmail(login.getEmail(), login.getUsername(), verifyCode);
    login.setOTP(verifyCode);
    loginRepository.save(login);
    VerifyCodeManager verifyCodeManager = new VerifyCodeManager();
    ScheduledFuture<?> scheduledFuture = verifyCodeManager.scheduleVerificationCleanup(SessionConstant.OTP_EXPIRE_TIME, login.getUsername(),
        loginRepository);
    return new ResponseObject(HttpStatus.ACCEPTED, "Yêu cầu xác thực thành công", login.getEmail());
  }
}
