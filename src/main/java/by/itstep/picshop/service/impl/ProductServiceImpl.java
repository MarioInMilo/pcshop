package by.itstep.picshop.service.impl;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.repository.ProductRepository;
import by.itstep.picshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<ProductDTO> getAll() {
        return null;
    }

//    @Override
//    public List<ProductDTO> getAll() {
//        return productMapper.fromProductList(repository.findAll());
//    }
}
