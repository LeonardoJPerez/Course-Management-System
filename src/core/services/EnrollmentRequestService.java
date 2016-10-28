package cms.core.services;

import cms.core.enumerations.CourseGrade;
import cms.core.enumerations.EnrollmentStatus;
import cms.core.enumerations.ReasonType;
import cms.core.models.AcademicRecord;
import cms.core.models.Course;
import cms.core.models.EnrollmentRequest;
import cms.core.models.RequestDecisionResult;
import cms.core.models.users.User;

import java.util.*;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class EnrollmentRequestService {

    private static EnrollmentRequestService _instance;
    private static Queue<EnrollmentRequest> pendingRequestQueue;
    private static Queue<EnrollmentRequest> deniedRequestQueue;
    private static Queue<EnrollmentRequest> approvedRequestQueue;

    private static CatalogService _catalog;
    private static AcademicRecordService _academicRecordService;

    private EnrollmentRequestService(CatalogService catalog, AcademicRecordService recordService) {
        this.pendingRequestQueue = new ArrayDeque<>();
        this.deniedRequestQueue = new ArrayDeque<>();
        this.approvedRequestQueue = new ArrayDeque<>();

        this._catalog = catalog;
        this._academicRecordService = recordService;
    }

    public static EnrollmentRequestService getInstance(CatalogService catalog, AcademicRecordService recordService) {
        if (_instance == null) {
            _instance = new EnrollmentRequestService(catalog, recordService);
        }

        return _instance;
    }

    public RequestDecisionResult approve(EnrollmentRequest request, User approver){
        if (request == null){
            throw new IllegalArgumentException("Enrollment request cannot be null.");
        }

        if (request.getStatus() == EnrollmentStatus.Approved){
            return new RequestDecisionResult(ReasonType.AllRulesMet, "Course already approved.");
        }

        RequestDecisionResult reason = this.validateEnrollmentRequest(request);
        boolean isValid = reason.getReasonType() == ReasonType.AllRulesMet;
        if (isValid){
            request.setApprovedBy(approver.getUUID());
            request.setDateApproved(new Date());
            request.setStatus(EnrollmentStatus.Approved);

            approvedRequestQueue.add(request);
        }else if (reason.getReasonType() == ReasonType.NoSeatAvailable){
            // Add request to pending request to be processed once a seat becomes available.
            pendingRequestQueue.add(request);
        }else{
            deniedRequestQueue.add(request);
        }

        request.setDecisionResult(reason);

        return reason;
    }

    public void deny(EnrollmentRequest request){
        if (request == null){
            throw new IllegalArgumentException("Enrollment request cannot be null.");
        }

        request.setApprovedBy("");
        request.setDateApproved(null);
        request.setStatus(EnrollmentStatus.Denied);

        this.deniedRequestQueue.add(request);
    }

    public void addNewRequest(EnrollmentRequest request){
        if (request == null){
            throw new IllegalArgumentException("Enrollment request cannot be null.");
        }

        if (request.getStatus() == EnrollmentStatus.Approved || request.getStatus() == EnrollmentStatus.Denied){
            return;
        }

        this.pendingRequestQueue.add(request);
    }

    public Queue<EnrollmentRequest> getRequests(EnrollmentStatus status){
        if (status == null){
            throw new IllegalArgumentException("Enrollment status cannot be null.");
        }

        switch (status){
            case Approved:
                return approvedRequestQueue;
            case Denied:
                return deniedRequestQueue;
            case Pending:
                return pendingRequestQueue;
        }

        return null;
    }

    public RequestDecisionResult validateEnrollmentRequest(EnrollmentRequest request){
        if (request == null){
            throw new IllegalArgumentException("Enrollment request cannot be null.");
        }

        if (!this.checkSeatAssignment(request)){
            return new RequestDecisionResult(ReasonType.NoSeatAvailable, "no remaining seats available for the course at this time");
        }

        if (!this.checkClassNotTaken(request)){
            return new RequestDecisionResult(ReasonType.CourseTaken, "student has already taken the course with a grade of C or higher");
        }

        if (!this.checkPrerequisites(request)){
            return new RequestDecisionResult(ReasonType.PrerequisitesNotMet, "student is missing one or more prerequisites");
        }

        return new RequestDecisionResult(ReasonType.AllRulesMet, "request is valid");
    }

    public Integer getAvailableSeats(Course course) {
        Integer maxSeat = course.getMaxTotalSeats();
        if (maxSeat == -1){
            return maxSeat; // Course has no seat limit.
        }

        Integer seatCount = 0;
        for (EnrollmentRequest r: this.approvedRequestQueue) {
            if (r.getCourse().getCourseId().equals(course.getCourseId())){
                seatCount++;
            }
        }

        return maxSeat - seatCount;
    }

    public EnrollmentRequest searchRequest(String studentId, String courseId) {
        Queue<EnrollmentRequest> aggregate = new ArrayDeque();
        aggregate.addAll(approvedRequestQueue);
        aggregate.addAll(deniedRequestQueue);
        aggregate.addAll(pendingRequestQueue);

        for (EnrollmentRequest e: aggregate) {
            if (e.getStudent().getUUID().equals(studentId) &&
                    e.getCourse().getCourseId().equals(courseId)){
                return e;
            }
        }

        return null;
    }

    // Verification Methods
    private boolean checkPrerequisites(EnrollmentRequest request){
        List<AcademicRecord> studentRecords = _academicRecordService.getRecordsByStudent(request.getStudent().getUUID());
        Integer prereqCount = request.getCourse().getPreRequisites().size();
        Integer metCount = 0;
        for (Course c: request.getCourse().getPreRequisites()) {
            for (AcademicRecord r: studentRecords) {
                if (c.getCourseId().equals(r.getCourse().getCourseId())){
                    // Check passing grades.
                    if (!r.getGrade().equals(CourseGrade.F) && !r.getGrade().equals(CourseGrade.W)){
                        metCount++;
                    }
                }
            }
        }

        return prereqCount == metCount;
    }

    private boolean checkSeatAssignment(EnrollmentRequest request){
        Integer available = this.getAvailableSeats(request.getCourse());
        if (available == -1){
            return true;
        }else{
            return available > 0;
        }

    }

    private boolean checkClassNotTaken(EnrollmentRequest request){
        List<AcademicRecord> studentRecords = _academicRecordService.getRecordsByStudent(request.getStudent().getUUID());
        for (AcademicRecord r: studentRecords) {
            // If course id matches and the passing grade is either A, B or C. Return false.
            if (request.getCourse().getCourseId().equals(r.getCourse().getCourseId())){
                if (!r.getGrade().equals(CourseGrade.D) &&
                        !r.getGrade().equals(CourseGrade.F)&&
                            !r.getGrade().equals(CourseGrade.W)){
                    return false;
                }
            }
        }

        return true;
    }
}
