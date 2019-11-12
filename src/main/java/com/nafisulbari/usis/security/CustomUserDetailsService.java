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
    private UserService userService;

    public CustomUserDetailsService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

            User user = userRepository.findByEmail(s);

            System.out.println("service::::" + user.toString());

            CustomUserDetails cud = new CustomUserDetails(user);
            return cud;

    }
}
