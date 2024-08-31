package cn.polister.infosys.controller;

import cn.polister.infosys.entity.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping
    ResponseResult test() {
        return ResponseResult.okResult();
    }
}
