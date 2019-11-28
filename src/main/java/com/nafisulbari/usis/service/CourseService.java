package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Course;

import java.util.List;

public interface CourseService {

    Course findCourseById(int theID);

    List<Course> findAllCourses();

    List<Course> searchCourses(String searchKey);

    void saveOrUpdateCourse(Course theCourse);

    void deleteCourse(int theID);






}
