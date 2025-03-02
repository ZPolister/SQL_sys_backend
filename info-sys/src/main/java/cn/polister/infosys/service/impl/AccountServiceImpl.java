package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.Account;
import cn.polister.infosys.entity.dto.LoginDto;
import cn.polister.infosys.entity.dto.RegisterDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.exception.SystemException;
import cn.polister.infosys.mapper.AccountMapper;
import cn.polister.infosys.service.AccountService;
import cn.polister.infosys.utils.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 账户表(Account)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:39:05
 */
@Service("accountService")
@RequiredArgsConstructor
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    private final AccountMapper accountMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    private static final String VERIFY_CODE_KEY = "verify_code:";
    private static final Duration CODE_EXPIRE = Duration.ofMinutes(5);

    public void sendVerificationCode(String email) {
        String code = generateRandomCode();
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY + email, code, CODE_EXPIRE);
        sendVerificationEmail(email, code);
    }

    private String generateRandomCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@health.com");
        message.setTo(to);
        message.setSubject("健康管理系统注册验证码");
        message.setText("您的验证码是：" + code + "，有效期为5分钟");
        mailSender.send(message);
    }

    @Transactional
    public void register(RegisterDto dto) {
        validateVerificationCode(dto.getEmail(), dto.getCode());

        if (accountMapper.existsByUsername(dto.getUsername())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (accountMapper.existsByEmail(dto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setEmail(dto.getEmail());
        account.setPasswordHash(PasswordUtil.encode(dto.getPassword()));
        account.setAccountType(0);
        account.setAccountStatus(1);

        accountMapper.insert(account);
    }

    private void validateVerificationCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(VERIFY_CODE_KEY + email);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_CODE_ERROR);
        }
        redisTemplate.delete(VERIFY_CODE_KEY + email);
    }

    public String login(LoginDto dto) {
        Account account = accountMapper.selectOne(new QueryWrapper<Account>()
                .eq("username", dto.getIdentifier())
                .or().eq("email", dto.getIdentifier()));

        if (account == null || !PasswordUtil.matches(dto.getPassword(), account.getPasswordHash())) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }

        if (account.getAccountStatus() != 1) {
            throw new SystemException(AppHttpCodeEnum.USER_BANNED);
        }

        StpUtil.login(account.getAccountId());
        return StpUtil.getTokenValue();
    }
}
