package com.nafisulbari.usis.service;

import com.nafisulbari.usis.model.CustomUserDetails;
import com.nafisulbari.usis.model.User;
import com.nafisulbari.usis.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        return optionalUser.map(CustomUserDetails::new).get();
    }
}
