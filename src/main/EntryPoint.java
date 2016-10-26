package cms.main;

import cms.core.enumerations.SemesterName;
import cms.core.models.*;
import cms.importer.*;

import java.io.File;
import java.util.*;

/**
 * Created by Leonardo on 9/24/2016.
 */
@SuppressWarnings("SpellCheckingInspection")
public class EntryPoint {

    private static String _filePath = "C:\\MEGA\\MEGAsync\\GA Tech\\CS6310\\Assignment 6\\Deliverables\\TestCase\\";
    //private static String _filePath = "";
    private static boolean _verbose = false;

    private static List<Semester> _semesters;
    private static List<Student> _students;
    private static List<Instructor> _instructors;
    private static List<Course> _courses;
    private static List<AcademicRecord> _records;

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals("-v")){ _verbose = true; }

        ProcessStudents();
        ProcessInstructors();
        ProcessCourses();
        ProcessAcademicRecords();

        PrintResults();

        if (_verbose) { PrintVerbose(); }
    }

    // Processing Methods.
    private static void ProcessStudents(){
        StudentImport studentImporter = new StudentImport(_filePath + "students.csv");
        studentImporter.process();

        _students = studentImporter.getStudents();
    }

    private static void ProcessCourses(){
        CourseImport courseImporter = new CourseImport(_filePath + "courses.csv");
        courseImporter.process();

        _semesters = courseImporter.getSemesters();
        _courses = courseImporter.getCourses();

        // Process Prerequisites.
        PreRequisitesImport preRequisitesImporter;
        Map<String, List<Course>> preRequisitesMapping = new HashMap<>();

        File prereqsFile = new File(_filePath + "prereqs.csv");
        if (prereqsFile.exists()){
            preRequisitesImporter = new PreRequisitesImport(_filePath + "prereqs.csv", _courses);
            preRequisitesImporter.process();
            preRequisitesMapping = preRequisitesImporter.getPreRequisitesMapping();
        }

        // Process SeatAssignments
        AssignmentsImport assignmentsImporter;
        Map<String, List<SeatAssignment>> seatAssignments = new HashMap<>();

        File assignmentsFile = new File(_filePath + "assignments.csv");
        if (assignmentsFile.exists()){
            assignmentsImporter = new AssignmentsImport(_filePath + "assignments.csv", _courses, _instructors);
            assignmentsImporter.process();
            seatAssignments = assignmentsImporter.getSeatAssignmentMap();
        }

        // ConsolidateData.
        for (Course c: _courses) {
            // Set its prerequisites.
            List<Course> prereqs = preRequisitesMapping.get(c.getCourseId());
            if (prereqs != null){
                c.setPreRequisites(prereqs);
            }

            // Set its seat assignment.
            List<SeatAssignment> seats = seatAssignments.get(c.getCourseId());
            if (seats != null){
                c.setSeatAssignment(seats);
            }
        }

        Collections.sort(_courses);
    }

    private static void ProcessInstructors(){
        InstructorImport instructorImport = new InstructorImport(_filePath + "instructors.csv");
        instructorImport.process();

        _instructors = instructorImport.getInstructors();
    }

    private static void ProcessAcademicRecords(){
        AcademicRecordImport recordImporter = new AcademicRecordImport(_filePath + "records.csv", _semesters, _students, _instructors);
        recordImporter.process();

        _records = recordImporter.getRecords();
    }

    // Analysis Methods.
    private static int getOrphanStudentsCount(){
        List<Student> orphans = new ArrayList<>();

        for (Student s: _students) {
            boolean exists = false;
            for (AcademicRecord r: _records) {
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

        for (Instructor i: _instructors) {
            boolean exists = false;
            for (AcademicRecord r: _records) {
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

        for (Course c: _courses) {
            boolean exists = false;
            for (AcademicRecord r: _records) {
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
        for (int i = 0; i < _semesters.size(); i++){
            if (_semesters.get(i).getSemesterName() == sname){
                return _semesters.get(i).getCurrentCourses().size();
            }
        }

        return 0;
    }

    // Print Methods.
    private static void PrintResults(){

        System.out.println(_records.size());
        System.out.println(_students.size());
        System.out.println(getOrphanStudentsCount());
        System.out.println(_instructors.size());
        System.out.println(getOrphanInstructorsCount());
        System.out.println(_courses.size());
        System.out.println(getOrphanCoursesCount());

        System.out.println(getCoursesCount(SemesterName.Fall));
        System.out.println(getCoursesCount(SemesterName.Spring));
        System.out.println(getCoursesCount(SemesterName.Summer));
    }

    private static void PrintVerbose(){
        if (_verbose){
            System.out.println("\nStudents extracted: " + _students.size());
            for (Student s: _students) {
                System.out.println(s.toString());
            }

            int count = 0;
            for (Semester s: _semesters) {
                count += s.getCurrentCourses().size();
            }

            System.out.println("\nCourses extracted (Union between Semesters): " + count);
            System.out.println("Unique Courses extracted: " + _courses.size());
            for (Course c: _courses) {
                System.out.println(c.toString());
            }

            System.out.println("\nInstructors extracted: " + _instructors.size());
            for (Instructor i: _instructors) {
                System.out.println(i.toString());
            }

            System.out.println("\nRecords extracted: " + _records.size());

            for (AcademicRecord a: _records) {
                System.out.println(a.toString());
            }
        }
    }
}
