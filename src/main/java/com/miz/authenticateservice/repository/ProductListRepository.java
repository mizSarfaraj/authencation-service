package com.miz.authenticateservice.repository;

import com.miz.authenticateservice.entity.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductListRepository extends JpaRepository<ProductList, Long> {
    @Query("SELECT p FROM ProductList p JOIN ProductUserMapping pm ON pm.productId = p.productId WHERE pm.userId = :userId")
    List<ProductList> findProductListByUserId(@Param("userId") Long userId);
}
