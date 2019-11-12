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
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private PreviousPasswordService previousPasswordService;
    private UserRepository userRepository;
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(PreviousPasswordService previousPasswordService , UserRepository userRepository, EntityManager entityManager) {
        this.previousPasswordService = previousPasswordService;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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

        previousPasswordService.saveApreviousPassword(previousPassword);

        userRepository.save(theUser);

    }

    @Override
    public void deleteUserById(int theID) {
        Optional<User> user =userRepository.findById(theID);

        previousPasswordService.deletePreviousPasswordsOfUser(user.get());

        userRepository.deleteById(theID);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

}
