package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "注册请求参数")
public class RegisterDto {
    @Schema(description = "用户名", example = "health_user", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "邮箱地址", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码（6-20位字符）", example = "securePwd123", minLength = 6, maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 6, max = 20, message = "密码必须6-20位")
    private String password;

    @Schema(description = "邮箱验证码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "生日（字符串形式）", example = "2004-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请填写生日信息")
    private String birthday;
}