package cms.main;

import cms.core.enumerations.SemesterName;
import cms.core.models.*;
import cms.importer.AcademicRecordImport;
import cms.importer.CourseImport;
import cms.importer.InstructorImport;
import cms.importer.StudentImport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class EntryPoint {

    //private static String filePath = "C:\\Source\\CS6310\\Course-Management-System\\OMSCS6310_Assignment3_Fall2016_test_cases\\test_case_0\\";
    private static String filePath = "";

    private static List<Semester> semesters;

    private static List<Student> students;
    private static List<Instructor> instructors;
    private static List<Course> courses;
    private static List<AcademicRecord> records;

    public static void main(String[] args) {

        ProcessStudents();
        ProcessCourses();
        ProcessInstructors();
        ProcessAcademicRecords();

        PrintResults();
    }

    private static void ProcessStudents(){
        StudentImport studentImporter = new StudentImport(filePath + "students.csv");
        studentImporter.process();

        students = studentImporter.getStudents();
        //System.out.println("Students extracted: " + students.size());

        /*for (Student s: students) {
            System.out.println(s.toString());
        }*/
    }

    private static void ProcessCourses(){
        CourseImport courseImporter = new CourseImport(filePath + "courses.csv");
        courseImporter.process();

        semesters = courseImporter.getSemesters();
        courses = courseImporter.getCourses();

        int count = 0;
        for (Semester s: semesters) {
            count += s.getCurrentCourses().size();
        }

        System.out.println("Courses extracted: " + count);
        System.out.println("Unique Courses extracted: " + courses.size());
    }

    private static void ProcessInstructors(){
        InstructorImport instructorImport = new InstructorImport(filePath + "instructors.csv");
        instructorImport.process();

        instructors = instructorImport.getInstructors();
        /*System.out.println("Instructors extracted: " + instructors.size());

        for (Instructor i: instructors) {
            System.out.println(i.toString());
        }*/
    }

    private static void ProcessAcademicRecords(){
        AcademicRecordImport recordImporter = new AcademicRecordImport(filePath + "records.csv", semesters, students, instructors);
        recordImporter.process();

        records = recordImporter.getRecords();
        /*System.out.println("Records extracted: " + records.size());

        for (AcademicRecord a: records) {
            System.out.println(a.toString());
        }*/
    }

    private static int getOrphanStudentsCount(){
        List<Student> orphans = new ArrayList<>();

        for (Student s: students) {
            boolean exists = false;
            for (AcademicRecord r: records) {
                String sId1 = r.getStudent().getUUID();
                String sId2 = s.getUUID();
                if (sId1.equals(sId2)){
                    exists = true;
                    break;
                }
            }

            if (!exists){
                orphans.add(s);
            }
        }

        return orphans.size();
    }

    private static int getOrphanInstructorsCount(){
        List<Instructor> orphans = new ArrayList<>();

        for (Instructor i: instructors) {
            boolean exists = false;
            for (AcademicRecord r: records) {
                String sId = r.getInstructor().getUUID();
                if (sId.equals(i.getUUID())){
                    exists = true;
                    break;
                }
            }

            if (!exists){ orphans.add(i); }
        }

        return orphans.size();
    }

    private static int getOrphanCoursesCount(){
        List<Course> orphans = new ArrayList<>();

        for (Course c: courses) {
            boolean exists = false;
            for (AcademicRecord r: records) {
                String sId = r.getCourse().getCourseId();
                if (sId.equals(c.getCourseId())){
                    exists = true;
                    break;
                }
            }

            if (!exists){ orphans.add(c); }
        }


        return orphans.size();
    }

    private static int getCoursesCount(SemesterName sname){
        Semester s = null;
        for (int i = 0; i < semesters.size(); i++){
            if (semesters.get(i).getSemesterName() == sname){
                return semesters.get(i).getCurrentCourses().size();
            }
        }

        return 0;
    }

    private static void PrintResults(){
        System.out.println(records.size());
        System.out.println(students.size());
        System.out.println(getOrphanStudentsCount());
        System.out.println(instructors.size());
        System.out.println(getOrphanInstructorsCount());
        System.out.println(courses.size());
        System.out.println(getOrphanCoursesCount());

        System.out.println(getCoursesCount(SemesterName.Fall));
        System.out.println(getCoursesCount(SemesterName.Spring));
        System.out.println(getCoursesCount(SemesterName.Summer));
    }
}
