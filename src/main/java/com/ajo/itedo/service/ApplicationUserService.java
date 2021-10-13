package com.ajo.itedo.service;

import com.ajo.itedo.data.ApplicationUser;
import com.ajo.itedo.dto.ApplicationUserDto;

public interface ApplicationUserService {
    String save(ApplicationUserDto applicationUserDto);
    String generateToken(ApplicationUserDto applicationUserDto);
    ApplicationUser getApplicationUserByUsername(String username);
}
