package com.mprtcz.timeloggerdesktop.backend.activity.model;

import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
public class HoursData {
    private Logger logger = LoggerFactory.getLogger(HoursData.class);

    private List<Activity> allActivities;
    private List<Record> allRecords;
    private LocalDateTime earliest;
    private LocalDateTime latest;

    private List<Hour> hours = new ArrayList<>();

    public HoursData(List<Activity> allActivities) {
        this.allActivities = allActivities;
        this.getAllRecords();
        this.setExtremeRecords();
        this.createHoursList();
        this.fillHoursWithActivities();
    }

    private void getAllRecords() {
        this.allRecords = new ArrayList<>();
        this.allActivities.stream().filter(a -> a.getActivityRecords() != null)
                .forEach(a -> allRecords.addAll(a.getActivityRecords()));
    }

    private void setExtremeRecords() {
        Date earliestRecord = new Date(Long.MAX_VALUE);
        Date latestRecord = new Date(Long.MIN_VALUE);
        for (Record record : allRecords) {
            if (record.getStartDateTime().before(earliestRecord)) {
                earliestRecord = record.getStartDateTime();
            }
            if (record.getEndDateTime().after(latestRecord)) {
                latestRecord = record.getEndDateTime();
            }
        }

        this.earliest = LocalDateTime.ofInstant(earliestRecord.toInstant(), ZoneId.systemDefault());
        this.latest = LocalDateTime.ofInstant(latestRecord.toInstant(), ZoneId.systemDefault());

    }

    public HoursData.Hour[][] getHoursArray() {
        long dayDelta = calculateDayDelta(earliest, latest);
        int dayDeltaInt = Math.toIntExact(dayDelta);

        logger.info("dayDeltaInt = {}", dayDeltaInt);

        HoursData.Hour[][] hoursArray = new HoursData.Hour[dayDeltaInt + 1][24];

        for (HoursData.Hour hourObject : this.getHours()) {
            int hour = hourObject.getDatetime().getHour();
            int objectsDayDelta = Math.toIntExact(calculateDayDelta(earliest, hourObject.getDatetime()));

            hoursArray[objectsDayDelta][hour] = hourObject;
        }

        for (int i = 0; i < hoursArray.length; i++) {
            for (int j = 0; j < hoursArray[i].length; j++) {
                if (hoursArray[i][j] == null) {
                    logger.debug("void");
                } else {
                    logger.debug(hoursArray[i][j].getDatetime().toString());
                }
            }
        }
        return hoursArray;
    }

    private long calculateDayDelta(LocalDateTime earliest, LocalDateTime current) {
        LocalDateTime earliestModulus = earliest;
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHour());
        return earliestModulus.until(current, DAYS);
    }

    private void createHoursList() {
        long hoursDelta = earliest.until(latest, ChronoUnit.HOURS);
        System.out.println("hoursDelta = " + hoursDelta);
        LocalDateTime currentHour = earliest;
        this.hours = new ArrayList<>();
        for (int i = 0; i < hoursDelta; i++) {
            Hour hour = new Hour(currentHour);
            this.hours.add(hour);
            currentHour = currentHour.plusHours(1L);
        }
    }

    private void fillHoursWithActivities() {
        for (Record record : this.allRecords) {
            LocalDateTime start = LocalDateTime.ofInstant(record.getStartDateTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(record.getEndDateTime().toInstant(), ZoneId.systemDefault());
            long duration = start.until(end, ChronoUnit.HOURS);
            int position = getListPositionOfSpecificHour(start);
            for (int i = 0; i < duration; i++) {
                Hour hour = this.hours.get(position);
                hour.getActivitiesDuringThisHour().add(record.getActivity());
                position++;
            }
        }
    }

    @Getter
    @EqualsAndHashCode
    public static class Hour {
        List<Activity> activitiesDuringThisHour;
        LocalDateTime datetime;

        public Hour(LocalDateTime datetime) {
            this.datetime = datetime;
            this.activitiesDuringThisHour = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "\nHour{\n" +
                    ", datetime=" + datetime +
                    ", num of activities =" + activitiesDuringThisHour.size() +
                    "activitiesDuringThisHour=" + activitiesDuringThisHour +
                    "}";
        }
    }

    private int getListPositionOfSpecificHour(LocalDateTime hourToFind) {
        for (Hour hour :
                this.hours) {
            if (hourToFind.equals(hour.getDatetime())) {
                return this.hours.indexOf(hour);
            }
        }
        return -1;
    }
}
