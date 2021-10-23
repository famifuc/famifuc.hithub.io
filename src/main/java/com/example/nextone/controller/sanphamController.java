package com.example.nextone.controller;


import com.example.nextone.exception.ResourceNotFoundException;
import com.example.nextone.model.*;
import com.example.nextone.rep.sanphamrep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/")
public class sanphamController {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    @Autowired
    private sanphamrep sanphamrepo;
    @Value("${upload.path}")
    private String fileUpload;
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String name) {
        try {
            List<Product> product = new ArrayList<Product>();

            if (name == null)
                sanphamrepo.findAll().forEach(product::add);
            else
                sanphamrepo.findByNameContaining(name).forEach(product::add);

            if (product.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get by id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable("id") Long id){
        Product product= sanphamrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
        
        return ResponseEntity.ok(product);
    }

 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestParam String name,
                       @RequestParam Integer price,
                       @RequestParam MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setImage(imagePath.resolve(image.getOriginalFilename()).toString());
        return sanphamrepo.save(p);
    }
    {/*
    @PostMapping("/products")
    public Product create(@RequestBody Product product) {
        return sanphamrepo.save(product);
    }
    */}
    @PutMapping("/products/{id}")
    public ResponseEntity<Product>  update(@PathVariable Long id, @RequestBody Product ProductDetails){
        Product product = sanphamrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        product.setName(ProductDetails.getName());
        product.setPrice(ProductDetails.getPrice());
        product.setImage(ProductDetails.getImage());

        Product updatedProduct = sanphamrepo.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
	

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Long id){
        Product product = sanphamrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        sanphamrepo.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    
}
