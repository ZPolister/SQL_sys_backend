package cn.polister.infosys.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVo {
    //账户ID
    private Long accountId;
    //用户名
    private String username;
    //账户类型（0-个人，1-管理员）
    private Integer accountType;
    //生日
    private String birthday;
    //邮箱
    private String email;
}
