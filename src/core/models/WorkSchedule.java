package cms.core.models;

/**
 * Created by Leonardo on 9/23/2016.
 */
public class WorkSchedule {

    private String day;
    private Integer fromHour;
    private Integer toHour;

    public WorkSchedule(){}


    // Setters / Getters
    public String getDay(){
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getFromHour(){
        return this.fromHour;
    }

    public void setFromHour(Integer fromHour) {
        this.fromHour = fromHour;
    }

    public Integer getToHour(){
        return this.toHour;
    }

    public void setToHour(Integer toHour) {
        this.toHour = toHour;
    }
}
