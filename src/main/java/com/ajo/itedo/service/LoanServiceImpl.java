package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.data.Loan;
import com.ajo.itedo.dto.LoanDto;
import com.ajo.itedo.repository.AjoMemberRepo;
import com.ajo.itedo.repository.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class LoanServiceImpl  implements LoanService{
    Logger logger = Logger.getLogger(LoanServiceImpl.class.getName());
    @Autowired
    LoanRepo loanRepo;

    @Autowired
    AjoMemberRepo ajoMemberRepo;

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
                        "first Guarantor total savings of: "+firstGuarantorTotalSavings+ "And your second Guarantor total savings of: "+secondGuarantorTotalSavings;
            }
            else {
                if (borrower.getCollectedLoan()){
                    return "Borrower's with card number "+ borrower.getCardNumber()+ " have a pending loan";
                }
                else if (firstGuarantor.getIsGuarantor()){
                    return "Borrower's first Guarantor  stood for a pending loan";
                }
                else if (secondGuarantor.getIsGuarantor()){
                    return "Borrower's second Guarantor  stood for a pending loan";
                }
                else {
                    Loan loan = new Loan();
                    loan.setBorrower(borrower);
                    loan.setFirstGuarantor(firstGuarantor);
                    loan.setSecondGuarantor(secondGuarantor);
                    loan.setLoanAmount(loanDto.getLoanAmount());
                    loan.setWeeklyRepayment(loanDto.getWeeklyRepayment());
                    loan.setDueDate(loanDto.getDueDate());
                    borrower.setCollectedLoan(Boolean.TRUE);
                    ajoMemberRepo.save(borrower);
                    firstGuarantor.setIsGuarantor(Boolean.TRUE);
                    ajoMemberRepo.save(firstGuarantor);
                    secondGuarantor.setIsGuarantor(Boolean.TRUE);
                    ajoMemberRepo.save(secondGuarantor);
                    loanRepo.save(loan);
                    return "Loan of :" + loanDto.getLoanAmount() + "was given to member with card number " + borrower.getCardNumber() + " successfully!";
                }
            }
        }
    }
}
