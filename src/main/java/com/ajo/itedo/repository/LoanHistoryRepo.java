package com.ajo.itedo.repository;

import com.ajo.itedo.data.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanHistoryRepo extends JpaRepository<LoanHistory ,Integer> {
    List<LoanHistory> findLoanHistoryByLoan_Id(Integer loadId);
}
