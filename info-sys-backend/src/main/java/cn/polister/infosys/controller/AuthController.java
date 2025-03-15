package cn.polister.infosys.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.LoginDto;
import cn.polister.infosys.entity.dto.RegisterDto;
import cn.polister.infosys.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Tag(name = "认证模块", description = "用户注册登录相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private AccountService accountService;

    @Operation(summary = "发送验证码", description = "向指定邮箱发送6位数字验证码")
    @ApiResponse(responseCode = "200", description = "验证码发送成功")
    @ApiResponse(responseCode = "400", description = "邮箱格式错误")
    @PostMapping("/send-code")
public ResponseResult<Void> sendVerificationCode(
        @Parameter(description = "注册邮箱", required = true, example = "user@example.com")
        @RequestParam @Email(message = "邮箱格式错误") String email) {
    accountService.sendVerificationCode(email);
    return ResponseResult.okResult();
}

@Operation(summary = "用户注册", description = "使用邮箱验证码完成注册")
@ApiResponse(responseCode = "200", description = "注册成功")
@ApiResponse(responseCode = "400", description = "参数校验失败/验证码错误")
@PostMapping("/register")
public ResponseResult<Void> register(
     @RequestBody RegisterDto dto) {
    accountService.register(dto);
    return ResponseResult.okResult();
}

@Operation(summary = "用户登录", description = "使用用户名/邮箱登录获取Token")
@ApiResponse(responseCode = "200", description = "登录成功")
@ApiResponse(responseCode = "505", description = "用户名或密码错误")
@ApiResponse(responseCode = "511", description = "账户被禁用")
@PostMapping("/login")
public ResponseResult<Map<String, String>> login(@RequestBody LoginDto dto) {
    String token = accountService.login(dto);
    return ResponseResult.okResult(Collections.singletonMap("token", token));
}

@PostMapping("/logout")
@Operation(summary = "退出登录", description = "退出当前登录")
public ResponseResult<Void> logout() {
    StpUtil.logout();
    return ResponseResult.okResult();
}

@GetMapping("/checkLogin")
@Operation(summary = "检查是否登录")
public ResponseResult<Boolean> checkIsLogin() {
    return ResponseResult.okResult(StpUtil.isLogin());
}
}