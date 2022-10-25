package by.itstep.picshop.service.impl;

import by.itstep.picshop.dto.UserDTO;
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(UserDTO userDTO) {
        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatching())){
            throw new RuntimeException("Password is not equals");
        }
        User user = User.builder()
                .name(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .role(Role.CLIENT)
                .build();
        repository.save(user);
        return true;
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
        return Optional.empty();
    }




    @Override
    public List<UserDTO> updateUser(User user) {
        if (getById(user.getId()).isPresent()){
            save(toDTO(user));
        }
        return getAll();
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findFirstByName(username);
        if (user == null){
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
