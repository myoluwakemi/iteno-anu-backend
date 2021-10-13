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
        if (ajoBranchRepo.existsAjoBranchByBranchName(ajoBranchDto.getBranchName())){
            return  ajoBranchDto.getBranchName()+" Already exist";
        }
        else {
            AjoBranch ajoBranch = new AjoBranch();
            ajoBranch.setBranchName(ajoBranchDto.getBranchName());
            ajoBranch.setAddress(ajoBranchDto.getAddress());
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
}
