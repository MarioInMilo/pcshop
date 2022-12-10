package by.itstep.picshop.controller;

import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.MapperUser;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Role;
import by.itstep.picshop.model.User;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    private final UseMapper mapper = UseMapper.MAPPER;

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @GetMapping
    public String userList(Model model) {
        List<UserDTO> all = userService.getAll();
        if (!all.isEmpty())
            model.addAttribute("users", all);
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "newUser";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO userDTO, Model model) {
        if (userService.addUser(userDTO)) {
            return "redirect:/";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model,
                           @PathVariable(value = "code") String code) {
        Boolean isActivated = userService.verifyUser(code);
        if (isActivated) {
            model.addAttribute("message", "success");
        } else {
            model.addAttribute("message", "user not found");
        }

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        User user1 = userService.findByName(principal.getName());

        model.addAttribute("user", mapper.fromUser(user1));
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize2");
        }
        if (dto.getPassword() != null &&
                !dto.getPassword().isEmpty() &&
                !Objects.equals(dto.getNewPassword(), dto.getMatching())) {
            model.addAttribute("user", dto);
            return "profile";
        }
        userService.updateProfile(dto, principal.getName());
        return "redirect:/logout";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUser(userService.getById(id).orElseThrow());
        return "redirect:/users";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String userEdit(@PathVariable(value = "id") long id, Model model) {
        if (!userService.existUserById(id)) return "redirect:/users";
        Optional<UserDTO> user = userService.getById(id);
        model.addAttribute("user", user.stream().collect(Collectors.toList()));
        return "editProfile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable(value = "id") long id,
                             @RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String role) {
        UserDTO userDTO = userService.getById(id).orElseThrow();
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        userDTO.setRole(Role.valueOf(role));
        userService.updateUser(MapperUser.toUser(userDTO));
        return "redirect:/users";
    }

}
