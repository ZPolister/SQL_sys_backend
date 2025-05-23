package cn.polister.infosys.service;

import cn.polister.infosys.entity.Account;
import cn.polister.infosys.entity.dto.LoginDto;
import cn.polister.infosys.entity.dto.RegisterDto;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 账户表(Account)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:39:05
 */
public interface AccountService extends IService<Account> {
    void sendVerificationCode(String email);
    void register(RegisterDto dto);
    String login(LoginDto dto);

}
