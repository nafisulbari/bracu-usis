package com.nafisulbari.usis.security;

import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.UserRepository;
import com.nafisulbari.usis.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository ) {
        this.userRepository = userRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            //fetching user from db to know its roles and passed onto spring's UserDetails()
            User user = userRepository.findByEmail(s);

            CustomUserDetails cud = new CustomUserDetails(user);
            return cud;

    }
}
