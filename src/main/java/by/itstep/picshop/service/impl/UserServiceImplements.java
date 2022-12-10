package by.itstep.picshop.service.impl;

import by.itstep.picshop.bean.request.CreateUserRequest;
import by.itstep.picshop.bean.response.CreateUserResponse;
import by.itstep.picshop.config.SecurityConfig;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.MapperUser;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Role;
import by.itstep.picshop.model.User;
import by.itstep.picshop.repository.UserRepository;
import by.itstep.picshop.service.BasketService;
import by.itstep.picshop.service.MailSender;
import by.itstep.picshop.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImplements implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;

    private final UseMapper mapper = UseMapper.MAPPER;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(UserDTO userDTO) {
        if (Objects.equals(userDTO.getPassword(), userDTO.getPassword())) {
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
    public BigDecimal getBalance(UserDTO userDTO) {
        return userDTO.getBalance();
    }

    @Override
    @Transactional
    public Boolean addUser(@NotNull UserDTO userDTO) {
        User user = findByName(userDTO.getUsername());
        if (user != null) return false;
        userDTO.setActivationCode(UUID.randomUUID().toString());
        if (userDTO.getRole() == null) userDTO.setRole(Role.CLIENT);
        userDTO.setBalance(new BigDecimal("0.0"));
        repository.save(mapper.toUser(userDTO));
        if (!StringUtils.isEmpty(userDTO.getEmail())) {
            String message = String.format(
                    """
                            Hello, %s
                            Welcome to PicSHOP.
                            Please follow this link to verify the validity of your account:
                            http://localhost:8080/users/activate/%s
                            If this is not your account - do not click on the link!""",
                    userDTO.getUsername(),
                    userDTO.getActivationCode()

            );

            mailSender.send(userDTO.getEmail(), "PicSHOP verify", message);
        }
        return true;
    }

    @Override
    public Boolean verifyUser(String code) {
        User user = repository.findByActivationCode(code);

        if (user == null) return false;

        user.setVerified(true);
        user.setActivationCode(null);

        repository.save(user);

        String message = String.format(
                """
                        Hello, %s
                        Thanks for confirming the account!
                        """,
                user.getName());
        mailSender.send(user.getEmail(), "PicSHOP verify", message);
        return true;
    }

    @Override
    @Transactional
    public void banUserById(Long id) {
        UserDTO user = getById(id).orElseThrow();
        user.setArchive(true);
        save(user);
    }

    @Override
    @Transactional
    public void unBanUserById(Long id) {
        UserDTO user = getById(id).orElseThrow();
        user.setArchive(false);
        save(user);
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
    public void updateProfile(UserDTO userDTO, String name) {

        User save = repository.findFirstByName(name);
        if (save == null) {
            throw new RuntimeException("User not found " + userDTO.getUsername());
        }
        boolean isChanged = false;

        if (passwordEncoder.matches(userDTO.getPassword(), save.getPassword())
                && Objects.equals(userDTO.getNewPassword(), userDTO.getMatching())) {
            save.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            isChanged = true;
        }
        if (!Objects.equals(userDTO.getEmail(), save.getEmail())) {
            save.setEmail(userDTO.getEmail());
            isChanged = true;
        }

        if (!Objects.equals(userDTO.getUsername(), save.getName())) {
            save.setName(userDTO.getUsername());
            isChanged = true;
        }

        if (!Objects.equals(userDTO.getPhoto(), save.getPhoto())) {
            save.setPhoto(userDTO.getPhoto());
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
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        return null;
    }

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
