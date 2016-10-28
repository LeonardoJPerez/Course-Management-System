package cms.main;

import cms.core.enumerations.CourseGrade;
import cms.core.enumerations.EnrollmentStatus;
import cms.core.enumerations.SemesterName;
import cms.core.models.*;
import cms.core.models.users.Administrator;
import cms.core.models.users.Instructor;
import cms.core.models.users.Student;
import cms.core.models.users.User;
import cms.core.services.AcademicRecordService;
import cms.core.services.CatalogService;
import cms.core.services.EnrollmentRequestService;
import cms.importer.*;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Leonardo on 9/24/2016.
 */
@SuppressWarnings("SpellCheckingInspection")
public class EntryPoint {

    private static String _filePath = "C:\\MEGA\\MEGAsync\\GA Tech\\CS6310\\Assignment 6\\Deliverables\\TestCase\\";
    //private static String _filePath = "";
    private static boolean _verbose = false;
    private static User admin = new Administrator("Leo", "Perez", "lperez42@gmail.com", null, "987");

    private static List<Semester> _semesters;
    private static List<Student> _students;
    private static List<Instructor> _instructors;
    private static List<Course> _courses;
    private static List<AcademicRecord> _records;
    private static List<EnrollmentRequest> _enrollmentRequests;

    private static Integer totalRequestCount = 0;
    private static Integer approveRequestCount = 0;
    private static Integer missingPrerequisitesRequestCount = 0;
    private static Integer noAvailableSeatsRequestCount = 0;
    private static Integer courseAlreadyTakenRequestCount = 0;

    private static CatalogService _courseCatalog = CatalogService.getInstance();
    private static AcademicRecordService _academicRecordService = AcademicRecordService.getInstance();
    private static EnrollmentRequestService _enrollmentRequestService = EnrollmentRequestService.getInstance(_courseCatalog, _academicRecordService);

    private static List<RequestDecisionResult> _decisionResults;

    public static void main(String[] args) {
        ProcessStudents();
        ProcessInstructors();
        ProcessCourses();
        ProcessAcademicRecords();

        ProcessEnrollmentRequests();
        PrintResults2();

        BeginInteractiveMode(args);

        if (args.length > 0 && args[0].equals("-v")){
            PrintVerbose();
        }
    }

    // Interactive
    private static void BeginInteractiveMode(String[] consoleArgs){
        String command = "";
        Scanner scan = new Scanner(System.in);
        do{
            System.out.print("$main:");
            command = scan.nextLine();

            if (command.length() == 0){
                System.out.print("Command not found.");
                continue;
            }

            String[] commandParts = command.split(",");
            switch (commandParts[0].trim()){
                // Display operations.
                case "display_requests":
                    PrintEnrollmentRequests();
                    break;
                case "display_seats":
                    PrintSeatAssignment();
                    break;
                case "display_records":
                    PrintAcademicRecords();
                    break;

                // Editing operations
                case "add_record":
                    CreateAcademicRecord(commandParts[1].trim(), commandParts[2].trim(), commandParts[3].trim(), commandParts[4].trim(), commandParts[5].trim());
                    break;
                case "add_seats":
                    break;
                case "check_request":
                    CheckEnrollmentRequests(commandParts[1].trim(), commandParts[2].trim());
                    break;
                default:
                    continue;
            }
        }while(!command.equals("quit"));
        System.out.print("stopping the command loop");
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

        // Seeding catalog.
        for (Semester s: _semesters) {
            _courseCatalog.addSemesterCourses(s);
        }

        _courses = _courseCatalog.getDistinctCourses();

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

        for (AcademicRecord r: _records) {
            _academicRecordService.insert(r);
        }
    }
    private static void ProcessEnrollmentRequests(){
        EnrollmentRequestImport requestImporter = new EnrollmentRequestImport(_filePath + "requests.csv", _semesters, _students);
        requestImporter.process();

        totalRequestCount = requestImporter.getTotalCount();
        _enrollmentRequests = requestImporter .getEnrollmentRequests();

        // Validate all enrollments.
        _decisionResults = new ArrayList<>();
        for (EnrollmentRequest e: _enrollmentRequests) {
            RequestDecisionResult result = _enrollmentRequestService.approve(e, admin);
            _decisionResults.add(result);

            switch (result.getReasonType()){
                case PrerequisitesNotMet:
                    missingPrerequisitesRequestCount++;
                    break;
                case NoSeatAvailable:
                    noAvailableSeatsRequestCount++;
                    break;
                case CourseTaken:
                    courseAlreadyTakenRequestCount++;
                    break;
                default:
                    approveRequestCount++;
                    break;
            }
        }
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
    private static void PrintResults1(){
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
    private static void PrintResults2(){
        System.out.println(totalRequestCount);
        System.out.println(approveRequestCount);
        System.out.println(missingPrerequisitesRequestCount);
        System.out.println(courseAlreadyTakenRequestCount);
        System.out.println(noAvailableSeatsRequestCount);
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
                System.out.println(a.toStringVerbose());
            }
        }
    }

