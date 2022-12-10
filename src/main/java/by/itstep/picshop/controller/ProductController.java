package by.itstep.picshop.controller;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.dto.UserDTO;
import by.itstep.picshop.mapper.ProductMapper;
import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository repository;

    private final ProductMapper mapper = ProductMapper.MAPPER;

    @GetMapping
    public String list(Model model) {
        List<ProductDTO> productDTOS = productService.getAll();
        model.addAttribute("products", productDTOS);
        return "products";
    }

    @GetMapping("/{id}/basket")
    public String addBasket(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        productService.addToUserBasket(id, principal.getName());
        return "redirect:/products";
    }


    @GetMapping("/{id}/info")
    public String productInfo(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.getById(id).orElseThrow();
        model.addAttribute("products", productDTO);
        return "product-info";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @GetMapping("/create")
    public String addProduct(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "add-prod";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @PostMapping("/create")
    public String createProduct(Product product, Model model) {
        if (productService.createProduct(mapper.fromProduct(product))) {
            return "redirect:/products";
        } else {
            model.addAttribute("product", product);
        }
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @PostMapping("/{id}/delete")
    public String removeProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(productService.getById(id).orElseThrow());
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable(name = "id") Long id, Model model) {
        if (!productService.existProductById(id)) return "redirect:/products";
        Optional<ProductDTO> productDTO = productService.getById(id);
        model.addAttribute("product", productDTO.stream().collect(Collectors.toList()));
        return "edit-product";
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DIRECTOR', 'MANAGER')")
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable(name = "id") Long id,
                                @RequestParam String title,
                                @RequestParam Double price,
                                @RequestParam Integer quantity,
                                @RequestParam String productInfo) {
        ProductDTO productDTO = productService.getById(id).orElseThrow();
        productDTO.setTitle(title);
        productDTO.setQuantity(quantity);
        productDTO.setProductInfo(productInfo);
        productDTO.setPrice(BigDecimal.valueOf(price));
        productService.editProduct(productDTO);
        return "redirect:/products";
    }


}
