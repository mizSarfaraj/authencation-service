package com.miz.authenticateservice.service;

import com.miz.authenticateservice.config.filter.CurrentUser;
import com.miz.authenticateservice.dto.request.product.ProductReqDto;
import com.miz.authenticateservice.entity.ProductList;
import com.miz.authenticateservice.repository.ProductListRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Transactional
    public void saveProduct(ProductReqDto productReqDto) {
        List<ProductList> productLists = new ArrayList<>();
        for (String product : productReqDto.getProducts()) {
            ProductList entity = new ProductList();
            entity.setProduct(product);
            entity.setCreateAt(new Date());
            productLists.add(entity);
        }
        productListRepository.saveAll(productLists);
    }
}
