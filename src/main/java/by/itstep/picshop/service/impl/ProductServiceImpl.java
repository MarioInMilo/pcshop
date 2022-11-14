package by.itstep.picshop.service.impl;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.mapper.ProductMapper;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper = ProductMapper.MAPPER;

    @Autowired
    private ProductRepository repository;


    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(repository.findAll());
    }

    @Override
    public void addToUserBasket(Long productId, String username) {

    }

}
