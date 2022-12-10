package by.itstep.picshop.controller;

import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.service.BalanceService;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;

@RequestMapping("/balance")
@Controller
public class BalanceController {

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private final UseMapper userMapper = UseMapper.MAPPER;


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    private String balance(Model model, Principal principal) {
        if (principal == null) throw new RuntimeException("U not auth");
        model.addAttribute("balance", userService.findByName(principal.getName()));
        return "wallet";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    private String replenishmentBalance(Principal principal,
                                        @RequestParam BigDecimal balance) {
        balanceService.addMoney(principal.getName(), balance);
        return "redirect:/";
    }


}
