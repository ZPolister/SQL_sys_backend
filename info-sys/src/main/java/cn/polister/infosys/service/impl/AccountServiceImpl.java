package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.Account;
import cn.polister.infosys.mapper.AccountMapper;
import cn.polister.infosys.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 账户表(Account)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:39:05
 */
@Service("accountService")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
