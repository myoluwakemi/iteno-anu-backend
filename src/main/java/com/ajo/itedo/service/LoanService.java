package com.ajo.itedo.service;

import com.ajo.itedo.data.Loan;
import com.ajo.itedo.data.LoanHistory;
import com.ajo.itedo.dto.LoanDto;
import com.ajo.itedo.dto.LoanHistoryDto;

import java.util.List;

public interface LoanService {

    String save(LoanDto loanDto) throws Exception;
    Loan findLoanById(Integer id);

    List<Loan> getLoans();

    Object repayment(Integer loadId,LoanHistoryDto loanHistoryDto);

    List<LoanHistory> loanHistory(Integer loanId);

    String deleteLoan(Integer loanId);
}
