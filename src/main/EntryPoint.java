package cms.main;

import cms.core.models.Course;
import cms.core.models.Instructor;
import cms.core.models.Semester;
import cms.core.models.Student;
import cms.importer.CourseImport;
import cms.importer.InstructorImport;
import cms.importer.StudentImport;

import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class EntryPoint {

    private static String filePath = "C:\\Source\\CS6310\\Course-Management-System\\OMSCS6310_Assignment3_Fall2016_test_cases\\test_case_0";

    public static void main(String[] args) {

        ProcessStudents();
        ProcessCourses();
        ProcessInstructors();

    }

    public static void ProcessStudents(){
        StudentImport studentImporter = new StudentImport(filePath + "\\students.csv");
        studentImporter.process();

        List<Student> students = studentImporter.getStudents();
        System.out.println("Students extracted: " + students.size());

        for (Student s: students) {
            System.out.println(s.toString());
        }
    }

    public static void ProcessCourses(){
        CourseImport courseImporter = new CourseImport(filePath + "\\courses.csv");
        courseImporter.process();

        List<Semester> semesters = courseImporter.getCourses();
        int count = 0;
        for (Semester s: semesters) {
            count += s.getCurrentCourses().size();
        }

        System.out.println("Courses extracted: " + count);

        for (Semester s: semesters) {
            for (Course c: s.getCurrentCourses()) {
                System.out.println(c.getFullName() + " " + s.getFullName());
            }
        }
    }

    public static void ProcessInstructors(){
        InstructorImport instructorImport = new InstructorImport(filePath + "\\instructors.csv");
        instructorImport.process();

        List<Instructor> instructors = instructorImport.getInstructors();
        System.out.println("Instructors extracted: " + instructors.size());

        for (Instructor i: instructors) {
            System.out.println(i.toString());
        }
    }
}
