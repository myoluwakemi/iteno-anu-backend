package com.ajo.itedo.controller;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;
import com.ajo.itedo.service.AjoBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajo")
public class AjoBranchController {

    @Autowired
    AjoBranchService ajoBranchService;

    @PostMapping("/create-branch")
    public ResponseEntity<String> saveAjoBranch(@RequestBody AjoBranchDto ajoBranchDto){
       return ResponseEntity.ok(ajoBranchService.save(ajoBranchDto));
    }

    @GetMapping("/branches")
    public List<AjoBranch> getAllBranches(){
        return ajoBranchService.getAllAjoBranches();
    }

    @GetMapping("/branch/{branchName}")
    public AjoBranch getBranch(@PathVariable("branchName") String branchName){
        return ajoBranchService.findAjoBranchByBranchName(branchName);
    }

}
