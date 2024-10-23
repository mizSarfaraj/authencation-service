package com.miz.authenticateservice.controller;

import com.miz.authenticateservice.constant.SuccessCode;
import com.miz.authenticateservice.dto.response.BaseResponse;
import com.miz.authenticateservice.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/product")
    public ResponseEntity<BaseResponse<Object>> getProduct() {
        return new ResponseEntity<>(new BaseResponse<>(mainService.getProduct(), SuccessCode.DATA_SAVED_SUCCESSFULLY), HttpStatus.OK);
    }
}
