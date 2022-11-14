package by.itstep.picshop.service;

import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.User;

import java.util.List;

public interface BasketService {
    Basket createBasket(User user, List<Long> productsIds);

    void addProducts(Basket basket, List<Long> productsIds);
}
