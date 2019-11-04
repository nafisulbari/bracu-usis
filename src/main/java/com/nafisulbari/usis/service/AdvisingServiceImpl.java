package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Advising;
import com.nafisulbari.usis.repo.AdvisingRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Service
public class AdvisingServiceImpl implements AdvisingService {


    private AdvisingRepository advisingRepository;

    private EntityManager entityManager;

    @Autowired
    public AdvisingServiceImpl(AdvisingRepository theAdvisingRepo, EntityManager theEntityMan) {

        advisingRepository = theAdvisingRepo;
        entityManager = theEntityMan;
    }


    @Override
    public List findAdvisedCourses(int studentID) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select a from Advising a where a.stdId=:studentID");
        theQuery.setParameter("studentID", studentID);

        List advisedCourses = null;
        try {
            advisedCourses = theQuery.getResultList();

        } catch (NoResultException nre) {
            System.out.println("no entity found class:AdvisingServiceImpl. method:findAdvisedCourses");
        }
        return advisedCourses;

    }

    @Override
    public void saveAdvisedCourse(Advising courseToAdvice) {
        advisingRepository.save(courseToAdvice);

    }

    @Override
    public void deleteAdvicedCourse(int theID) {
        advisingRepository.deleteById(theID);
    }
}
