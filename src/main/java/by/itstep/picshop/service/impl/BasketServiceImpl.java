package by.itstep.picshop.service.impl;

import by.itstep.picshop.dto.BasketDTO;
import by.itstep.picshop.dto.BasketDetailsDTO;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.model.User;
import by.itstep.picshop.repository.BasketRepository;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private BalanceService balanceService;
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BasketService basketService;


    @Autowired
    private MailSender mailSender;


    private final UseMapper mapper = UseMapper.MAPPER;


    @Override
    @Transactional
    public Basket createBasket(User user, List<Long> productsIds) {
        Basket basket = new Basket();
        basket.setUser(user);
        List<Product> productList = getCollectRefProductsById(productsIds);
        basket.setProducts(productList);
        return basketRepository.save(basket);
    }

    private List<Product> getCollectRefProductsById(List<Long> productsIds) {
        return productsIds.stream().map(productRepository::getOne).collect(Collectors.toList());
    }


    @Override
    public BasketDTO getBasketByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getBasket() == null) return new BasketDTO();

        BasketDTO basketDTO = new BasketDTO();
        Map<Long, BasketDetailsDTO> map = new HashMap<>();
        List<Product> products = user.getBasket().getProducts();
        for (Product product : products) {
            BasketDetailsDTO details = map.get(product.getId());
            if (details == null) {
                map.put(product.getId(), new BasketDetailsDTO(product));
            } else {
                details.setAmount(details.getAmount().add(new BigDecimal("1.0")));
                details.setSum(details.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }
        basketDTO.setDetailsDTOS(new ArrayList<>(map.values()));
        basketDTO.aggregate();
        return basketDTO;
    }

    @Override
    public void addProducts(Basket basket, List<Long> productsIds) {
        List<Product> products = basket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsById(productsIds));
        basket.setProducts(newProductList);
        basketRepository.save(basket);
    }


    @Override
    @Transactional
    public void deleteOneProductByTitle(String username, String title) {
        Basket basket = userService.findByName(username).getBasket();
        Long findDeleteId = new ArrayList<>(basket.getProducts()).stream()
                .filter(product -> product.getTitle().equals(title))
                .findFirst()
                .orElseThrow()
                .getId();
        basketRepository.deleteAllById(findDeleteId, basket.getId());
    }

    @Override
    @Transactional
    public void deleteAllProducts(String username) {
        Long basketId = userService.findByName(username)
                .getBasket()
                .getId();
        basketRepository.deleteAllUserBasket(basketId);
    }



    @Override
    @Transactional
    public void deleteFirstProductByTitle(String username, String title) {
        Basket basket = userService.findByName(username).getBasket();
        Long findDeleteId = new ArrayList<>(basket.getProducts()).stream()
                .filter(product -> product.getTitle().equals(title))
                .findFirst()
                .orElseThrow()
                .getId();
        basketRepository.deleteFirstById(findDeleteId, basket.getId());
    }

    @Override
    @Transactional
    public void deleteProductAll(String title) {
        Long productId = productRepository.findByTitle(title).getId();
        basketRepository.deleteOneProductAll(productId);
    }

    @Override
    @Transactional
    public boolean buyProduct(String name, BigDecimal money) {
        if (balanceService.withdrawalOfMoney(name, money)) {
            deleteAllProducts(name);
            productService.weaningAfterPurchase(basketService.getBasketByUser(name));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendMessage(List<BasketDetailsDTO> detailsDTOS, String name) {

        StringBuilder info = new StringBuilder();
        for (BasketDetailsDTO detailsDTO : detailsDTOS) {
            info.append(info)
                    .append(detailsDTO.getTitle())
                    .append(": $")
                    .append(detailsDTO.getPrice())
                    .append("\n");
        }

        String message = String.format(
                """
                        %s
                          Thanks u.
                            """,
                info);

        mailSender.send(userService.findByName(name).getEmail(), "Buy", message);

    }

    @Override
    @Transactional
    public void deleteAllById(Long id) {

    }


    @Override
    public Optional<Basket> getById(Long id) {
        return basketRepository.findById(id);
    }
}
