package com.nafisulbari.usis.dev;

import com.nafisulbari.usis.entity.User;
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
//-------------TO insert users during app launch
//
//        User std9 = new User();
//        std9.setEmail("reg@bracu.ac.bd");
//        std9.setPassword(passwordEncoder.encode("123"));
//        std9.setActive(1);
//        std9.setDept("");
//        std9.setName("Reg");
//        std9.setCredit("");
//        std9.setMobile("0000");
//        std9.setRole("ADMIN");
//        std9.setPermissions("");
//        std9.setRoutine("");
//
//        userRepository.save(std9);


    }
}
