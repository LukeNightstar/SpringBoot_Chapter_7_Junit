package com.springboot.junit.controller;


import com.google.gson.Gson;
import com.springboot.junit.data.dto.ProductDto;
import com.springboot.junit.data.dto.ProductResponseDto;
import com.springboot.junit.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // ProductController에서 잡고 있는 Bean 객체에 대해 Mock 형태의 객체를 생성해줌
    ProductServiceImpl productService;

    @Test // http://localhost:8080/api/v1/product-api/product/{productId}
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception {
        // given : Mock 객체가 특정 상황에서 해야 하는 행위를 정의하는 메소드
        given(productService.getProduct(123L)).willReturn(
                new ProductResponseDto(123L, "pen", 5000, 2000));

        String productId = "123";
        // andExpect : 기대하는 값이 나왔는지 체크해볼 수 있는 메소드
        mockMvc.perform(
                        get("/product?number=" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        // verify : 해당 객체의 메소드가 실행되었는지 체크해줌
        verify(productService).getProduct(123L);
    }

    @Test // http://localhost:8080/api/v1/product-api/product
    @DisplayName("Product 데이터 생성 테스트")
    void createProductTest() throws Exception {
    //Mock 객체에서 특정 메서드가 실행되는 경우 실제 Return을 줄 수 없기 때문에 아래와 같이 가정 사항을 만들어 줌
        given(productService.saveProduct(new ProductDto("pen", 5000, 2000)))
                .willReturn(new ProductResponseDto(12315L, "pen", 5000, 2000));
        ProductDto productDto = ProductDto.builder()
                .name("pen")
                .price(5000)
                .stock(2000)
                .build();
        Gson gson = new Gson(); // 구글에서 개발한 Json 파싱 라이브러리
        String content = gson.toJson(productDto);
        mockMvc.perform(
                        post("/product")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());
        verify(productService).saveProduct(new ProductDto("pen", 5000, 2000));
    }

}
