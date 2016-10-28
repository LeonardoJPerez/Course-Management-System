package cms.importer;

import cms.core.models.Course;
import cms.core.models.EnrollmentRequest;
import cms.core.models.Semester;
import cms.core.models.users.Student;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 10/27/2016.
 */
public class EnrollmentRequestImport extends BaseImport {

    private List<Student> students;
    private List<Semester> semesters;
    private List<EnrollmentRequest> enrollmentRequests;

    public EnrollmentRequestImport(String importFilePath, List<Semester> semesters, List<Student> students) {
        super(importFilePath);

        this.students = students;
        this.semesters = semesters;
        this.enrollmentRequests = new ArrayList<>();
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 2) { continue; }

                // Extract Request Course Unique ID and Student ID.
                String studentId = values[0];
                String courseId = values[1];

                // Verify Courses exist in catalog.
                Pair<Semester, Course> semesterCoursePair = this.getCourse(courseId);
                if (semesterCoursePair == null){
                    continue;
                }

                if (semesterCoursePair.getKey() == null || semesterCoursePair.getValue() == null ){
                    continue;
                }

                Student student = this.getStudent(studentId);
                if (student == null){
                    continue;
                }

                // Create Request.
                EnrollmentRequest newRequest = new EnrollmentRequest(student, semesterCoursePair.getValue(), semesterCoursePair.getKey());
                this.enrollmentRequests.add(newRequest);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<EnrollmentRequest> getEnrollmentRequests(){
        return this.enrollmentRequests;
    }

    public Integer getTotalCount(){
        return this.getLineCount();
    }

    private Pair<Semester, Course> getCourse(String courseId){
        for (Semester s: this.semesters) {
            for (Course c: s.getCurrentCourses()) {
                if (c.getCourseId().equals(courseId)){
                    return new Pair<>(s, c);
                }
            }
        }

        return null;
    }

    private Student getStudent(String studentId){
        for (Student s: this.students) {
            if (s.getUUID().equals(studentId)){
                return s;
            }
        }

        return null;
    }


}
