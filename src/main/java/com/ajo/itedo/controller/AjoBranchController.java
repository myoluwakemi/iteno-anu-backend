package com.ajo.itedo.controller;

import com.ajo.itedo.data.ActivityLog;
import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;
import com.ajo.itedo.service.ActivityLogService;
import com.ajo.itedo.service.AjoBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ajo")
public class AjoBranchController {

    @Autowired
    AjoBranchService ajoBranchService;

    @Autowired
    ActivityLogService activityLogService;

    @PostMapping("/create-branch")
    public ResponseEntity<?> saveAjoBranch(@RequestBody AjoBranchDto ajoBranchDto){
        Map<String,String> response = new HashMap<>();
        if (ajoBranchService.save(ajoBranchDto).endsWith("successfully")){
            response.put("message","Branch with name: "+ ajoBranchDto.getBranchName()+ " was saved successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else {
            response.put("message","Branch with name: "+ ajoBranchDto.getBranchName()+ " Already exist");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/dashboard-stat")
    public ResponseEntity<?> dashboardStat(){
        return  new ResponseEntity<>(ajoBranchService.getDashboardStat(),HttpStatus.OK);
    }
    @GetMapping("/monthly-members/{month}/{year}")
    public ResponseEntity<?> monthlyMembersStats(@PathVariable Integer month, @PathVariable Integer year ){
        Map<String, Object> response = ajoBranchService.getMembersByWeekly(month,year);
        if (response.containsKey("ErrorMessage")){
            return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        else {
            return  new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    @GetMapping("/branches")
    public List<AjoBranch> getAllBranches(){
        return ajoBranchService.getAllAjoBranches();
    }

    @GetMapping("/branches/activityLogs")
    public List<ActivityLog> getAllLogs(){
        return activityLogService.findAll();
    }

    @GetMapping("/branch/{branchName}/activityLog")
    public List<ActivityLog> getLogsByBranchName(@PathVariable("branchName") String branchName){
        return activityLogService.findByBranchName(branchName);
    }
    @GetMapping("/branch/{branchName}")
    public ResponseEntity<?> getBranch(@PathVariable("branchName") String branchName){
        Map<String,Object> response;
        response = ajoBranchService.findAjoBranchByBranchName(branchName);
        if (response !=null){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("/branch/{activityLogId}")
    public void deleteActivityLogById(@PathVariable Integer activityLogId){
        activityLogService.deleteActivityLogById(activityLogId);
    }
    @PatchMapping("/branch/{branchId}")
    public ResponseEntity<?> updateBranch (@PathVariable Integer branchId, AjoBranchDto ajoBranchDto){
        Map<String,String> response = new HashMap<>();
        AjoBranch ajoBranch = ajoBranchService.updateBranch(branchId,ajoBranchDto);
        if (ajoBranch != null){
            return new ResponseEntity<>(ajoBranch,HttpStatus.OK);
        }
        else {
            response.put("message","Branch does not exist");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable Integer branchId){
        Map<String,String> response = new HashMap<>();
        String deletedBranch = ajoBranchService.deleteBranch(branchId);
        if (deletedBranch.endsWith("successfully")){
            response.put("message","Branch deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            response.put("message","Branch Not Found");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }
}
