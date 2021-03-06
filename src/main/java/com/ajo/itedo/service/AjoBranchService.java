package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;

import java.util.List;
import java.util.Map;

public interface AjoBranchService {
    String save(AjoBranchDto ajoBranchDto);
    Map<String, Object> findAjoBranchByBranchName(String branchName);
    List<AjoBranch> getAllAjoBranches();
    AjoBranch updateBranch(Integer branchId,AjoBranchDto ajoBranchDto);
    String deleteBranch(Integer branchId);
    Map<String,Object> getDashboardStat();
    Map<String,Object> getMembersByWeekly(Integer month, Integer year);
}
