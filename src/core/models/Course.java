package cms.core.models;

import cms.core.enumerations.CourseType;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class Course {

    private String courseId;
    private String title;
    private String description;
    private CourseType type;
    private Instructor instructor;

    private List<Course> preRequisites;
    private List<SeatAssignment> seatAssignment;

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

        this.setPreRequisites(new ArrayList<Course>());
        this.seatAssignment = new ArrayList<>();
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

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public Instructor getInstructor(){
        return this.instructor;
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

    public Integer getAvailableSeats() {
        // TODO: Pull from SeatAssignment collection.
        throw new NotImplementedException("Needs to pull from SeatAssignment collection.");
    }

    public void addSeats(SeatAssignment seatAssignment) {
        this.seatAssignment.add(seatAssignment);
    }

    public Integer getMaxTotalSeats() {
        // TODO: Pull from SeatAssignment collection.
        throw new NotImplementedException("Needs to pull from SeatAssignment collection.");
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
}