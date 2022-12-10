package by.itstep.picshop.controller;

import by.itstep.picshop.dto.BasketDTO;
import by.itstep.picshop.dto.BasketDetailsDTO;
import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.model.Product;
import by.itstep.picshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping()
    public String aboutBasket(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("basket", new BasketDTO());
        } else {
            BasketDTO basketDTO = basketService.getBasketByUser(principal.getName());
            model.addAttribute("basket", basketDTO);
        }
        return "basket";
    }

    @PostMapping("/{title}/delete")
    public String deleteProduct(@PathVariable(value = "title") String title, Principal principal) {
        basketService.deleteOneProductByTitle(principal.getName(), title);
        return "redirect:/basket";
    }

    @PostMapping("/{title}/deletes")
    public String deleteOneProduct(@PathVariable(value = "title") String title, Principal principal) {
        basketService.deleteFirstProductByTitle(principal.getName(), title);
        return "redirect:/basket";
    }

    //
    @GetMapping("/{money}/buy")
    public String buyProductsGet(Model model, Principal user,
                                 @PathVariable(value = "money") BigDecimal money) {
        if (money.compareTo(new BigDecimal(0)) <= 0) return "redirect:/basket";
        if (user == null) return "redirect:/basket";
        model.addAttribute("products", basketService.getBasketByUser(user.getName()));
        return "offer";
    }


    @PostMapping("/{money}/buy")
    public String buyProduct(Principal user,
                             @PathVariable(value = "money") BigDecimal money) {
        if (user == null) return "redirect:/basket";
        if (money.compareTo(new BigDecimal(0)) <= 0) return "redirect:/basket";
        List<BasketDetailsDTO> detailsDTOS = basketService.getBasketByUser(user.getName()).getDetailsDTOS();
        if (!(userService.findByName(user.getName()).getBalance().compareTo(money) < 0)) {
            basketService.buyProduct(user.getName(), money);
            basketService.sendMessage(detailsDTOS, user.getName());
        } else {
            return "redirect:/";
        }
        return "redirect:/products";
    }

}
