package cms.importer;

import cms.core.models.Course;
import cms.core.models.Instructor;
import cms.core.models.SeatAssignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class AssignmentsImport
        extends BaseImport{

    Map<String, HashMap<String, Integer>> seatAssigmentsMap; // Dictionary of CourseID => [InstructorID, SeatCount]
    private List<Course> coursesCatalog;
    private List<Instructor> instructors;

    public AssignmentsImport(String importFilePath, List<Course> courses, List<Instructor> instructors) {
        super(importFilePath);

        this.coursesCatalog = courses;
        this.instructors = instructors;
    }

    @Override
    public void process() {
        try {
            List<String> lines = this.readFile();
            this.seatAssigmentsMap = new HashMap<>();

            for (String l : lines) {
                String[] values = l.split(",");
                if (values.length != 3) { continue; }

                // Extract PreRequisite Course Unique ID.
                String instructorId = values[0];
                String courseId = values[1];
                Integer seatCount = Integer.parseInt(values[2]);

                // Verify Instructors and courses exist.
                if (!this.courseExist(courseId) || !this.instructorExist(instructorId)){
                    continue;
                }

                if (this.seatAssigmentsMap.containsKey(courseId)){
                    boolean exist = false;
                    for (Map.Entry<String, Integer> kvp: this.seatAssigmentsMap.get(courseId).entrySet()) {
                        if (kvp.getKey().equals(instructorId)){
                            kvp.setValue(kvp.getValue() + seatCount);
                            exist = true;
                            break;
                        }
                    }

                    if (!exist){
                        this.seatAssigmentsMap.get(courseId).put(instructorId, seatCount);
                    }
                }else{
                    this.seatAssigmentsMap.put(courseId, new HashMap<String, Integer>());
                    this.seatAssigmentsMap.get(courseId).put(instructorId, seatCount);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Map<String, List<SeatAssignment>> getSeatAssignmentMap() {
        Map<String, List<SeatAssignment>> assignmentsPerCourse = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Integer>> courseMap: this.seatAssigmentsMap.entrySet()) {
            String courseId = courseMap.getKey();
            assignmentsPerCourse.put(courseId, new ArrayList<SeatAssignment>());

            for (Map.Entry<String, Integer> kvp: courseMap.getValue().entrySet()) {
                assignmentsPerCourse.get(courseId).add(new SeatAssignment(courseId, kvp.getKey(), kvp.getValue()));
            }
        }

        return assignmentsPerCourse;
    }

    // Helper methods.
    private boolean courseExist(String courseId){
        return this.getCourse(courseId) != null;
    }

    private Course getCourse(String courseId){
        for (Course c: this.coursesCatalog) {
            if (c.getCourseId().equals(courseId)){ return c; }
        }

        return null;
    }

    private boolean instructorExist(String courseId){
        return this.getInstructor(courseId) != null;
    }

    private Instructor getInstructor(String instructorId){
        for (Instructor i: this.instructors) {
            if (i.getUUID().equals(instructorId)){ return i; }
        }

        return null;
    }

}
