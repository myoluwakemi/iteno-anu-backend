package com.ajo.itedo.repository;

import com.ajo.itedo.data.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepo  extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findApplicationUserByUsername(String username);
}
