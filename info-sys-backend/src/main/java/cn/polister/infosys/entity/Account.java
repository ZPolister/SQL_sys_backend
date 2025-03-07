package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 账户表(Account)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:39:05
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
public class Account  {
//账户ID
    @TableId
    private Long accountId;

//用户名
    private String username;
//加密密码
    private String passwordHash;
//账户类型（0-个人，1-管理员）
    private Integer accountType;
//邮箱
    private String email;
//生日
    private String birthday;
//账户状态（0-停用，1-启用）
    private Integer accountStatus;
//创建时间
    private Date createdAt;


}
