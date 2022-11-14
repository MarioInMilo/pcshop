package by.itstep.picshop.service;

import by.itstep.picshop.bean.request.CreateUserRequest;
import by.itstep.picshop.bean.response.CreateUserResponse;
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
    User findByName(String name);
    void updateProfile(UserDTO userDTO);
    void deleteUser(UserDTO userDTO);
    boolean existUserById(Long id);



    CreateUserResponse createUser(CreateUserRequest request);

    UserDTO updateRole(long id, String role);
}
