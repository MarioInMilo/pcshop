package by.itstep.picshop.service.impl;

import by.itstep.picshop.dto.BasketDTO;
import by.itstep.picshop.dto.BasketDetailsDTO;
import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.mapper.ProductMapper;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.model.ProductImage;
import by.itstep.picshop.model.User;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.BasketService;
import by.itstep.picshop.service.ProductService;
import by.itstep.picshop.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper = ProductMapper.MAPPER;
    private final UseMapper userMapper = UseMapper.MAPPER;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BasketService basketService;


    @Override
    @Transactional
    public List<ProductDTO> getAll() {
        productRepository.deleteNullQuantityProduct();
        return productMapper.fromProductList(productRepository.findAll());
    }

    @Override
    public void addToUserBasket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found " + username);
        }
        Basket basket = user.getBasket();
        if (basket == null) {
            Basket newBasket = basketService.createBasket(user, Collections.singletonList(productId));
            user.setBasket(newBasket);
            userService.save(user);
        } else {
            basketService.addProducts(basket, Collections.singletonList(productId));
        }
    }

    @Override
    @Transactional
    public void saveProduct(Product product, @NotNull MultipartFile fileOne, MultipartFile fileTwo, MultipartFile fileThree) {
        ProductImage image1;
        ProductImage image2;
        ProductImage image3;
        try {
            if (fileOne.getSize() != 0) {
                image1 = toImageEntity(fileOne);
                image1.setPreviewImage(true);
                product.addImageToProduct(image1);
            }
            if (fileTwo.getSize() != 0) {
                image2 = toImageEntity(fileOne);
                product.addImageToProduct(image2);
            }
            if (fileThree.getSize() != 0) {
                image3 = toImageEntity(fileOne);
                product.addImageToProduct(image3);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    @Override
    public Optional<ProductDTO> getById(Long id) {
        return Optional.ofNullable(
                productMapper.fromProduct(
                        productRepository.findById(id)
                                .orElseThrow()));
    }

    @Override
    public void save(ProductDTO productDTO) {
        productRepository.save(productMapper.toProduct(productDTO));
    }

    @Override
    @Transactional
    public boolean createProduct(ProductDTO productDTO) {
        productRepository.save(productMapper.toProduct(productDTO));
        return false;
    }

    @Override
    @Transactional
    public void editProduct(@NotNull ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).orElseThrow();
        boolean isChanged = false;
        if (!Objects.equals(product.getPrice(), productDTO.getPrice())) {
            product.setPrice(productDTO.getPrice());
            isChanged = true;
        }
        if (!Objects.equals(product.getTitle(), productDTO.getTitle())) {
            product.setTitle(productDTO.getTitle());
            isChanged = true;
        }

        if (!Objects.equals(product.getQuantity(), productDTO.getQuantity())) {
            product.setQuantity(productDTO.getQuantity());
            isChanged = true;
        }

        if (!Objects.equals(product.getProductInfo(), productDTO.getProductInfo())) {
            product.setProductInfo(productDTO.getProductInfo());
            isChanged = true;
        }

        if (isChanged) {
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(@NotNull ProductDTO productDTO) {
        basketService.deleteProductAll(productDTO.getTitle());
        productRepository.deleteById(productDTO.getId());
    }

    @Override
    public boolean existProductById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    @Transactional
    public void weaningAfterPurchase(BasketDTO basket) {
        List<BasketDetailsDTO> detailsDTO = basket.getDetailsDTOS();
        List<ProductDTO> productDTO = getAll();
        for (BasketDetailsDTO basketDetailsDTO : detailsDTO) {
            for (ProductDTO dto : productDTO) {
                if (dto.getTitle().equals(basketDetailsDTO.getTitle())) {
                    dto.setQuantity(dto.getQuantity() - basketDetailsDTO.getAmount().intValueExact());
                }
            }
        }
        productRepository.saveAll(productMapper.toProductList(productDTO));

    }


    private ProductImage toImageEntity(@NotNull MultipartFile file) throws IOException {
        return ProductImage.builder()
                .name(file.getName())
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
    }


}
