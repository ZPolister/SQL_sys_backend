package cn.polister.infosys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootController {
    @GetMapping("/")
    public RedirectView redirectToPolisterBlog() {
        return new RedirectView("https://blog.polister.cn");
    }
}