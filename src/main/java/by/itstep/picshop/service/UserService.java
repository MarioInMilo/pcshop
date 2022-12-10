package by.itstep.picshop.service;

import by.itstep.picshop.bean.request.CreateUserRequest;
import by.itstep.picshop.bean.response.CreateUserResponse;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService { //Security

    boolean save(UserDTO userDTO);

    List<UserDTO> getAll();

    Optional<UserDTO> getById(Long id);

    List<UserDTO> updateUser(User user);

    User findByName(String name);

    void updateProfile(UserDTO userDTO, String name);

    void deleteUser(UserDTO userDTO);

    boolean existUserById(Long id);

    void save(User user);

    CreateUserResponse createUser(CreateUserRequest request);

    UserDTO updateRole(long id, String role);

    BigDecimal getBalance(UserDTO userDTO);

    Boolean addUser(UserDTO userDTO);

    Boolean verifyUser(String code);

    void banUserById(Long id);

    void unBanUserById(Long id);
}