    // Print Methods via command line.
    private static void PrintEnrollmentRequests(){
        for (EnrollmentRequest r:_enrollmentRequestService.getRequests(EnrollmentStatus.Approved)) {
            System.out.println(r.toString());
        }
    }
    private static void PrintSeatAssignment(){
        MessageFormat fmt = new MessageFormat("{0},  {1},  {2}");
        Object[] values;

        for (Course c: _courses) {
            Integer seats = _enrollmentRequestService.getAvailableSeats(c);
            values = new Object[]{ c.getCourseId(), c.getFullName(), seats == -1 ? 0: seats };
            System.out.println(fmt.format(values));
        }
    }
    private static void PrintAcademicRecords(){
        for (AcademicRecord r:_academicRecordService.getRecords()) {
            System.out.println(r.toString());
        }
    }

    // Modification Methods via command line.
    private static void CheckEnrollmentRequests(String studentId, String courseId){
        EnrollmentRequest request = _enrollmentRequestService.searchRequest(studentId, courseId);

        // Let's try to approve it.
        _enrollmentRequestService.approve(request, admin);

        if (request != null && request.getDecisionResult() != null){
            System.out.println(request.getDecisionResult().getMessage());
        }
    }
    private static void CreateAcademicRecord(String studentId, String courseId, String intructorId, String comments, String grade){
        Student student = getStudent(studentId);
        if (student == null){
            System.out.println("Student not found.");
            return;
        }

        Course course = _courseCatalog.getCourse(courseId);
        if (course == null){
            System.out.println("Course not found.");
            return;
        }

        Semester semester = _courseCatalog.getSemesterByCourse(courseId);
        if (semester == null){
            System.out.println("Course not found.");
            return;
        }

        Instructor instructor = getInstructor(intructorId);
        if (instructor == null){
            System.out.println("Instructor not found.");
            return;
        }

        if (grade == null || grade.length() > 1){
            System.out.println("Grade value invalid.");
            return;
        }
        CourseGrade gradeValue = null;
        switch (grade){
            case "A":
                gradeValue = CourseGrade.A;
                break;
            case "B":
                gradeValue = CourseGrade.B;
                break;
            case "C":
                gradeValue = CourseGrade.C;
                break;
            case "D":
                gradeValue = CourseGrade.D;
                break;
            case "F":
                gradeValue = CourseGrade.F;
                break;
            case "W":
                gradeValue = CourseGrade.W;
                break;
            default:
                System.out.println("Grade value invalid.");
                return;
        }

        AcademicRecord newRecord = new AcademicRecord(course, gradeValue, semester, student, instructor);
        newRecord.setComments(comments);

        _academicRecordService.insert(newRecord);
    }

    // Helpers
    private static Student getStudent(String studentId){
        for (Student s: _students) {
            if (s.getUUID().equals(studentId)){
                return s;
            }
        }

        return null;
    }

    private static Instructor getInstructor(String instructorId){
        for (Instructor i: _instructors) {
            if (i.getUUID().equals(instructorId)){
                return i;
            }
        }

        return null;
    }
}
