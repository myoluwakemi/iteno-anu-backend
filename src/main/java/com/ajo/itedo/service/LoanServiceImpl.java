package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.data.LOANSTATUS;
import com.ajo.itedo.data.Loan;
import com.ajo.itedo.data.LoanHistory;
import com.ajo.itedo.dto.LoanDto;
import com.ajo.itedo.dto.LoanHistoryDto;
import com.ajo.itedo.repository.AjoMemberRepo;
import com.ajo.itedo.repository.LoanHistoryRepo;
import com.ajo.itedo.repository.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class LoanServiceImpl  implements LoanService{
    Logger logger = Logger.getLogger(LoanServiceImpl.class.getName());
    @Autowired
    LoanRepo loanRepo;

    @Autowired
    AjoMemberRepo ajoMemberRepo;

    @Autowired
    LoanHistoryRepo loanHistoryRepo;

    @Override
    public String save(LoanDto loanDto) throws Exception {

        try {
            if (ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getBorrowerCardNumber()).isPresent()){
                AjoMember borrower = ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getBorrowerCardNumber()).get();
                if (ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getFirstGuarantorCardNumber()).isPresent()){
                    AjoMember firstGuarantor = ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getFirstGuarantorCardNumber()).get();

                    if (ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getSecondGuarantorCardNumber()).isPresent()){
                        AjoMember secondGuarantor = ajoMemberRepo.findAjoMemberByCardNumber(loanDto.getSecondGuarantorCardNumber()).get();

                        return giveLoan(borrower,firstGuarantor,secondGuarantor, loanDto);
                    }
                    else {
                        return "Second Guarantor with card number "+ loanDto.getSecondGuarantorCardNumber()+ " does not exist";
                    }
                }
                else {
                    return "First Guarantor with card number "+ loanDto.getFirstGuarantorCardNumber()+ " does not exist";
                }
            }
            else {
                return "Member with card number "+ loanDto.getBorrowerCardNumber()+ " does not exist";
            }

        }
        catch (Exception e){
           logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public Loan findLoanById(Integer id) {
        if (loanRepo.findById(id).isPresent()){
            return loanRepo.findById(id).get();
        }
        return null;
    }

    @Override
    public List<Loan> getLoans() {
        return loanRepo.findAll();
    }

    @Override
    public Object repayment(Integer loadId, LoanHistoryDto loanHistoryDto) {
        if (loanRepo.findById(loadId).isPresent()){
            Loan loan = loanRepo.findById(loadId).get();
            if (loan.getAmountPaid() >= loan.getLoanAmount()){
                loan.setLoanStatus(LOANSTATUS.COMPLETED);
                updateMemberStatus(loadId);
                loanRepo.save(loan);
                return "Done with your loan repayment";
            }
            else {
                loan.setAmountPaid(loan.getAmountPaid()+loanHistoryDto.getCurrentPayment());
                loanRepo.save(loan);
                LoanHistory loanHistory = new LoanHistory();
                loanHistory.setDateOfPayment(LocalDate.now());
                loanHistory.setLoan(loan);
                loanHistory.setBalance(loan.getLoanAmount()-loan.getAmountPaid());
                loanHistory.setCurrentPayment(loanHistoryDto.getCurrentPayment());
                loanHistoryRepo.save(loanHistory);
                return loanHistory;
            }
        }
        else {
            return "Loan not found";
        }


    }

    @Override
    public List<LoanHistory> loanHistory(Integer loanId) {
        return loanHistoryRepo.findLoanHistoryByLoan_Id(loanId);
    }

    @Override
    public String deleteLoan(Integer loanId) {
        if (loanRepo.findById(loanId).isPresent()){
            updateMemberStatus(loanId);
            loanRepo.deleteById(loanId);
            return "Loan deleted successfully";
        }
        else {
            return "Loan not found";
        }

    }
    private void updateMemberStatus(Integer loanId){
        AjoMember borrower = ajoMemberRepo.findAjoMemberByCardNumber(loanRepo.findById(loanId).get().getBorrower().getCardNumber()).get();
        AjoMember firstGuarantor = ajoMemberRepo.findAjoMemberByCardNumber(loanRepo.findById(loanId).get().getFirstGuarantor().getCardNumber()).get();
        AjoMember secondGuarantor = ajoMemberRepo.findAjoMemberByCardNumber(loanRepo.findById(loanId).get().getSecondGuarantor().getCardNumber()).get();
        borrower.setIsBorrower(Boolean.FALSE);
        ajoMemberRepo.save(borrower);
        firstGuarantor.setIsGuarantor(Boolean.FALSE);
        ajoMemberRepo.save(firstGuarantor);
        secondGuarantor.setIsGuarantor(Boolean.FALSE);
        ajoMemberRepo.save(secondGuarantor);
    }
    private String giveLoan(AjoMember borrower, AjoMember firstGuarantor, AjoMember secondGuarantor, LoanDto loanDto){
        Double borrowerTotalSavings = borrower.getTotalSavings();
        Double firstGuarantorTotalSavings = firstGuarantor.getTotalSavings();
        Double secondGuarantorTotalSavings = secondGuarantor.getTotalSavings();

        if (loanDto.getLoanAmount() > borrowerTotalSavings * 3){
            return "Your total savings multiply by 3: "+borrowerTotalSavings * 3 + " is less than " + loanDto.getLoanAmount();
        }
        else {
            if (borrowerTotalSavings + firstGuarantorTotalSavings + secondGuarantorTotalSavings < loanDto.getLoanAmount()){
                return "The amount you want to borrow is higher than your total savings of: "+borrowerTotalSavings+
                        " first Guarantor total savings of: "+firstGuarantorTotalSavings+ " And your second Guarantor total savings of: "+secondGuarantorTotalSavings;
            }
            else {
                if (firstGuarantor.getIsGuarantor()){
                    return "Borrower's first Guarantor  stood for a pending loan";
                }
                else if (secondGuarantor.getIsGuarantor()){
                    return "Borrower's second Guarantor  stood for a pending loan";
                }
                else{
                    if (!borrower.getIsBorrower()){
                        Loan loan = new Loan();
                        loan.setBorrower(borrower);
                        loan.setFirstGuarantor(firstGuarantor);
                        loan.setSecondGuarantor(secondGuarantor);
                        loan.setLoanAmount(loanDto.getLoanAmount());
                        loan.setWeeklyRepayment(loanDto.getWeeklyRepayment());
                        loan.setDueDate(loanDto.getDueDate());
                        borrower.setIsBorrower(Boolean.TRUE);
                        ajoMemberRepo.save(borrower);
                        firstGuarantor.setIsGuarantor(Boolean.TRUE);
                        ajoMemberRepo.save(firstGuarantor);
                        secondGuarantor.setIsGuarantor(Boolean.TRUE);
                        ajoMemberRepo.save(secondGuarantor);
                        loanRepo.save(loan);
                        return "Loan of :" + loanDto.getLoanAmount() + "was given to member with card number " + borrower.getCardNumber() + " successfully";

                    }
                    else {
                        return "Borrower's with card number "+ borrower.getCardNumber()+ " have a pending loan";
                    }

                }
            }

        }
    }
}
