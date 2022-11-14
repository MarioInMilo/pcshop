package by.itstep.picshop.controller;

import by.itstep.picshop.bean.request.CreateUserRequest;
import by.itstep.picshop.bean.response.CreateUserResponse;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/create/user")
    private String createUser(CreateUserRequest request, Model model) {
        CreateUserResponse response = userService.createUser(request);
        if (response.getCode() != null
                && response.getCode() > 299) {
            model.addAttribute("error", response.getMessage());
            return "html";
        } else {
            model.addAttribute("user", response);
        }
        return null;
    }

}

