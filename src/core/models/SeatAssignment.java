package cms.core.models;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class SeatAssignment {
    private String instructorId;
    private String courseId;
    private Integer capacity;

    public SeatAssignment(String courseId, String instructorId, Integer capacity){
        this.setCourseId(courseId);
        this.setInstructorId(instructorId);
        this.setCapacity(capacity);
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
