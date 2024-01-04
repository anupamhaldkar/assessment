package com.speer.assessment.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.speer.assessment.dto.AuthRequestDTO;
import com.speer.assessment.entity.User;
import com.speer.assessment.repository.UserRepository;
import com.speer.assessment.service.UserService;
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User signUp(AuthRequestDTO authRequestDTO) {
        User user = new User();
        user.setUsername(authRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws  UsernameNotFoundException{
        Optional<User> optionalUser = userRepository.findByUsername(username);

            User user = optionalUser.orElseThrow(()-> new UsernameNotFoundException("Username " + username));
            
        

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), new ArrayList<>());
    }
    
}
