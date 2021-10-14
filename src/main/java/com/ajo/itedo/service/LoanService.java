package com.ajo.itedo.service;

import com.ajo.itedo.data.Loan;
import com.ajo.itedo.dto.LoanDto;

import java.util.List;

public interface LoanService {

    String save(LoanDto loanDto) throws Exception;
    Loan findLoanById(Integer id);

    List<Loan> getLoans();
}
