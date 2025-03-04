package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.Account;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 账户表(Account)表数据库访问层
 *
 * @author Polister
 * @since 2025-03-02 20:39:05
 */
public interface AccountMapper extends BaseMapper<Account> {
    default boolean existsByUsername(String username) {
        return selectCount(new QueryWrapper<Account>().eq("username", username)) > 0;
    }

    default boolean existsByEmail(String email) {
        return selectCount(new QueryWrapper<Account>().eq("email", email)) > 0;
    }
}
