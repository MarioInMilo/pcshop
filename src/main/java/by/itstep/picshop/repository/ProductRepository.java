package by.itstep.picshop.repository;

import by.itstep.picshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM products p WHERE p.quantity <= 0")
    void deleteNullQuantityProduct();

    Product findByTitle(String title);
}
