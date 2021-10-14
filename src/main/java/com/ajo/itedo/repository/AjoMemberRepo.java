package com.ajo.itedo.repository;
import com.ajo.itedo.data.AjoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AjoMemberRepo extends JpaRepository<AjoMember,Integer> {

    List<AjoMember> findAjoMemberByAjoBranch_BranchName(String branchName);
    Optional<AjoMember> findAjoMemberByFirstNameAndLastName(String firstName, String lastname);
    Optional<AjoMember> findAjoMemberByCardNumber(String cardNumber);
    void deleteAjoMemberByCardNumber(String cardNumber);
    Boolean existsAjoMemberByCardNumber(String cardNumber);



}
