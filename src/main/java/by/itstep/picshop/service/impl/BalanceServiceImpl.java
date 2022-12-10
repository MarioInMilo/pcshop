package by.itstep.picshop.service.impl;

import by.itstep.picshop.mapper.UseMapper;
import by.itstep.picshop.model.User;
import by.itstep.picshop.service.BalanceService;
import by.itstep.picshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private UserService userService;

    private final UseMapper userMapper = UseMapper.MAPPER;

    @Override
    @Transactional
    public void addMoney(String name, BigDecimal money) {
        User user = userService.findByName(name);
        user.setBalance(user.getBalance().add(money));
        userService.save(user);
    }

    @Override
    @Transactional
    public boolean withdrawalOfMoney(String name, BigDecimal money) {
        User user = userService.findByName(name);
        if (user.getBalance().compareTo(money) < 0) {
            return false;
        } else {
            user.setBalance(user.getBalance().subtract(money));
        }
        userService.save(user);
        return true;
    }

    @Override
    public BigDecimal getBalanceByName(String name) {
        return userService.findByName(name)
                .getBalance();
    }

    @Override
    public BigDecimal getBalanceById(Long id) {
        return userService.getById(id)
                .orElseThrow()
                .getBalance();
    }

    @Override
    @Transactional
    public void setBalanceByName(String name, BigDecimal money) {
        User user = userService.findByName(name);
        user.setBalance(money);
        userService.save(user);
    }

    @Override
    @Transactional
    public void setBalanceById(Long id, BigDecimal money) {
        User user = userMapper.toUser(userService.getById(id).orElseThrow());
        user.setBalance(money);
        userService.save(user);
    }
}
