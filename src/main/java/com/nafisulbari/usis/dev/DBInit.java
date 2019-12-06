package com.nafisulbari.usis.dev;

import com.nafisulbari.usis.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DBInit implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

//------------RUNS EVERYTIME SERVER RUNS--------------------------------------------
//        User user = new User();
////        user.setEmail("std7@gmail.com");
////        user.setPassword(passwordEncoder.encode("000"));
////        user.setActive(1);
////        user.setCourseLimit(3);
////        user.setDept("CSE");
////        user.setName("std7");
////        user.setCredit("");
////        user.setMobile("0000");
////        user.setRole("STUDENT");
////        user.setPermissions("");
////        user.setRoutine("");
////        userRepository.save(user);


    }
}
