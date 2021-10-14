package com.ajo.itedo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanDto {

    private String borrowerCardNumber;
    private String firstGuarantorCardNumber;
    private String secondGuarantorCardNumber;
    private Double loanAmount;
    private Double weeklyRepayment;
    private LocalDate dueDate;

}
