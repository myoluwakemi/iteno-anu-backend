package com.ajo.itedo.service;

import com.ajo.itedo.controller.config.PasswordConfig;
import com.ajo.itedo.controller.config.TokenProvider;
import com.ajo.itedo.data.ApplicationUser;
import com.ajo.itedo.dto.ApplicationUserDto;
import com.ajo.itedo.repository.ApplicationUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("applicationUserService")
public class ApplicationUserServiceImpl implements ApplicationUserService, UserDetailsService {

    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PasswordConfig passwordConfig;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public String save(ApplicationUserDto applicationUserDto) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(applicationUserDto.getEmail());
        applicationUser.setPassword(passwordConfig.passwordEncoder().encode(applicationUserDto.getPassword()));
        applicationUserRepo.save(applicationUser);

        return "User with "+applicationUserDto.getEmail()+ "was saved successfully";
    }

    @Override
    public String generateToken(ApplicationUserDto applicationUserDto) {
        Optional<ApplicationUser> user = applicationUserRepo.findApplicationUserByUsername(applicationUserDto.getEmail());
        try {
            if (user.get().getIsActive()){
                return  "BAD REQUEST";
            }
            else {
                final Authentication authentication = authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        applicationUserDto.getEmail(),
                                        applicationUserDto.getPassword()
                                )
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return tokenProvider.generateToken(authentication,user.get());
            }
        }catch (RuntimeException e){
            return e.getMessage();
        }

    }

    @Override
    public ApplicationUser getApplicationUserByUsername(String username) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;

        Optional<ApplicationUser> activeUser = applicationUserRepo.findApplicationUserByUsername(email);
        if(activeUser.isEmpty()){
            throw new UsernameNotFoundException(String.format("User with username {} not found", email));
        }
        else {
            user = new User(activeUser.get().getUsername(),activeUser.get().getPassword(),
                    getAuthority(activeUser.get()));
        }

        return user;
    }

    private Set<SimpleGrantedAuthority> getAuthority(ApplicationUser user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        return authorities;
    }
}
