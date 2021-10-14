package com.ajo.itedo.controller;

import com.ajo.itedo.data.Loan;
import com.ajo.itedo.dto.LoanDto;
import com.ajo.itedo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajo")
public class LoanController {

    @Autowired
    LoanService loanService;

    @PostMapping("/give-loan")
    public ResponseEntity<String> GiveLoan(@RequestBody LoanDto loanDto) throws Exception {
       return ResponseEntity.ok(loanService.save(loanDto));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<?> getLoanById(@PathVariable Integer loanId){
        if (loanService.findLoanById(loanId).getId() != null ) {
            return ResponseEntity.ok(loanService.findLoanById(loanId));
        }
        else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/loans")
    public List<Loan> getLoans(){
        return loanService.getLoans();
    }
}
