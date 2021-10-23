package com.ajo.itedo.repository;

import com.ajo.itedo.data.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepo  extends JpaRepository<ActivityLog, Integer> {
    List<ActivityLog> findActivityLogsByAjoBranch_BranchName(String branchName);
}
