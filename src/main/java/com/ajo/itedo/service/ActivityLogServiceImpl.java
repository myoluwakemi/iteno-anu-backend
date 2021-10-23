package com.ajo.itedo.service;

import com.ajo.itedo.data.ActivityLog;
import com.ajo.itedo.repository.ActivityLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService{

    @Autowired
    ActivityLogRepo activityLogRepo;

    @Override
    public List<ActivityLog> findByBranchName(String branchName) {
        return activityLogRepo.findActivityLogsByAjoBranch_BranchName(branchName);
    }

    @Override
    public List<ActivityLog> findAll() {
        return activityLogRepo.findAll();
    }

    @Override
    public void deleteActivityLogById(Integer id) {
      activityLogRepo.deleteById(id);
    }
}
