package com.miz.authenticateservice.controller;

import com.miz.authenticateservice.constant.SuccessCode;
import com.miz.authenticateservice.dto.request.product.ProductReqDto;
import com.miz.authenticateservice.dto.response.BaseResponse;
import com.miz.authenticateservice.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/product")
    public ResponseEntity<BaseResponse<Object>> getProduct() {
        return new ResponseEntity<>(new BaseResponse<>(mainService.getProduct(), SuccessCode.DATA_FETCH_SUCCESSFULLY), HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<BaseResponse<Void>> saveProduct(@RequestBody ProductReqDto productReqDto) {
        mainService.saveProduct(productReqDto);
        return new ResponseEntity<>(new BaseResponse<Void>(null, SuccessCode.DATA_SAVED_SUCCESSFULLY), HttpStatus.OK);
    }


}
