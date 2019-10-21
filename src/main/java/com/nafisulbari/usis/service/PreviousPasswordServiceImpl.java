package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.PreviousPassword;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.security.MD5;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Service
public class PreviousPasswordServiceImpl implements PreviousPasswordService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Boolean findPreviousPasswordByEmail(PasswordRequest passwordRequest) {


        MD5 md5=new MD5();
        String hashed=md5.getMd5(passwordRequest.getPassword());
        System.out.println("impl req 2"+passwordRequest.getPassword());


        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select p.password from PreviousPassword p where p.email=:email");
        theQuery.setParameter("email", passwordRequest.getEmail());

        List<String> listPreviousPasswords = null;
        listPreviousPasswords = (List<String>) theQuery.getResultList();

        System.out.println("list prev pass impl 3: "+listPreviousPasswords.toString());

        for (String pass : listPreviousPasswords) {
            System.out.println("LOOP TRUE CHECKER impl : "+pass.equals(hashed));
            if (pass.equals(hashed)) {

                System.out.println("returning true impl 4/5");
                return true;
            }
        }
        System.out.println("returning false impl 4/5");
        return false;

    }
}
