package by.itstep.picshop.mapper;

import by.itstep.picshop.dto.ProductDTO;
import by.itstep.picshop.model.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
        ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

        Product toProduct(ProductDTO productDTO);

        @InheritInverseConfiguration
        ProductDTO fromProduct(Product product);

        List<Product> toProductList(List<ProductDTO> productDTOS);

        List<ProductDTO> fromProductList(List<Product> products);


//        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//        ProductDTO toDTO(Product product);
//        Product DTOto(ProductDTO productDTO);
//        //    public static ProductDTO toProductDTO(Product product){
////        return ProductDTO.builder()
////                .price(product.getPrice())
////                .title(product.getTitle())
////                .categories(product.getCategories())
////                .build();
////    }
////
////    public static Product fromProductDTO (ProductDTO productDTO){
////        return Product.builder()
////                .categories(productDTO.getCategories())
////                .price(productDTO.getPrice())
////                .title(productDTO.getTitle())
////                .build();
////    }

}
