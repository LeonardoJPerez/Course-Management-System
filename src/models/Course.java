package models;

import enumerations.CourseType;

import java.util.UUID;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class Course {

    private UUID courseId;
    private String title;
    private String description;
    private CourseType type;
    private Instructor instructor;

    public Course(String title, CourseType type, Instructor instructor){

        if (title == null){
            throw new IllegalArgumentException("Title cannot be null.");
        }

        if (instructor == null){
            throw new IllegalArgumentException("models.Instructor cannot be null.");
        }

        this.courseId = UUID.randomUUID();
        this.title = title;
        this.type = type;
        this.instructor = instructor;
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

    public UUID getCourseId(){
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
}