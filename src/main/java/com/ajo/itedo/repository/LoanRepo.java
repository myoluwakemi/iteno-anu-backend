package com.ajo.itedo.repository;

import com.ajo.itedo.data.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {

    List<Loan> findLoanByBorrower_Id(Integer id);

}
