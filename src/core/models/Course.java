package cms.core.models;

import cms.core.enumerations.CourseType;
import cms.core.models.users.Instructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class Course
        implements Comparable {

    private String courseId;
    private String title;
    private String description;
    private CourseType type;
    private Instructor instructor;

    private List<Course> preRequisites;
    private List<SeatAssignment> seatAssignments;

    public Course(String title, CourseType type, Instructor instructor, String id){

        if (title == null){
            throw new IllegalArgumentException("Title cannot be null.");
        }

        /*if (instructor == null){
            throw new IllegalArgumentException("core.models.Instructor cannot be null.");
        }*/

        this.courseId = id;
        if (this.courseId == null){
            UUID _UUID =  randomUUID();
            this.courseId =  _UUID.toString();
        }

        this.title = title;
        this.type = type;
        this.instructor = instructor;
        this.preRequisites = new ArrayList<>();

        this.setPreRequisites(new ArrayList<Course>());
        this.seatAssignments = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 59).append(this.courseId).append(this.description).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Course)){ return false; }
        if (obj == this){ return true; }

        Course course = (Course) obj;
        return new EqualsBuilder().append(this.courseId, course.courseId).append(this.description, course.description).isEquals();
    }

    @Override
    public String toString() {
        MessageFormat fmt = new MessageFormat("[{0}] {1} - Type: {2} - PreReqs: {3} - Seat Capacity: {4} - # of Instructors: {5}");
        Object[] values = new Object[]{
                    this.getCourseId(),
                    this.getFullName(),
                    this.getCourseType(),
                    this.getPreRequisites().size(),
                    this.getMaxTotalSeats(),
                    this.getInstructors().size()};


        return fmt.format(values);
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public List<String> getInstructors(){
        List<String> instructors = new ArrayList<>();

        for (SeatAssignment s: this.seatAssignments)
        {
            instructors.add(s.getInstructorId());
        }

        return instructors;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getCourseId(){
        return this.courseId;
    }

    public CourseType getCourseType(){
        return this.type;
    }

    public String getFullName(){

        if (this.description != null){
            return this.title + ": " + this.description;
        }else{
            return this.title;
        }
    }

    public void setSeatAssignment(List<SeatAssignment> seats) {
        this.seatAssignments = seats;
    }

    public void addSeats(SeatAssignment seatAssignment) {
        this.seatAssignments.add(seatAssignment);
    }

    public Integer getMaxTotalSeats() {
        Integer count = 0;
        for (SeatAssignment s: this.seatAssignments){
            count += s.getCapacity();
        }

        // Return -1 to denote no seat limit for this course.
        return count == 0 ? -1 : count;
    }

    public List<Course> getPreRequisites() {
        return this.preRequisites;
    }

    public void setPreRequisites(List<Course> preRequisites) {
        this.preRequisites = preRequisites;
    }

    public void addPreRequisites(Course preRequisite) {
        if (preRequisite == null){
            return;
        }

        // TODO: Check if prereq already exist.

        this.preRequisites.add(preRequisite);
    }

    @Override
    public int compareTo(Object c) {
        Integer current = Integer.parseInt(this.getCourseId());
        Integer compareToId = Integer.parseInt(((Course)c).getCourseId());

        if (current.equals(compareToId)){ return 0; }
        return current > compareToId ? 1 : -1;

    }
}