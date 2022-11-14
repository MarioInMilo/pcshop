package by.itstep.picshop.mapper;

import by.itstep.picshop.config.SecurityConfig;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring", imports = {BCryptPasswordEncoder.class, SecurityConfig.class})
public interface UseMapper {
    UseMapper MAPPER = Mappers.getMapper(UseMapper.class);
    @Mapping(target = "password", expression = "java(SecurityConfig.passwordEncoder().encode(userDTO.getPassword()))")
    @Mapping(target = "name", source = "username")
//    @Mapping(target = "password", expression = "java(new BCryptPasswordEncoder().encode(userDTO.getPassword()))")
    User toUser(UserDTO userDTO);

    @InheritInverseConfiguration
    @Mapping(target = "matching", ignore = true)
//    @Mapping(target = "password", expression = "java(new BCryptPasswordEncoder().encode(user.getPassword()))")
    // securityConfig.passwordEncoder().encode(userDTO.getPassword())
    @Mapping(target = "username", source = "name")
    @Mapping(target = "password", expression = "java(SecurityConfig.passwordEncoder().encode(user.getPassword()))")
    UserDTO fromUser(User user);

    List<User> toUserList(List<UserDTO> userDTOS);

    List<UserDTO> fromUserList(List<User> users);
}
