package com.shevchenko.DiplomaWorkStudyPlatform.services;

import com.shevchenko.DiplomaWorkStudyPlatform.models.User;
import com.shevchenko.DiplomaWorkStudyPlatform.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Username or password not found");
        }
        return new CustomUserDetails(user);
    }

    public Collection<? extends GrantedAuthority> authorities(){
        return List.of(new SimpleGrantedAuthority("USER"));
    }
}
