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
        if (course == null){
            throw new IllegalArgumentException("Course cannot be null.");
        }

        if (semester == null){
            throw new IllegalArgumentException("Semester cannot be null.");
        }

        if (student == null){
            throw new IllegalArgumentException("Student cannot be null.");
        }

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

    public boolean equals(AcademicRecord obj2) {
        if (!(obj2 instanceof AcademicRecord)){
            return false;
        }

        if (this.student.getUUID() != obj2.student.getUUID()){
            return false;
        }

        if (this.course.getCourseId() != obj2.course.getCourseId()){
            return false;
        }

        if (this.semester.getSemesterName() != obj2.semester.getSemesterName()){
            return false;
        }
        return true;
    }
}
