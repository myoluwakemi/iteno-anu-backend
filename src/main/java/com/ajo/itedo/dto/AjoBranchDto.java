package com.ajo.itedo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AjoBranchDto {
    private String branchName;

    private  String address;

    private Double weeklySavings;

    private Double monthlySavings;

    private Double developmentLoan;

    private Double buildingLoan;
}
