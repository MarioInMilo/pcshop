package by.itstep.picshop.service.impl;

import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.model.User;
import by.itstep.picshop.repository.BasketRepository;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Basket createBasket(User user, List<Long> productsIds) {
        Basket basket = new Basket();
        basket.setUser(user);
        List<Product> productList = getCollectRefProductsById(productsIds);
        basket.setProducts(productList);
        return basketRepository.save(basket);
    }

    private List<Product> getCollectRefProductsById(List<Long> productsIds) {
        return productsIds.stream()
                .map(productRepository::getReferenceById)
                .collect(Collectors.toList());
    }

    @Override
    public void addProducts(Basket basket, List<Long> productsIds) {
        List<Product> productList = basket.getProducts();
        List<Product> newProductList = productList == null ? new ArrayList<>() : new ArrayList<>(productList);
        newProductList.addAll(getCollectRefProductsById(productsIds));
        basket.setProducts(newProductList);
        basketRepository.save(basket);


    }
}
