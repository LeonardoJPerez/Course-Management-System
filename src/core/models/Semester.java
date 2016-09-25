package cms.core.models;

import cms.core.enumerations.SemesterName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class Semester {

    private List<Course> courses;
    private int year;
    private SemesterName name;


    public Semester(SemesterName name, int year){
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) < year){
            throw new IllegalArgumentException("Year cannot be less than current year.");
        }

        this.year = year;
        this.name = name;
        this.courses = new ArrayList<Course>();
    }

    public int getYear(){
        return this.year;
    }

    public SemesterName getSemesterName(){
        return this.name;
    }

    public String getFullName(){
        if (this.getSemesterName() == SemesterName.NoSemester){
            return "N/A";
        }else {
            return this.name.toString() + "/" + this.year;
        }
    }

    public List<Course> getCurrentCourses(){
        return this.courses;
    }

    public boolean addCourse(Course newCourse){
         if(newCourse == null){
             throw new IllegalArgumentException("Course cannot be null.");
         }

         boolean exists = false;
        for (Course c: this.courses) {
            if (newCourse.getCourseId() == c.getCourseId()){
                exists = true;
                break;
            }
        }

        if (!exists){
            this.courses.add(newCourse);
            return true;
        }

        return false;
    }

    public List<Instructor> getActiveInstructors(){
        List<Instructor> instructors = new ArrayList<Instructor>();

        for (Course c: this.courses) {
            instructors.add(c.getInstructor());
        }

        return instructors;
    }
}
