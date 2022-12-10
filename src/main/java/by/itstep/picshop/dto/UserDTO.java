package by.itstep.picshop.dto;

import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private Byte photo;
    private String username;
    private String password;
    private String newPassword;
    private String matching;
    private String email;
    private Basket basket;
    private Boolean archive;
    private BigDecimal balance;
    private Role role;
    private String activationCode;
    private Boolean verified;

}
