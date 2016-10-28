package cms.core.models;

import cms.core.enumerations.EnrollmentStatus;
import cms.core.models.users.Student;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class EnrollmentRequest {

    private Course course;
    private Semester semester;
    private EnrollmentStatus status;
    private Date dateRequested;
    private Date dateApproved;
    private Student student;
    private String approvedBy;
    private RequestDecisionResult decisionResult;

    public EnrollmentRequest(Student student, Course course, Semester semester) {
        if (student == null){
            throw new IllegalArgumentException("Student ID missing.");
        }

        if (course == null){
            throw new IllegalArgumentException("Course object missing.");
        }

        if (semester == null){
            throw new IllegalArgumentException("Semester object missing.");
        }

        this.student = student;
        this.course = course;
        this.semester = semester;
        this.status = EnrollmentStatus.Pending;
        this.decisionResult = null;

        this.dateRequested = new Date();
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Semester getSemester() {
        return semester;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Date dateApproved) {
        if (dateApproved == null) {
            throw new IllegalArgumentException("Approval date must not be null.");
        }

        if (dateApproved.before(this.dateRequested)) {
            throw new IllegalArgumentException("Approval date must be later than the Requested date.");
        }
        this.dateApproved = dateApproved;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public RequestDecisionResult getDecisionResult() {
        return this.decisionResult;
    }

    public void setDecisionResult(RequestDecisionResult decision) {
        this.decisionResult = decision;
    }

    @Override
    public String toString() {
        MessageFormat fmt = new MessageFormat("{0},  {1},  {2},  {3}");
        Object[] values = new Object[]{this.getStudent().getUUID(), this.getStudent().getFullName(), this.getCourse().getCourseId(), this.getCourse().getFullName()};

        return fmt.format(values);
    }
}
