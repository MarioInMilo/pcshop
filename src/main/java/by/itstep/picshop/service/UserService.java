package by.itstep.picshop.service;

import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService { //Security
    boolean save(UserDTO userDTO);
    List<UserDTO> getAll();
    Optional<UserDTO> getById(Long id);
    List<UserDTO> updateUser(User user);

}
