package cms.core.models;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class WorkSchedule {

    private String day;
    private Integer fromHour;
    private Integer toHour;

    public WorkSchedule(){}

    public String getDay(){
        return this.day;
    }

    public Integer getFromHour(){
        return this.fromHour;
    }

    public Integer getToHour(){
        return this.toHour;
    }
}
