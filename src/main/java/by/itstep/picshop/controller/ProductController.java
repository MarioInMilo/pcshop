package by.itstep.picshop.controller;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public String list(Model model){
        List<ProductDTO> productDTOS = service.getAll();
        model.addAttribute("products", productDTOS);
        return "products";
    }
}
