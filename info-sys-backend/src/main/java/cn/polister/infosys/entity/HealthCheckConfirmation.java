package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 体检提醒确认表(HealthCheckConfirmation)表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_check_confirmation")
public class HealthCheckConfirmation {
    //确认ID
    @TableId
    private Long confirmationId;
    //提醒ID
    private Long reminderId;
    //确认令牌
    private String confirmationToken;
    //是否已确认
    private Integer isConfirmed;
    //创建时间
    private Date createdAt;
    //确认时间
    private Date confirmedAt;
}