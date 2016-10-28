package cms.core.services;

import cms.core.models.Course;
import cms.core.models.Semester;
import org.apache.commons.lang3.NotImplementedException;

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
    public void addCourses(List<Course> courses, String semesterId){}

    public void removeCourse(String courseId, String semesterId){}
    public void updateCourse(Course course, String semesterId){}
    public void getCourseCount(){}
    public  List<Course> getDistinctCourses(){
        List<Course> courses = new ArrayList<>();

        for (Semester s: this.semesters) {
            courses.addAll(s.getCurrentCourses());
        }

        // Filter unique.
        List<Course> courses2 = new ArrayList<>();
        for (Course c: courses) {
            if (!courses2.contains(c)){
                courses2.add(c);
            }
        }

        return courses2;
    }
    public Course getCourse(String courseId){
        for (Course c: this.getDistinctCourses()) {
            if (c.getCourseId().equals(courseId)){
                return c;
            }
        }

        return null;
    }

    public Semester getSemester(String semesterId){
        throw new NotImplementedException("Method not implemented");
    }

    public Semester getSemesterByCourse(String courseId){
        for (Semester s: this.semesters) {
            for (Course c: s.getCurrentCourses()) {
                if (c.getCourseId().equals(courseId)){
                    return s;
                }
            }
        }

        return null;
    }
    public void searchCourse(String searchTerm){}

    public Semester getCurrentSemester(){
        return this.currentSemester;
    }
    public void setCurrentSemester(Semester semester){
        this.currentSemester = semester;
    }
    public void getSemesterCourses(String semesterId){}

    public void addSemesterCourses(Semester semester){
        Semester s = null;
        for (Semester s1: this.semesters) {
            if (!s1.getSemesterName().equals(semester.getSemesterName())){ continue; }
            s = s1;
            break;
        }

        if (s != null){
            s.addCourses(semester.getCurrentCourses());
        }else{
            this.semesters.add(semester);
        }
    }

    public List<Course> getOnlineCourses(Semester semester){
        return null;
    }
    public List<Course> getOnCampusCourses(Semester semester){
        return null;
    }

}
