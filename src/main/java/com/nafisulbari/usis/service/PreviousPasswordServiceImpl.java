package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.PreviousPassword;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.PreviousPasswordRepository;
import org.hibernate.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PreviousPasswordServiceImpl implements PreviousPasswordService {


    private PasswordEncoder passwordEncoder;
    private PreviousPasswordRepository previousPasswordRepository;


    private EntityManager entityManager;

    public PreviousPasswordServiceImpl(PasswordEncoder passwordEncoder, PreviousPasswordRepository previousPasswordRepository, EntityManager entityManager) {
        this.passwordEncoder = passwordEncoder;
        this.previousPasswordRepository = previousPasswordRepository;
        this.entityManager = entityManager;
    }


    @Override
    public Boolean findPreviousPasswordByEmail(PasswordRequest passwordRequest) {

        String requestedPlainPass = passwordRequest.getPassword();

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select p.password from PreviousPassword p where p.email=:email");
        theQuery.setParameter("email", passwordRequest.getEmail());

        List<String> listPreviousPasswords = null;
        listPreviousPasswords = (List<String>) theQuery.getResultList();

        for (String pass : listPreviousPasswords) {
            if (passwordEncoder.matches(requestedPlainPass,pass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deletePreviousPasswordsOfUser(User user) {

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("delete from PreviousPassword where email=:email");
        theQuery.setParameter("email", user.getEmail());

        theQuery.executeUpdate();
    }

    @Override
    public void saveApreviousPassword(PreviousPassword previousPassword) {
        previousPasswordRepository.save(previousPassword);
    }
}
