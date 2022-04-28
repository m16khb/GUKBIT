package com.gukbit.service;

import com.gukbit.domain.Course;
import com.gukbit.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    //코스 아이디를 통해 존재하는 코드를 리스트로 반환
    public List<Course> getCourseData(String courseId){
        return courseRepository.findAllById(courseId);
    }


    // 학원 아이디를 통해 학원아이디와 일치하는 과정을 리스트로 반환
    public List<Course> getCourseList(String code) { return courseRepository.findAllByAcademyCode(code); }


//    public List<Course> getCourseListByAcademyId(String academyId){
//        return courseRepository.findAllByAcademyCode(academyId);
//    }

}
