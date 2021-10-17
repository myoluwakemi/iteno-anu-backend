package com.ajo.itedo.controller;

import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.dto.AjoMemberDto;
import com.ajo.itedo.service.AjoMemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ajo")
public class AjoMemberController {
   Logger logger = Logger.getLogger(AjoMemberController.class.getName());

    @Autowired
    AjoMemberService ajoMemberService;

    @PostMapping("/register-member")
    public ResponseEntity<?> registerMember(@RequestBody AjoMemberDto ajoMemberDto){
        Map<String,String> response = new HashMap<>();

        if (ajoMemberService.save(ajoMemberDto).endsWith("successfully")){
            response.put("message","Member with card number: "+ ajoMemberDto.getCardNumber()+ " saved successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else {
            response.put("message",ajoMemberService.save(ajoMemberDto));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/member/{cardNumber}")
    public AjoMember getMemberByCardNumber(@PathVariable String cardNumber){
        return ajoMemberService.findMemberByCardNumber(cardNumber);
    }
    @PutMapping("/member/{cardNumber}")
    public  ResponseEntity<?> updateMember(@PathVariable String cardNumber, @RequestBody AjoMemberDto ajoMemberDto){
        Map<String,String> response = new HashMap<>();
        if (ajoMemberService.updateMemberByCardNumber(cardNumber,ajoMemberDto) != null){
            return  new ResponseEntity<>(ajoMemberService.findMemberByCardNumber(cardNumber),HttpStatus.OK);
        }
        else {
            response.put("message","Member with card number: "+cardNumber+ " Not Found");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/member/{cardNumber}")
    public ResponseEntity<?> deleteMember(@PathVariable String cardNumber) throws Exception {
        Map<String,String> response = new HashMap<>();
        if (ajoMemberService.deleteMember(cardNumber).endsWith("successfully")){
            response.put("message", "Member with card number: "+ cardNumber+ " was deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            response.put("message", "Member with card number: "+ cardNumber+ " Not Found");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

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
