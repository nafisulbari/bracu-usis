package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.entity.User;
import com.nafisulbari.usis.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Service
public class CourseServiceImpl implements CourseService {


    private CourseRepository courseRepository;

    private EntityManager entityManager;

    @Autowired
    public CourseServiceImpl(CourseRepository theCourseRepo, EntityManager theEntityManager) {
        courseRepository = theCourseRepo;
        entityManager = theEntityManager;
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
    public List<Course> findAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public void saveOrUpdateCourse(Course theCourse) {
        courseRepository.save(theCourse);
    }

    @Override
    public void deleteCourse(Course theCourse) {
        courseRepository.deleteById(theCourse.getId());
    }
}
