package com.psw.project1.utils.messages;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ProductQuantity {
    private Long productId; //it identifies a product
    private Integer quantity; //it specifies a quantity (number of purchased items) for that product
}//ProductQuantity
