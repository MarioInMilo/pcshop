package by.itstep.picshop.mapper;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.model.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
        ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

        Product toProduct(ProductDTO productDTO);

        @InheritInverseConfiguration
        ProductDTO fromProduct(Product product);

        List<Product> toProductList(List<ProductDTO> productDTOS);

        List<ProductDTO> fromProductList(List<Product> products);


}
