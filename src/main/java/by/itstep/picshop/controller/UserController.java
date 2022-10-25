package by.itstep.picshop.controller;

import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public String userList(Model model){
        List<UserDTO> all = userService.getAll();
        if(all.isEmpty())
        model.addAttribute("users", all);
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model){
        model.addAttribute("user", new UserDTO());
        return "newUser";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO userDTO, Model model){
        if (userService.save(userDTO)){
            return "redirect:/users";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

}
