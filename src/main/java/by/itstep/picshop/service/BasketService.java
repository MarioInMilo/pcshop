package by.itstep.picshop.service;

import by.itstep.picshop.dto.BasketDTO;
import by.itstep.picshop.dto.BasketDetailsDTO;
import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BasketService {
    Basket createBasket(User user, List<Long> productsIds);

    void addProducts(Basket basket, List<Long> productsIds);

    BasketDTO getBasketByUser(String name);

    Optional<Basket> getById(Long id);

    void deleteOneProductByTitle(String username, String title);

    void deleteAllProducts(String username);

    void deleteFirstProductByTitle(String username, String title);

    void deleteProductAll(String title);

    boolean buyProduct(String name, BigDecimal money);

    void sendMessage(List<BasketDetailsDTO> detailsDTOS, String name);

    void deleteAllById(Long id);
}
