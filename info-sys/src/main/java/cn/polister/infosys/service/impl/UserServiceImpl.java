package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.User;
import cn.polister.infosys.mapper.UserMapper;
import cn.polister.infosys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author Polister
 * @since 2024-08-31 22:41:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
