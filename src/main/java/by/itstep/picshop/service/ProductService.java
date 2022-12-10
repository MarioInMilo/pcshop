package by.itstep.picshop.service;

import by.itstep.picshop.dto.BasketDTO;
import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.model.Basket;
import by.itstep.picshop.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> getAll();

    void addToUserBasket(Long productId, String username);

    void saveProduct(Product product, MultipartFile fileOne, MultipartFile fileTwo, MultipartFile fileThree) throws IOException;

    Optional<ProductDTO> getById(Long id);

    void save(ProductDTO productDTO);

    boolean createProduct(ProductDTO productDTO);

    void editProduct(ProductDTO productDTO);

    void deleteProduct(ProductDTO productDTO);

    boolean existProductById(Long id);

    void weaningAfterPurchase(BasketDTO basket);


}
