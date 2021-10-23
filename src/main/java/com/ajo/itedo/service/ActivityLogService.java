package com.ajo.itedo.service;

import com.ajo.itedo.data.ActivityLog;

import java.util.List;

public interface ActivityLogService {

    List<ActivityLog> findByBranchName(String branchName);

    List<ActivityLog> findAll();
    void deleteActivityLogById(Integer id);
}
