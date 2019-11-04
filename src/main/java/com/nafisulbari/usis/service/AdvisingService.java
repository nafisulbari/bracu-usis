package com.nafisulbari.usis.service;

import com.nafisulbari.usis.entity.Advising;

import java.util.List;

public interface AdvisingService {

    List findAdvisedCourses(int studentID);

    void saveAdvisedCourse(Advising courseToAdvice);

    void deleteAdvicedCourse(int theID);


}
