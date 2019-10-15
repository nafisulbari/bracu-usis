package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.UserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository, EntityManager theEntityManager) {
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

        User user = null;
        user = (User) theQuery.getSingleResult();

        if (user == null) {
            throw new RuntimeException("no users found with email " + theUser.getEmail());
        }
        return user;
    }

    @Override
    public void saveOrUpdateUser(User theUser) {
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
