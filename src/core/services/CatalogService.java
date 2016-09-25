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
        this.semesters = new ArrayList<Semester>();
    }

    public static CatalogService getInstance() {
        if (_instance == null) {
            _instance = new CatalogService();
        }

        return _instance;
    }


    public void addCourse(Course course, UUID semesterId){}
    public void removeCourse(UUID courseId, UUID semesterId){}
    public void updateCourse(Course course, UUID semesterId){}
    public void getCourseCount(){}
    public void getCourse(UUID courseId){}

    public void getSemester(UUID semesterId){}

    public void searchCourse(String searchTerm){}

    public Semester getCurrentSemester(){
        return this.currentSemester;
    }
    public void setCurrentSemester(Semester semester){
        this.currentSemester = semester;
    }
    public void getSemesterCourses(UUID semesterId){

    }
    public void addSemesterCourses(Semester semester){}

    public List<Course> getOnlineCourses(Semester semester){
        return null;
    }
    public List<Course> getOnCampusCourses(Semester semester){
        return null;
    }

}
