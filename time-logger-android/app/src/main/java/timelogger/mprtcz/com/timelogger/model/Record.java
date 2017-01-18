package timelogger.mprtcz.com.timelogger.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Azet on 2017-01-17.
 */
@Getter
@Setter
public class Record {

    private Long id;

    private Task task;

    private Date startDateTime;

    private Date endDateTime;

    public Record(Date startTime,
                  Date endTime,
                  Date startDate,
                  Date endDate,
                  Task task) {
        this.task = task;
        this.startDateTime = new Date(
                startDate.getYear(), startDate.getMonth(), startDate.getDay(),
                startTime.getHours(), startTime.getMinutes(), startTime.getSeconds());
        this.endDateTime = new Date(
                endDate.getYear(), endDate.getMonth(), endDate.getDay(),
                endTime.getHours(), endTime.getMinutes(), endTime.getSeconds()
        );
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", activityName=" + task.getName() +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
