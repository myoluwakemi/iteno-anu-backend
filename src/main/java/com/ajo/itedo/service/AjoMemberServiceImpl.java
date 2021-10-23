package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoBranch;
import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.dto.AjoMemberDto;
import com.ajo.itedo.repository.AjoBranchRepo;
import com.ajo.itedo.repository.AjoMemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AjoMemberServiceImpl implements AjoMemberService{
  Logger logger = Logger.getLogger(AjoMemberServiceImpl.class.getName());
    @Autowired
    AjoMemberRepo ajoMemberRepo;

    @Autowired
    AjoBranchRepo ajoBranchRepo;

    @Override
    public String save(AjoMemberDto ajoMemberDto) {
        if (ajoMemberRepo.existsAjoMemberByCardNumber(ajoMemberDto.getCardNumber())){
            return "Member with card number "+ ajoMemberDto.getCardNumber() + " Already exist";
        }
        else {
            AjoMember ajoMember = new AjoMember();
            ajoMember.setFirstName(ajoMemberDto.getFirstName());
            ajoMember.setLastName(ajoMemberDto.getLastName());
            ajoMember.setCardNumber(ajoMemberDto.getCardNumber());
            ajoMember.setPhotograph(ajoMemberDto.getPhotograph());
            if (ajoMemberDto.getTotalSavings() != null){
                ajoMember.setTotalSavings(ajoMemberDto.getTotalSavings());
            }
            Optional<AjoBranch> branch = ajoBranchRepo.findAjoBranchByBranchName(ajoMemberDto.getBranchName());
            if (branch.isPresent()){
                ajoMember.setAjoBranch(branch.get());
                ajoMemberRepo.save(ajoMember);
                return "Member with card number: "+ ajoMemberDto.getCardNumber() + " Saved successfully";
            }
            else {
                return ajoMemberDto.getBranchName() + " does not exist";
            }
        }

    }

    @Override
    public AjoMember findMemberByCardNumber(String cardNumber) {
        if (ajoMemberRepo.findAjoMemberByCardNumber(cardNumber).isPresent()) {
            return ajoMemberRepo.findAjoMemberByCardNumber(cardNumber).get();
        }
        return null;
    }

    @Override
    public AjoMember updateMemberByCardNumber(String cardNumber, AjoMemberDto ajoMemberDto) {
        if (ajoMemberRepo.findAjoMemberByCardNumber(cardNumber).isPresent()) {
            AjoMember ajoMember = ajoMemberRepo.findAjoMemberByCardNumber(cardNumber).get();
            if (ajoMemberDto.getFirstName() != null && !Objects.equals(ajoMemberDto.getFirstName(), "")){
                ajoMember.setFirstName(ajoMemberDto.getFirstName());
            }
            if (ajoMemberDto.getLastName() != null && !Objects.equals(ajoMemberDto.getLastName(), "")){
                ajoMember.setLastName(ajoMemberDto.getLastName());
            }
            if (ajoMemberDto.getPhotograph() != null && !Objects.equals(ajoMemberDto.getPhotograph(), "")){
                ajoMember.setPhotograph(ajoMemberDto.getPhotograph());
            }
            if (ajoMemberDto.getTotalSavings() != null){
                ajoMember.setTotalSavings(ajoMemberDto.getTotalSavings()+ajoMember.getTotalSavings());
                ajoMember.setCurrentSavings(ajoMemberDto.getTotalSavings());
            }
            ajoMemberRepo.save(ajoMember);
            return ajoMember;
        }
        else {
            return null;
        }

    }

    @Override
    public String deleteMember(String cardNumber) throws Exception {
        try {
            if (ajoMemberRepo.findAjoMemberByCardNumber(cardNumber).isPresent()) {
                ajoMemberRepo.deleteAjoMemberByCardNumber(cardNumber);
                return "Member with card number "+ cardNumber + " was deleted successfully";
            }
            else {
                return "Not Found";
            }
        }
        catch (Exception e){
            return e.getCause().getMessage()+ ", Member with card number "+cardNumber+ " has a relationship in the database";
        }
    }

    @Override
    public List<AjoMember> getMemberByBranch(String branchName) {
        return ajoMemberRepo.findAjoMemberByAjoBranch_BranchName(branchName);
    }

    @Override
    public List<AjoMember> getAllMembers() {
        return ajoMemberRepo.findAll();
    }
}
