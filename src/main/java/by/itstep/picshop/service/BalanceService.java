package by.itstep.picshop.service;

import java.math.BigDecimal;

public interface BalanceService {

    void addMoney(String name, BigDecimal money);

    boolean withdrawalOfMoney(String name, BigDecimal money);

    BigDecimal getBalanceByName(String name);

    BigDecimal getBalanceById(Long id);

    void setBalanceByName(String name, BigDecimal money);

    void setBalanceById(Long id, BigDecimal money);



}
