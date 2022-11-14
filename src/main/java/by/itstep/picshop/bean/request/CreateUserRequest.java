package by.itstep.picshop.bean.request;

import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String name;
    private String password;
    private String matching;
    private String email;
    private Basket basket;
    private Boolean archive;
    private Role role;
}
