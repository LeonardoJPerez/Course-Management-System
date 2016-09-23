package models;

import enumerations.CourseGrade;
import enumerations.CourseType;

import java.util.UUID;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class AcademicRecord {

    private Student student;
    private CourseGrade grade;
    private Semester semester;
    private Course course;
    private Instructor instructor;
    private String comments;
    private UUID id;

    public AcademicRecord(Course course, CourseGrade grade, Semester semester, Student student){
        this.id = UUID.randomUUID();
        this.semester = semester;
        this.course = course;
        this.student = student;
        this.grade = grade;
    }

    public CourseGrade getGrade() {
        return this.grade;
    }

    public void setGrade(CourseGrade grade) {
        this.grade = grade;
    }

    public Semester getSemester() {
        return this.semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Student getStudent() {
        return this.student;
    }

    public Course getCourse() {
        return this.course;
    }

    public Instructor getInstructor() {
        return this.instructor;
    }

    public UUID getId() {
        return this.id;
    }
}
