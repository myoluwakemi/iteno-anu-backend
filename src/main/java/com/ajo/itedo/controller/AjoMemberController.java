package com.ajo.itedo.controller;

import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.dto.AjoMemberDto;
import com.ajo.itedo.service.AjoMemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ajo")
public class AjoMemberController {
   Logger logger = Logger.getLogger(AjoMemberController.class.getName());

    @Autowired
    AjoMemberService ajoMemberService;

    @PostMapping("/register-member")
    public ResponseEntity<String> registerMember(@RequestBody AjoMemberDto ajoMemberDto){
        return ResponseEntity.ok(ajoMemberService.save(ajoMemberDto));
    }

    @GetMapping("/member/{cardNumber}")
    public AjoMember getMemberByCardNumber(@PathVariable String cardNumber){
        return ajoMemberService.findMemberByCardNumber(cardNumber);
    }
    @PatchMapping("/member/{cardNumber}")
    public  AjoMember updateMember(@PathVariable String cardNumber, @RequestBody AjoMemberDto ajoMemberDto){
        return ajoMemberService.updateMemberByCardNumber(cardNumber,ajoMemberDto);
    }

    @DeleteMapping("/member/{cardNumber}")
    public ResponseEntity<?> deleteMember(@PathVariable String cardNumber){

        return ResponseEntity.ok(ajoMemberService.deleteMember(cardNumber));
    }

    @GetMapping("/members-in-branch/{branchName}")
    public List<AjoMember> membersInBranch(@PathVariable String branchName){
        return ajoMemberService.getMemberByBranch(branchName);
    }

    @GetMapping("/all-members")
    public List<AjoMember> allMembers(){
        return  ajoMemberService.getAllMembers();
    }
}
