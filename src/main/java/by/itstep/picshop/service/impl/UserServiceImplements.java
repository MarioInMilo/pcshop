package by.itstep.picshop.service.impl;

import by.itstep.picshop.bean.request.CreateUserRequest;
import by.itstep.picshop.bean.response.CreateUserResponse;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Role;
import by.itstep.picshop.model.User;
import by.itstep.picshop.repository.UserRepository;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplements implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UseMapper mapper = UseMapper.MAPPER;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(UserDTO userDTO) {
        if (Objects.equals(userDTO.getPassword(), userDTO.getMatching())) {
            repository.save(Objects.requireNonNull(mapper.toUser(userDTO))); //fromUserDTO(userDTO)
            return true;
        } else {
            throw new RuntimeException("Password is not equals");
        }
    }


    @Override
    public List<UserDTO> getAll() {
        List<User> all = repository.findAll();
        return !all.isEmpty() ?
                all.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()) :
                null;
    }

    @Override
    public Optional<UserDTO> getById(Long id) {
        return Optional.ofNullable( //toUserDTO
                mapper.fromUser(
                        repository.findById(id)
                                .orElseThrow()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserDTO> updateUser(User user) {
        if (getById(user.getId()).isPresent()) {
            save(Objects.requireNonNull(mapper.fromUser(user))); //toUserDTO(user)
        }
        return getAll();
    }

    @Override
    public User findByName(String name) {
        return repository.findFirstByName(name);
    }

    @Override
    @Transactional
    public void updateProfile(UserDTO userDTO) {
        boolean isChanged = false;
        User save = repository.findFirstByName(userDTO.getUsername());
        if (save == null) {
            throw new RuntimeException("User not found" + userDTO.getUsername());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            save.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (Objects.equals(userDTO.getEmail(), save.getEmail())) {
            save.setEmail(userDTO.getEmail());
            isChanged = true;
        }
        if (isChanged) {
            repository.save(save);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UserDTO userDTO) {
        repository.delete(Objects.requireNonNull(mapper.toUser(userDTO))); //fromUserDTO(userDTO)
    }

    @Override
    public boolean existUserById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        return null;
    }

//    @Override
//    public CreateUserResponse createUser(CreateUserRequest request) {
//        CreateUserResponse response;
//        if (Objects.nonNull(request)
//                && request.getName() != null
//                && request.getPassword() != null) {
//            response = userToUserResponse(
//                    repository.save(userCreateRequestToUser(request))
//            );
//        } else {
//            response = new CreateUserResponse(400, "User not created");
//        }
//        return response;
//    }

    @Override
    public UserDTO updateRole(long id, String role) {
        User user = repository.findById(id).orElse(null);
        if (Objects.nonNull(user)) {
            user.setRole(Role.valueOf(role));
        }
        return mapper.fromUser(user); //toUserDTO(user)
    }

    private UserDTO toDTO(User user) {
        return mapper.fromUser(user);
    } //toUserDTO

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findFirstByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name" + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                roles
        );
    }
}
