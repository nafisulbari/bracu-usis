package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.PreviousPassword;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.PreviousPasswordRepository;
import com.nafisulbari.usis.repo.UserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private PreviousPasswordRepository previousPasswordRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository, PreviousPasswordRepository thePreviousPasswordRepository, EntityManager theEntityManager) {
        previousPasswordRepository = thePreviousPasswordRepository;
        userRepository = theUserRepository;
        entityManager = theEntityManager;
    }

    @Override
    public User findUserById(int theID) {
        Optional<User> result = userRepository.findById(theID);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();

        } else {
            throw new RuntimeException("Did not find employee of ID: " + theID);
        }

        return theUser;
    }

    @Override
    public User findUserByEmail(User theUser) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select u from User u where u.email=:email");
        theQuery.setParameter("email", theUser.getEmail());
        System.out.println("finding user by email");
        User user = null;
        try {
            user = (User) theQuery.getSingleResult();

        } catch (NoResultException nre) {
            System.out.println("no entity found class:UserServiceImpl. method:findUserByEmail");
        }
        return user;

    }

    @Override
    public void saveOrUpdateUser(User theUser) {

        PreviousPassword previousPassword = new PreviousPassword();
        previousPassword.setEmail(theUser.getEmail());
        previousPassword.setPassword(theUser.getPassword());

        previousPasswordRepository.save(previousPassword);

        userRepository.save(theUser);

    }

    @Override
    public void deleteUserById(int theID) {
        userRepository.deleteById(theID);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    public String loginAuthenticator(User theUser) {

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select u from User u where u.email=:email");
        theQuery.setParameter("email", theUser.getEmail());
        try {
            User user = null;
            //TODO error solve

            user = (User) theQuery.getSingleResult();


            if (user == null) {
                throw new RuntimeException("no users found with email " + theUser.getEmail());
            }


            if (user.getPassword().equals(theUser.getPassword())) {
                return user.getRole();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //password DID not match
        return "FAILED";
    }
}
