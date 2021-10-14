package com.ajo.itedo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AjoMemberDto {

    private String firstName;
    private String lastName;
    private String cardNumber;
    private String photograph;
    private String branchName;
    private Double totalSavings;
}
