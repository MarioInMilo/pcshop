package by.itstep.picshop.controller;

import by.itstep.picshop.model.ProductImage;
import by.itstep.picshop.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping({"/image"})
public class ImageController {

    @Autowired
    private ProductImageRepository productImageRepository;

    @GetMapping("/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        ProductImage image = productImageRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}
