package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;

import java.util.List;

public interface AjoBranchService {
    String save(AjoBranchDto ajoBranchDto);
    AjoBranch findAjoBranchByBranchName(String branchName);
    List<AjoBranch> getAllAjoBranches();
}
