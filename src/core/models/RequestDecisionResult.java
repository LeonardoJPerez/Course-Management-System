package cms.core.models;

import cms.core.enumerations.ReasonType;

/**
 * Created by Leonardo on 10/26/2016.
 */
public class RequestDecisionResult {

    private String message;
    private ReasonType reasonType;

    public RequestDecisionResult(ReasonType reasonType, String message){
        this.reasonType = reasonType;
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public ReasonType getReasonType(){
        return this.reasonType;
    }
}
