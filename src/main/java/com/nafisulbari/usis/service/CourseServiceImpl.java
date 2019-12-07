package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.repo.CourseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;


@Service
public class CourseServiceImpl implements CourseService {


    private CourseRepository courseRepository;
    private AdvisingService advisingService;
    private EntityManager entityManager;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, AdvisingService advisingService, EntityManager entityManager) {
        this.courseRepository = courseRepository;
        this.advisingService = advisingService;
        this.entityManager = entityManager;
    }


    @Override
    public Course findCourseById(int theID) {
        Optional<Course> result = courseRepository.findById(theID);

        Course theCourse;

        if (result.isPresent()) {
            theCourse = result.get();

        } else {
            throw new RuntimeException("Did not find Course of ID: " + theID);
        }

        return theCourse;
    }

    @Override
    public List<Course> findAllTheoryCourses() {
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select c from Course c where c.lab=:lab ORDER BY c.courseCode");
        theQuery.setParameter("lab", 0);

        List courses = null;
        try {
            courses = theQuery.getResultList();

        } catch (NoResultException nre) {
            System.out.println("No courses found");
        }
        return (List<Course>) courses;
    }


    @Override
    public List<Course> findAllCourses() {
        return (List<Course>) courseRepository.findAll(Sort.by(Sort.Direction.ASC, "courseCode"));
    }

    @Override
    public List<Course> searchTheoryCourses(String searchKey) {

        searchKey = "%" + searchKey + "%";
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select c from Course c where c.lab=:lab and c.courseCode like :searchKey");
        theQuery.setParameter("lab", 0);
        theQuery.setParameter("searchKey", searchKey);

        List courses = null;
        try {
            courses = theQuery.getResultList();

        } catch (NoResultException nre) {
            System.out.println("No courses found");
        }
        return (List<Course>) courses;

    }

    @Override
    public Course getMatchingLabCourse(Course theoryCourse) {

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select c from Course c where c.courseCode=:courseCode and c.section=:section and c.lab=:lab");
        theQuery.setParameter("courseCode", theoryCourse.getCourseCode());
        theQuery.setParameter("section", theoryCourse.getSection());
        theQuery.setParameter("lab", 1);

        Course labCourse = new Course();

        try {
            labCourse = (Course) theQuery.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("ignoring NoResultException, because course has no lab, message from:CourseServiceImpl,GetMatchingLabcourse()");
        } finally {
            return labCourse;
        }
    }
    @Override
    public List<Course> getSpecificCourseList(String courseCode) {

        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("Select c from Course c where c.courseCode=:courseCode");
        theQuery.setParameter("courseCode", courseCode);

        List courses = null;
        try {
            courses = theQuery.getResultList();

        } catch (NoResultException nre) {
            System.out.println("No courses found");
        }
        return (List<Course>) courses;

    }

    @Override
    public void saveOrUpdateCourse(Course theCourse) {
        courseRepository.save(theCourse);

    }

    @Override
    public void deleteCourse(int theID) {
        courseRepository.deleteById(theID);
    }
}
