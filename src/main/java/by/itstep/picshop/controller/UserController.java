package by.itstep.picshop.controller;

import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.ProductMapper;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Role;
import by.itstep.picshop.model.User;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

//import static by.itstep.picshop.mapper.UserMapper.fromUserDTO;
//import static by.itstep.picshop.mapper.UserMapper.toUserDTO;

@Controller
//@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    private final UseMapper mapper = UseMapper.MAPPER;


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
        userDTO.setRole(Role.CLIENT);
        if (userService.save(userDTO)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal user) {
        if (user == null) {
            throw new RuntimeException("Not authorize");
        }
        User user1 = userService.findByName(user.getName());

        model.addAttribute("user", mapper.fromUser(user1));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal user) {
        if (user == null || !Objects.equals(user.getName(), dto.getUsername())) {
            throw new RuntimeException("Not authorize");
        }
        if (dto.getPassword() != null &&
                !dto.getPassword().isEmpty() &&
                !Objects.equals(dto.getPassword(), dto.getMatching())) {
            model.addAttribute("user", dto);
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUser(userService.getById(id).orElseThrow());
        return "redirect:/users";
    }

    // переделать ---->
    @PostMapping("/{id}/edit")
    public String userEdit(@PathVariable(value = "id") long id, Model model) {

        if (!userService.existUserById(id)) return "redirect:/users";


//        Optional<User> user = Optional.ofNullable(
//                fromUserDTO(
//                        userService.getById(id)
//                                .get()));
        List<UserDTO> list = new ArrayList<>();
//        user.ifPresent(list::add);
        Optional<UserDTO> us = userService.getById(id);
        us.ifPresent(list::add);

        model.addAttribute("user", us.stream().collect(Collectors.toList()));
        return "editProfile";
    }

    @PostMapping("/{id}/{role}")
//    @PreAuthorize(Role.MANAGER)
    public String changeRole(@PathVariable(value = "id") long id, @PathVariable(value = "role") String role, Model model) {
        UserDTO userDTO = userService.updateRole(id, role);
        model.addAttribute("user", userDTO);
        return "redirect:/users";
    }

}
