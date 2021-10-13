package com.ajo.itedo.repository;

import com.ajo.itedo.data.AjoBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AjoBranchRepo extends JpaRepository<AjoBranch,Integer> {
    Optional<AjoBranch> findAjoBranchByBranchName(String branchName);
    Boolean existsAjoBranchByBranchName(String branchName);
}
