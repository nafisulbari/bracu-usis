package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Course;

import java.util.List;

public interface CourseService {

    Course findCourseById(int theID);

    List<Course> findAllTheoryCourses();

    List<Course> findAllCourses();

    Course getMatchingLabCourse(Course theoryCourse);

    List<Course> searchTheoryCourses(String searchKey);

    public List<Course> getSpecificCourseList(String courseCode);

    void saveOrUpdateCourse(Course theCourse);

    void deleteCourse(int theID);


}
