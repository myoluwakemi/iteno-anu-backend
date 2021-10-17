package com.ajo.itedo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AjoMemberResponseDto {
    private Integer id;
    private String cardNumber;
    private Double totalSavings;
    private Integer branchId;
    private String branchName;
    private Boolean isGuarantor;
    private Boolean isBorrower;

}
