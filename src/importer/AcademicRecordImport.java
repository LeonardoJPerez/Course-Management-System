package cms.importer;

import cms.core.enumerations.CourseGrade;
import cms.core.enumerations.CourseType;
import cms.core.enumerations.SemesterName;
import cms.core.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class AcademicRecordImport extends BaseImport {

    private List<AcademicRecord> records;
    private List<Semester> semesters;
    private List<Student> students;
    private List<Instructor> instructors;

    public AcademicRecordImport(String importFilePath, List<Semester> semesters, List<Student> students, List<Instructor> instructors) {
        super(importFilePath);

        this.records = new ArrayList<AcademicRecord>();

        this.semesters = semesters;
        this.students = students;
        this.instructors = instructors;
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            for (String l : lines) {

                String[] values = l.split(",");
                if (values.length < 3){
                    // Minimum amount of data required not met.
                    continue;
                }

                String studentId = values[0];
                Student student = getStudent(studentId);

                String courseId = values[1];
                Semester semester = getSemester(courseId);
                Course course = getCourse(semester, courseId);

                String instructorId = values[2];
                Instructor instructor = getInstructors(instructorId);

                String comments = "";
                CourseGrade grade;
                if (values.length > 4){
                    // Get comments
                    comments = values[3];
                    grade = CourseGrade.valueOf(values[4]);
                }else{
                    grade = CourseGrade.valueOf(values[3]);
                }

                AcademicRecord newRecord = new AcademicRecord(course, grade, semester, student, instructor);
                newRecord.setComments(comments);
                records.add(newRecord);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Student getStudent(String studentId){
        Student student = null;
        for (Student s : this.students){
            String id = s.getUUID();
            if (id.equals(studentId)){
                student = s;
                break;
            }
        }
        return student;
    }

    private Semester getSemester(String courseId){
        Semester semester = null;
        for (Semester s : this.semesters){
            for (Course c : s.getCurrentCourses()){
                String id = c.getCourseId();
                if (id.equals(courseId)){
                    semester = s;
                    break;
                }
            }

            if (semester != null){ break; }
        }
        return semester;
    }

    private Course getCourse(Semester s, String courseId){
        Course course = null;
        for (Course c : s.getCurrentCourses()){
            String id = c.getCourseId();
            if (id.equals(courseId)){
                course = c;
                break;
            }
        }

        return course;
    }

    private Instructor getInstructors(String instructorId){
        Instructor instructor = null;
        for (Instructor i : this.instructors){
            String id = i.getUUID();
            if (id.equals(instructorId)){
                instructor = i;
                break;
            }
        }
        return instructor;
    }

    public List<AcademicRecord> getRecords() {
        return this.records;
    }
}
