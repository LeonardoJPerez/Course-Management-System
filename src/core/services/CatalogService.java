package cms.core.services;

import cms.core.models.Course;
import cms.core.models.Semester;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class CatalogService {

    private static CatalogService _instance;
    private List<Semester> semesters;
    private Semester currentSemester;

    private CatalogService() {
        this.semesters = new ArrayList<>();
    }

    public static CatalogService getInstance() {
        if (_instance == null) {
            _instance = new CatalogService();
        }

        return _instance;
    }

    public void addCourse(Course course, String semesterId){}
    public void removeCourse(String courseId, String semesterId){}
    public void updateCourse(Course course, String semesterId){}
    public void getCourseCount(){}
    public void getCourse(String courseId){}

    public void getSemester(String semesterId){}
    public void searchCourse(String searchTerm){}

    public Semester getCurrentSemester(){
        return this.currentSemester;
    }
    public void setCurrentSemester(Semester semester){
        this.currentSemester = semester;
    }
    public void getSemesterCourses(String semesterId){}
    public void addSemesterCourses(Semester semester){}

    public List<Course> getOnlineCourses(Semester semester){
        return null;
    }
    public List<Course> getOnCampusCourses(Semester semester){
        return null;
    }

}
