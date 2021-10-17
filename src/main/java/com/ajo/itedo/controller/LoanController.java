package com.ajo.itedo.controller;

import com.ajo.itedo.data.Loan;
import com.ajo.itedo.data.LoanHistory;
import com.ajo.itedo.dto.LoanDto;
import com.ajo.itedo.dto.LoanHistoryDto;
import com.ajo.itedo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ajo")
public class LoanController {

    @Autowired
    LoanService loanService;

    @PostMapping("/give-loan")
    public ResponseEntity<?> GiveLoan(@RequestBody LoanDto loanDto) throws Exception {
        Map<String,String> response = new HashMap<>();
        if (loanService.save(loanDto).endsWith("successfully")){
            response.put("message","Loan of "+ loanDto.getLoanAmount()+
                    " was awarded to member with card number "+ loanDto.getBorrowerCardNumber()+ " successfully");
            return new ResponseEntity<>(response,HttpStatus.CREATED);

        }
        else {
            response.put("message",loanService.save(loanDto));
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

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
    @DeleteMapping("/loan/{loanId}")
    public ResponseEntity<?> deleteLoan(@PathVariable Integer loanId){
        Map<String,String> response = new HashMap<>();
        if (loanService.deleteLoan(loanId).endsWith("successfully")){
            response.put("messages","Loan with Id of "+loanId+ " deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            response.put("message",loanService.deleteLoan(loanId));
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/loans")
    public List<Loan> getLoans(){
        return loanService.getLoans();
    }

    @PostMapping("/loan/{loanId}/repayment")
    public ResponseEntity<?> repayment(@PathVariable Integer loanId, @RequestBody LoanHistoryDto loanHistoryDto){
        Map<String,String> response = new HashMap<>();
        if (loanService.repayment(loanId,loanHistoryDto).toString().endsWith("found")){
            response.put("message","Loan with id "+loanId+ " Not Found");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        else if (loanService.repayment(loanId,loanHistoryDto).toString().endsWith("repayment")){
            response.put("message","Congrats you are done with your loan repayment");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            return ResponseEntity.ok(loanService.repayment(loanId, loanHistoryDto));
        }
    }

    @GetMapping("/loan/{loanId}/history")
    public List<LoanHistory> loanHistory(@PathVariable Integer loanId){
        return loanService.loanHistory(loanId);
    }
}
