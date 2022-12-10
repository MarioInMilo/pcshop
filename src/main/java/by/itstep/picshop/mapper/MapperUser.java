package by.itstep.picshop.mapper;

import by.itstep.picshop.config.SecurityConfig;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.model.User;

import java.util.Arrays;

public class MapperUser {
    public static UserDTO fromUser(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.username( user.getName() );
        userDTO.id( user.getId() );
        userDTO.photo( user.getPhoto() );
        userDTO.email( user.getEmail() );
        userDTO.basket( user.getBasket() );
        userDTO.archive( user.getArchive() );
        userDTO.role( user.getRole() );

        userDTO.password(user.getPassword().trim());

        return userDTO.build();
    }
    public static User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( userDTO.getUsername() );
        user.id( userDTO.getId() );
        user.photo( userDTO.getPhoto() );
        user.email( userDTO.getEmail() );
        user.archive( userDTO.getArchive() );
        user.role( userDTO.getRole() );
        user.basket( userDTO.getBasket() );
        user.password(userDTO.getPassword().trim());

        return user.build();
    }

}
