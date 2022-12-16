package com.example.demo.contronllers;

import com.example.demo.models.Product;
import com.example.demo.models.ResponseObject;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping(path = "management/products-dashboard")
public class ProductController {
    //DJ = Dependency Injection
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProducts() {
        return repository.findAll(); //Where is date?
    }

    //get detail product
    @GetMapping("/{id}")
    //Let's return an object with: data, massage, status
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Querry product successfully", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Cannot find product with id = " + id, "")
                );
    }

    //Insert new Product with POST method
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        //2 products mustn't have the same name!
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert product successfully", repository.save(newProduct))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updateProduct = repository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    product.setCompany(newProduct.getCompany());
                    return repository.save(product);
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update product successfully", updateProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exits = repository.existsById(id);
        if (exits) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("FAILED", "Cannot find product to delete", "")
        );
    }
}
