package com.ajo.itedo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanHistoryDto {

    private Double currentPayment;

    private LocalDate dateOfPayment;

}
