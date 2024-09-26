package com.shubham.lightbill.lightbill_backend.service;

import com.shubham.lightbill.lightbill_backend.constants.OtpType;
import com.shubham.lightbill.lightbill_backend.constants.Role;
import com.shubham.lightbill.lightbill_backend.constants.WalletStatus;
import com.shubham.lightbill.lightbill_backend.dto.SignUpDto;
import com.shubham.lightbill.lightbill_backend.model.Otp;
import com.shubham.lightbill.lightbill_backend.model.OtpCompositeKey;
import com.shubham.lightbill.lightbill_backend.model.User;
import com.shubham.lightbill.lightbill_backend.model.Wallet;
import com.shubham.lightbill.lightbill_backend.repository.OtpRepository;
import com.shubham.lightbill.lightbill_backend.repository.UserRepository;
import com.shubham.lightbill.lightbill_backend.repository.WalletRepository;
import com.shubham.lightbill.lightbill_backend.response.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private ThreadPoolService threadPool;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private OtpRepository otpRepository;
    @Value("${otp.Expiration.time}")
    private long otpExpirationTime;

    public void sendEmail(String email, String username, String otp) throws MessagingException, IOException {
        Runnable task = () -> {
            try{
                emailService.sendOtpMail(email, username, otp);
            } catch (Exception ignored) {}
        };
        threadPool.submitTask(task);
    }

    public User generateUser(SignUpDto req){
        User user = User.builder()
                .userId(idGeneratorService.generateId(User.class.getName(), String.valueOf(req.getRole())))
                .name(req.getName())
                .email(req.getEmail())
                .phNo(req.getPhNo())
                .address(req.getPhNo())
                .role(req.getRole())
                .isActive(true)
                .isBlocked(false)
                .build();
        return userRepository.save(user);
    }

    public Wallet generateWallet(User user){
        Wallet wallet = Wallet.builder()
                .walletId(idGeneratorService.generateId(Wallet.class.getName(), "Wallet"))
                .user(user)
                .status(WalletStatus.ACTIVE)
                .balance(0)
                .build();
        return walletRepository.save(wallet);
    }

    public List<Object> signUpUser(@Valid SignUpDto req){
        User user = generateUser(req);
        Wallet wallet = generateWallet(user);

        user.setWallet(wallet);
        userRepository.save(user);

        List<Object> res = new ArrayList<>();
        res.add(user);
        res.add(wallet);
        return res;
    }

    public Otp generateOtp(User user, String generatedOtp){
        Otp otp = Otp.builder()
                .key(
                        OtpCompositeKey.builder()
                                .type(OtpType.VERIFY_USER)
                                .user(user)
                        .build()
                )
                .otp(generatedOtp)
                .build();
        return otpRepository.save(otp);
    }

    public void sendOtpToEmail(String userId) throws Exception {
        SecureRandom random = new SecureRandom();
        String generatedOtp = String.format("%06d", random.nextInt(999999));

        User user = userRepository.findByUserId(userId);
        if(user == null) throw new Exception("User Not Found");

        Otp otp = generateOtp(user, generatedOtp);
        sendEmail(user.getEmail(), user.getName(), generatedOtp);
    }

    public Boolean verifyOtp(String userOtp, String userId, OtpType type){
        User user = userRepository.findByUserId(userId);
        OtpCompositeKey key = OtpCompositeKey.builder()
                .user(user)
                .type(type)
                .build();

        Otp dbOtp = otpRepository.findByKey(key);
        long duration = Duration.between(Instant.now(), dbOtp.getUpdatedTime().toInstant()).toMillis();

        return duration <= otpExpirationTime && Objects.equals(userOtp, dbOtp.getOtp());
    }
}
