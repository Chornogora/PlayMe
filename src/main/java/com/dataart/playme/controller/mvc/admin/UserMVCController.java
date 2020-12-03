package com.dataart.playme.controller.mvc.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class UserMVCController {

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("parameter", "earthman");
        return "hello";
    }
}
