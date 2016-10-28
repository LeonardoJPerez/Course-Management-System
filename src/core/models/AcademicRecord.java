package cms.core.models;

import cms.core.enumerations.CourseGrade;
import cms.core.models.users.Instructor;
import cms.core.models.users.Student;

import java.text.MessageFormat;
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

    public AcademicRecord(Course course, CourseGrade grade, Semester semester, Student student, Instructor instructor){
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
        this.instructor = instructor;
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

    @Override
    public String toString() {
        MessageFormat   fmt = new MessageFormat("{0},  {1},  {2},  {3},  {4}");
        Object[]    values = new Object[]{this.getStudent().getUUID(), this.getCourse().getCourseId(), this.getInstructor().getUUID(), this.getComments(), this.getGrade()};

        return fmt.format(values);
    }

    public String toStringVerbose() {
        MessageFormat fmt;
        Object[] values;
        if (this.comments != null && this.comments.length() > 0) {
            fmt = new MessageFormat("[{0}] Course ID: {1} - Student ID: {2} - Instructor ID: {3} - Grade: {4} - Comments: {5}");
            values = new Object[]{this.getId(), this.getCourse().getCourseId(), this.getStudent().getUUID(), this.getInstructor().getUUID(), this.getGrade(), this.getComments()};
        } else {
            fmt = new MessageFormat("[{0}] Course ID: {1} - Student ID: {2} - Instructor ID: {3} - Grade: {4}");
            values = new Object[]{this.getId(), this.getCourse().getCourseId(), this.getStudent().getUUID(), this.getInstructor().getUUID(), this.getGrade()};
        }

        return fmt.format(values);
    }
}
