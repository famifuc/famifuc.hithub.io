package com.example.nextone.rep;

import com.example.nextone.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface sanphamrep extends JpaRepository<Product,Long> {

    List<Product> findByNameContaining(String name);



}
