package com.speer.assessment.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.speer.assessment.dto.AuthRequestDTO;
import com.speer.assessment.entity.User;

public interface UserService {

    public User signUp(AuthRequestDTO authRequestDTO);

    public UserDetails loadUserByUsername(String username) throws  UsernameNotFoundException;
    
}
