package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.data.Loan;
import com.ajo.itedo.dto.AjoBranchDto;
import com.ajo.itedo.repository.AjoBranchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AjoBranchServiceImpl implements AjoBranchService{

    @Autowired
    AjoBranchRepo ajoBranchRepo;

    @Autowired
    LoanService loanService;

    @Autowired
    AjoMemberService ajoMemberService;


    @Override
    public String save(AjoBranchDto ajoBranchDto) {
        if (ajoBranchRepo.existsAjoBranchByBranchName(ajoBranchDto.getBranchName().toLowerCase())){
            return  ajoBranchDto.getBranchName()+" Already exist";
        }
        else {
            AjoBranch ajoBranch = new AjoBranch();
            ajoBranch.setBranchName(ajoBranchDto.getBranchName());
            ajoBranch.setAddress(ajoBranchDto.getAddress());
            if (ajoBranchDto.getWeeklySavings() != null){
                ajoBranch.setWeeklySavings(ajoBranchDto.getWeeklySavings());
            }
            if (ajoBranchDto.getMonthlySavings() != null){
                ajoBranch.setMonthlySavings(ajoBranchDto.getMonthlySavings());
            }
            if (ajoBranchDto.getDevelopmentLoan() != null){
                ajoBranch.setDevelopmentLoan(ajoBranchDto.getDevelopmentLoan());
            }
            if (ajoBranchDto.getBuildingLoan() != null){
                ajoBranch.setBuildingLoan(ajoBranchDto.getBuildingLoan());
            }
            ajoBranchRepo.save(ajoBranch);
            return ajoBranchDto.getBranchName() + " was saved successfully";
        }
    }

    @Override
    public Map<String, Object> findAjoBranchByBranchName(String branchName) {
        Map<String, Object> branchResponse = new HashMap<>();
        LocalDateTime currentWeek = LocalDateTime.now().minusDays(7);
        Optional<AjoBranch> ajoBranch =ajoBranchRepo.findAjoBranchByBranchName(branchName);
        List<AjoMember> allMembers = ajoMemberService.getAllMembers();
        List<Loan> allLoans = loanService.getLoans();
        if (ajoBranch.isPresent()){
            branchResponse.put("branchName", ajoBranch.get().getBranchName());
            branchResponse.put("totalMembers", allMembers.stream().filter(member -> member.getAjoBranch() == ajoBranch.get()).count());

            branchResponse.put("totalMembersAsGuarantor",allMembers.stream().
                    filter(member -> member.getIsGuarantor() && member.getAjoBranch() == ajoBranch.get()).count());

            branchResponse.put("totalCurrentSavings",allMembers.stream().filter(member -> member.getAjoBranch() == ajoBranch.get())
                    .filter(member -> currentWeek.isBefore(member.getCreated()))
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getCurrentSavings(),Double::sum));

            branchResponse.put("totalLoanDisbursed", allLoans.stream().filter(loan -> loan.getBorrower().getAjoBranch() == ajoBranch.get())
                    .reduce(0.0,(partialTotal, loan)-> partialTotal + loan.getLoanAmount(),Double::sum));

            branchResponse.put("totalLoanPending",allLoans.stream().filter(loan -> loan.getBorrower().getAjoBranch() == ajoBranch.get())
                    .reduce(0.0,(partialTotal,loan) -> partialTotal + loan.getLoanBalance(),Double::sum));
            branchResponse.put("totalBuildingLoan", ajoBranch.get().getBuildingLoan());
            branchResponse.put("developmentLoan",ajoBranch.get().getDevelopmentLoan());
            branchResponse.put("newMembers",allMembers.stream().filter(member -> member.getAjoBranch() == ajoBranch.get())
                    .filter(member -> currentWeek.isBefore(member.getCreated())).count());
            return branchResponse;
        }

        return null;
    }

    @Override
    public List<AjoBranch> getAllAjoBranches() {
        return ajoBranchRepo.findAll();
    }

    @Override
    public AjoBranch updateBranch(Integer branchId,AjoBranchDto ajoBranchDto) {
        Optional<AjoBranch> ajoBranch = ajoBranchRepo.findById(branchId);
        if (ajoBranch.isPresent()){
            if (ajoBranchDto.getBuildingLoan() != null){
                ajoBranch.get().setBuildingLoan(ajoBranch.get().getBuildingLoan()+ ajoBranchDto.getBuildingLoan());
            }
            if (ajoBranchDto.getDevelopmentLoan() != null){
                ajoBranch.get().setDevelopmentLoan(ajoBranch.get().getDevelopmentLoan()+ ajoBranchDto.getDevelopmentLoan());
            }
            if (ajoBranchDto.getWeeklySavings() != null){
                ajoBranch.get().setWeeklySavings(ajoBranch.get().getWeeklySavings()+ajoBranchDto.getWeeklySavings());
            }
            if (ajoBranchDto.getMonthlySavings() != null){
                ajoBranch.get().setMonthlySavings(ajoBranch.get().getMonthlySavings()+ ajoBranchDto.getMonthlySavings());
            }
            ajoBranchRepo.save(ajoBranch.get());
            return ajoBranch.get();
        }
        else {
            return null;
        }
    }

    @Override
    public String deleteBranch(Integer branchId) {
        if (ajoBranchRepo.findById(branchId).isPresent()){
            ajoBranchRepo.deleteById(branchId);
            return "Branch delete successfully";
        }
        else {
            return "Branch does not exist";
        }

    }



    @Override
    public Map<String, Object> getDashboardStat() {
        List<AjoMember> members = ajoMemberService.getAllMembers();
        List<Loan> loans = loanService.getLoans();
        Map<String,Object> stats = new HashMap<>();
        double totalSavings = members.stream().reduce(0.0,(partialTotalSavings, user) -> partialTotalSavings + user.getTotalSavings(), Double::sum);
        double loansDisbursed = loans.stream().reduce(0.0,(partialTotalLoans,loan) -> partialTotalLoans + loan.getLoanAmount(),Double::sum);
        double loanBalance = members.stream().reduce(0.0,(partialLoanBalance, member) -> partialLoanBalance + member.getCurrentLoanBalance(),Double::sum);

        stats.put("totalSavings",totalSavings);
        stats.put("loanDisbursed",loansDisbursed);
        stats.put("loanBalance",loanBalance);
        stats.put("members",members.size());
        return stats;
    }

    @Override
    public Map<String, Object> getMembersByWeekly(Integer month, Integer year) throws DateTimeException {
        Map<String, Object> monthlyMembers = new HashMap<>();
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate date = LocalDate.parse(year +"-"+month+"-"+1,df);
            List<AjoMember> getAllMembers = ajoMemberService.getAllMembers();

            Double firstWeekSavings  =  getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                            == date.getMonth() && member.getCreated().getYear() == date.getYear() && member.getCreated().getDayOfMonth() < 8)
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getTotalSavings(),Double::sum);

            Integer firstWeekMembers = (int) getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                    == date.getMonth() && member.getCreated().getYear() == date.getYear() && member.getCreated().getDayOfMonth() < 8).count();


            Double secondWeekSavings =  getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                            == date.getMonth() && member.getCreated().getYear() == date.getYear()
                            && member.getCreated().getDayOfMonth() > 7 && member.getCreated().getDayOfMonth() <=15)
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getTotalSavings(),Double::sum);

            Integer secondWeekMembers = (int) getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                    == date.getMonth() && member.getCreated().getYear() == date.getYear()
                    && member.getCreated().getDayOfMonth() > 7 && member.getCreated().getDayOfMonth() <=15).count();

            Double thirdWeekSavings =  getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                            == date.getMonth() && member.getCreated().getYear() == date.getYear()
                            && member.getCreated().getDayOfMonth() > 15 && member.getCreated().getDayOfMonth() <=22)
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getTotalSavings(),Double::sum);

            Integer thirdWeekMembers = (int) getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                    == date.getMonth() && member.getCreated().getYear() == date.getYear()
                    && member.getCreated().getDayOfMonth() > 15 && member.getCreated().getDayOfMonth() <=22).count();

            Double fourthWeekSavings =  getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                            == date.getMonth() && member.getCreated().getYear() == date.getYear()
                            && member.getCreated().getDayOfMonth() > 22 && member.getCreated().getDayOfMonth() <=29)
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getTotalSavings(),Double::sum);

            Integer fourthWeekMembers = (int) getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                    == date.getMonth() && member.getCreated().getYear() == date.getYear()
                    && member.getCreated().getDayOfMonth() > 22 && member.getCreated().getDayOfMonth() <=29).count();

            Double fifthWeekSavings =  getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                            == date.getMonth() && member.getCreated().getYear() == date.getYear()
                            && member.getCreated().getDayOfMonth() > 29)
                    .reduce(0.0,(partialTotal,member) -> partialTotal + member.getTotalSavings(),Double::sum);

            int fifthWeekMembers = (int) getAllMembers.stream().filter(member -> member.getCreated().getMonth()
                    == date.getMonth() && member.getCreated().getYear() == date.getYear()
                    && member.getCreated().getDayOfMonth() > 29).count();


            monthlyMembers.put("firstWeekMembers",firstWeekMembers);
            monthlyMembers.put("firstWeekSavings",firstWeekSavings);
            monthlyMembers.put("secondWeekMembers", secondWeekMembers);
            monthlyMembers.put("secondWeekSavings",secondWeekSavings);
            monthlyMembers.put("thirdWeekMembers",thirdWeekMembers);
            monthlyMembers.put("thirdWeekSavings",thirdWeekSavings);
            monthlyMembers.put("fourthWeekMembers",fourthWeekMembers);
            monthlyMembers.put("fourthWeekSavings",fourthWeekSavings);
            if(fifthWeekMembers > 0){
                monthlyMembers.put("fifthWeekMembers",fifthWeekMembers);
                monthlyMembers.put("fifthWeekSavings",fifthWeekSavings);
            }

            return monthlyMembers;
        }
        catch (DateTimeException e){
            monthlyMembers.put("ErrorMessage",e.getMessage());
            return  monthlyMembers;
        }
    }
}


//  for month 2, get the reading from day one to day seven
// how do you get the members for a particular day
    // pass in the day from the date and sum the