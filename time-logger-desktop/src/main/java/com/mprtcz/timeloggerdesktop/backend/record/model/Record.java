package com.mprtcz.timeloggerdesktop.backend.record.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.utilities.DateAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@Setter
@DatabaseTable(tableName = "records")
@XmlRootElement(name="Record")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

    @DatabaseField(generatedId = true)
    @XmlTransient
    private Long id;

    @DatabaseField(canBeNull = false, foreign = true)
    @XmlTransient
    private Activity activity;

    @DatabaseField
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date startDateTime;

    @DatabaseField
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date endDateTime;

    @DatabaseField
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date creationDate;

    @DatabaseField
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date synchronizationDate;

    @DatabaseField
    private String uuId;

    @DatabaseField(canBeNull = false)
    private boolean active;

    public Record(LocalTime startTime,
                  LocalTime endTime,
                  LocalDate startDate,
                  LocalDate endDate,
                  Activity activity) {
        this.activity = activity;
        this.startDateTime = Date.from(
                LocalDateTime.of(startDate, startTime).atZone(ZoneId.systemDefault()).toInstant());
        this.endDateTime = Date.from(
                LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Record() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (active != record.active) return false;
        if (activity != null ? !activity.equals(record.activity) : record.activity != null) return false;
        return uuId != null ? uuId.equals(record.uuId) : record.uuId == null;

    }

    @Override
    public int hashCode() {
        int result = activity != null ? activity.hashCode() : 0;
        result = 31 * result + (uuId != null ? uuId.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", activity.getName=" + activity.getName() +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", creationDate=" + creationDate +
                ", synchronizationDate=" + synchronizationDate +
                ", active=" + active +
                ", uuId=" + uuId +
                '}';
    }
}
