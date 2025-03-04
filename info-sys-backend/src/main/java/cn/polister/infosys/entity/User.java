package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author Polister
 * @since 2024-08-31 22:41:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User  {
//用户id@TableId
    private Integer id;

//用户名
    private String name;
//用户班级
    private String className;
//用户邮箱
    private String email;
//二维码口令
    private String qrCodeKey;
//二维码口令过期时间
    private Date qrCodeExpiredTime;
//卡片码
    private String cardKey;
//密码
    private String password;
//创建时间
    private Date createTime;
//创建人id
    private Integer createBy;
//更新时间
    private Date updateTime;
//更新人id
    private Integer updateBy;
//账号状态
    private Integer status;
//备注
    private String remarks;


}
