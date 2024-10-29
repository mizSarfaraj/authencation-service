package com.miz.authenticateservice.dto.request.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReqDto {
    List<String> products;
}
