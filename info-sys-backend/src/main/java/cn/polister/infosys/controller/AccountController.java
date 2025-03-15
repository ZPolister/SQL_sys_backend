package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.entity.Account;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.UserInfoDto;
import cn.polister.infosys.entity.vo.UserInfoVo;
import cn.polister.infosys.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Tag(name = "用户信息管理模块")
public class AccountController {
    @Resource
    private AccountService accountService;

    @GetMapping
    @SaCheckLogin
    @Operation(summary = "获取用户信息")
    public ResponseResult<UserInfoVo> getUserInfo() {
        return ResponseResult.okResult(
                BeanUtil.toBean(accountService.getById(StpUtil.getLoginIdAsLong()), UserInfoVo.class),
                UserInfoVo.class
        );
    }

    @PutMapping
    @SaCheckLogin
    @Operation(summary = "更新用户信息")
    public ResponseResult<Void> updateUserInfo(@RequestBody UserInfoDto dto) {
        Account account = BeanUtil.toBean(dto, Account.class);
        account.setAccountId(StpUtil.getLoginIdAsLong());
        accountService.updateById(account);
        return ResponseResult.okResult();
    }
}
