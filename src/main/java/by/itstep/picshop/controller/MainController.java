package by.itstep.picshop.controller;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping({"", "/", "/main"})
public class MainController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String index(Model model) {
        List<ProductDTO> productDTOS = productService.getAll();
        model.addAttribute("products", productDTOS.size() == 0 ? new ProductDTO() : productDTOS.get(0));
        return "index";
    }

    @GetMapping("/{id}/basket")
    public String addBasket(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        productService.addToUserBasket(id, principal.getName());
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("login-error")
    public String LoginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
