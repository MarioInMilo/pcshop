package by.itstep.picshop.repository;

import by.itstep.picshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);

    User findFirstByEmail(String email);

    User findFirstByRole(String role);

    User findByActivationCode(String code);

}
