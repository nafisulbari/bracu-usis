package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class PreviousPasswordServiceImpl implements PreviousPasswordService {


    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    public PreviousPasswordServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Boolean findPreviousPasswordByEmail(PasswordRequest passwordRequest) {


        String hashed =passwordEncoder.encode(passwordRequest.getPassword());

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select p.password from PreviousPassword p where p.email=:email");
        theQuery.setParameter("email", passwordRequest.getEmail());

        List<String> listPreviousPasswords = null;
        listPreviousPasswords = (List<String>) theQuery.getResultList();

        for (String pass : listPreviousPasswords) {
            if (pass.equals(hashed)) {

                return true;
            }
        }

        return false;

    }
}
