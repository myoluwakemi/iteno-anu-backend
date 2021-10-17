package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;
import com.ajo.itedo.repository.AjoBranchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AjoBranchServiceImpl implements AjoBranchService{

    @Autowired
    AjoBranchRepo ajoBranchRepo;

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
    public AjoBranch findAjoBranchByBranchName(String branchName) {
        if (ajoBranchRepo.findAjoBranchByBranchName(branchName).isPresent())
        return ajoBranchRepo.findAjoBranchByBranchName(branchName).get();

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
}
