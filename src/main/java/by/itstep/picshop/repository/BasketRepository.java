package by.itstep.picshop.repository;

import by.itstep.picshop.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM baskets_products b WHERE b.product_id = :products_id AND b.basket_id = :baskets_id")
    void deleteAllById(@Param("products_id") Long products_id, @Param("baskets_id") Long baskets_id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM baskets_products b WHERE b.product_id = :products_id AND b.basket_id = :baskets_id LIMIT 1")
    void deleteFirstById(@Param("products_id") Long products_id, @Param("baskets_id") Long baskets_id);


    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM baskets_products b WHERE b.product_id = :products_id")
    void deleteOneProductAll(@Param("products_id") Long products_id);


    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM baskets_products b WHERE b.basket_id = :basket_id")
    void deleteAllUserBasket(@Param("basket_id") Long basket_id);


}
