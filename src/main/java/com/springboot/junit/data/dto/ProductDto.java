package com.springboot.junit.data.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDto {

    private String name;
    private int price;
    private int stock;

}
