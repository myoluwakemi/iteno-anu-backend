package com.ajo.itedo.controller;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.dto.AjoBranchDto;
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

    @GetMapping("/branches")
    public List<AjoBranch> getAllBranches(){
        return ajoBranchService.getAllAjoBranches();
    }

    @GetMapping("/branch/{branchName}")
    public AjoBranch getBranch(@PathVariable("branchName") String branchName){
        return ajoBranchService.findAjoBranchByBranchName(branchName);
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
