package com.miz.authenticateservice.repository;

import com.miz.authenticateservice.entity.ProductUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUserMappingRepository extends JpaRepository<ProductUserMapping, Long> {
}
