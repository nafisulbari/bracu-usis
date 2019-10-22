package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PasswordRequest;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.PasswordRequestRepository;
import com.nafisulbari.usis.security.MD5;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordRequestImpl implements PasswordRequestService {

   private PasswordRequestRepository passwordRequestRepository;
    private EntityManager entityManager;

   @Autowired
   public PasswordRequestImpl(PasswordRequestRepository thePasswordRequestRepository, EntityManager theEntityManager){
       passwordRequestRepository=thePasswordRequestRepository;
       entityManager=theEntityManager;
   }

    @Override
    public void savePasswordRequest(PasswordRequest thePasswordRequest) {

        MD5 md5=new MD5();
        String hashed=md5.getMd5(thePasswordRequest.getPassword());
        System.out.println(hashed);
        thePasswordRequest.setPassword(hashed);

       passwordRequestRepository.save(thePasswordRequest);

    }

    @Override
    public void rejectByPasswordId(int theID) {
        passwordRequestRepository.deleteById(theID);
    }
    @Override
    public void acceptByPasswordEmail(int theID) {
       Optional<PasswordRequest> passwordRequest = passwordRequestRepository.findById(theID);
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select u from User u where u.email=:email");
        theQuery.setParameter("email", passwordRequest.get().getEmail());

        User user = null;
        user = (User) theQuery.getSingleResult();

        if (user == null) {
            throw new RuntimeException("no users found with email " + passwordRequest.get().getEmail());
        }
        user.setPassword(passwordRequest.get().getPassword());

        passwordRequestRepository.deleteById(passwordRequest.get().getId());
    }

    @Override
    public List<PasswordRequest> findAllPasswordRequest() {
       List<PasswordRequest> passwordRequests= (List<PasswordRequest>) passwordRequestRepository.findAll();
       return passwordRequests;
    }
}
