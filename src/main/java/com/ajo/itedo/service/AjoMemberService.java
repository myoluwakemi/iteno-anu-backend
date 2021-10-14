package com.ajo.itedo.service;

import com.ajo.itedo.data.AjoMember;
import com.ajo.itedo.dto.AjoMemberDto;

import java.util.List;

public interface AjoMemberService {
    String save(AjoMemberDto ajoMemberDto);

    AjoMember findMemberByCardNumber(String cardNumber);

    AjoMember updateMemberByCardNumber(String cardNumber, AjoMemberDto ajoMemberDto);

    String deleteMember(String cardNumber);

    List<AjoMember> getMemberByBranch(String branchName);

    List<AjoMember> getAllMembers();

}
