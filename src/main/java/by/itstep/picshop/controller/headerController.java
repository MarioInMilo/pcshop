package by.itstep.picshop.controller;

import by.itstep.picshop.model.User;
import by.itstep.picshop.service.BasketService;
import by.itstep.picshop.service.ProductService;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class headerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private BasketService basketService;


    @GetMapping
    public String getUserById(Principal principal, Model model) {
        List<User> users = new ArrayList<>();
        users.add(userService.findByName(principal.getName()));
        model.addAttribute("users", users);
        return "header";
    }
}
