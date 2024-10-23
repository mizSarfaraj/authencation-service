package com.miz.authenticateservice.service;

import com.miz.authenticateservice.config.filter.CurrentUser;
import com.miz.authenticateservice.entity.ProductList;
import com.miz.authenticateservice.repository.ProductListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MainService {
    private final CurrentUser currentUser;
    private final ProductListRepository productListRepository;

    public MainService(CurrentUser currentUser, ProductListRepository productListRepository) {
        this.currentUser = currentUser;
        this.productListRepository = productListRepository;
    }

    public List<ProductList> getProduct() {
        return productListRepository.findProductListByUserId(currentUser.getUserId());
    }
}
