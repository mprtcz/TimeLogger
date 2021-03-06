package timelogger.mprtcz.com.timelogger.graphics.controllers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.joda.time.DateTime;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import timelogger.mprtcz.com.timelogger.task.model.HoursDataService;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

/**
 * Created by Azet on 2017-01-21.
 */

public class GraphicController {
    private static final String TAG = "GraphicController";
    private boolean areHeadersEnabled = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;
    private static final int MAX_CELL_HEIGHT = 40;
    public static final int HEADER_FONT_SIZE = 20;

    @Setter
    private int visibleDays = 10;
    private int maxDays;
    private int basicCellHeight = 10;
    private int headerHeight = 0;
    private int cellWidth = 0;
    private int leftLegendWidth = 0;
    @Getter
    private DateTime earliestDay;
    @Getter
    private DateTime latestDay;

    private Paint paint;

    private HoursDataService.Hour[][] trimmedHourArray;
    private HoursDataService.Hour[][] hoursArray;

    public GraphicController(HoursDataService.Hour[][] hoursArray) {
        this.hoursArray = hoursArray;
        setEarliestAndLatestDay();
        this.maxDays = hoursArray.length;
        this.paint = new Paint();
    }

    private void setEarliestAndLatestDay() {
        for (int i = 0; i < this.hoursArray[0].length; i++) {
            if(this.hoursArray[0][i] != null) {
                this.earliestDay = this.hoursArray[0][i].getDatetime();
                break;
            }
        }
        int lastRowIndex = this.hoursArray.length - 1;
        for (int i = 0; i < this.hoursArray[lastRowIndex].length; i++) {
            if(this.hoursArray[lastRowIndex][i] != null) {
                this.latestDay = this.hoursArray[lastRowIndex][i].getDatetime();
                break;
            }
        }
    }

    public int getMaxDays() {
        LogWrapper.d(TAG, "maxDays = " +maxDays);
        return this.maxDays;
    }

    public void drawArrayOnCanvas(Canvas canvas) {
        LogWrapper.d(TAG, "drawArrayOnCanvas");
        LogWrapper.d(TAG, "Hours array = " + Arrays.deepToString(this.hoursArray));

        int xOffset = 0;
        int yOffset = 0;
        this.cellWidth = canvas.getWidth() / 24;

        this.setTrimmedArray(hoursArray);
        basicCellHeight = canvas.getHeight() / this.trimmedHourArray.length;
        if (basicCellHeight > MAX_CELL_HEIGHT) {
            basicCellHeight = MAX_CELL_HEIGHT;
        }

        if (areHeadersEnabled) {
            this.cellWidth = canvas.getWidth() / 27;
            xOffset = 2;
            yOffset = 1;
            leftLegendWidth = xOffset * cellWidth;
            headerHeight = yOffset * BASIC_CELL_HEIGHT;
            basicCellHeight = (canvas.getHeight() -
                    (yOffset * BASIC_CELL_HEIGHT)) / this.trimmedHourArray.length;
            if (basicCellHeight > MAX_CELL_HEIGHT) {
                basicCellHeight = MAX_CELL_HEIGHT;
            }
            drawHeader(canvas, cellWidth, xOffset);
        }

        for (int i = 0; i < this.trimmedHourArray.length; i++) {
            for (int j = 0; j < this.trimmedHourArray[i].length; j++) {
                drawArrayCellOnCanvas(canvas, this.trimmedHourArray[i][j], j, i, xOffset, yOffset);
            }
        }

    }

    private void drawHeader(Canvas canvas, int unit, int leftOffset) {
        int tick = 2;
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextSize(HEADER_FONT_SIZE);
        paint.setTypeface(Typeface.SANS_SERIF);
        for (int i = leftOffset; i < (24 + leftOffset); i = (i + tick)) {
            canvas.drawText(String.valueOf(i - leftOffset), unit * i, basicCellHeight, paint);
        }
    }

    private int getDrawDeltaDays(int arrayLength) {
        if(firstDayToDraw > 0 && lastDayToDraw > 0) {
            this.visibleDays = lastDayToDraw - firstDayToDraw;
        }
        if (visibleDays > arrayLength) {
            return 0;
        } else if(visibleDays < 1) {
            return arrayLength - 1;
        } else {
            return arrayLength - visibleDays;
        }
    }

    int firstDayToDraw;
    int lastDayToDraw;

    public void setDayRangeToDraw(int firstDay, int secondDay) {
        this.firstDayToDraw = firstDay;
        this.lastDayToDraw = secondDay;
    }

    private void setTrimmedArray(HoursDataService.Hour[][] hourArray) {
        LogWrapper.d(TAG, "hourArray = " + Arrays.deepToString(hourArray));
        this.trimmedHourArray = new HoursDataService.Hour[hourArray.length -
                getDrawDeltaDays(hourArray.length)][hourArray[0].length];
        int index = 0;
        for (int i = firstDayToDraw; i < lastDayToDraw; i++) {
            this.trimmedHourArray[index] = hourArray[i];
            index++;
        }
    }

    private void drawArrayCellOnCanvas(Canvas canvas, HoursDataService.Hour hour,
                                       int xCoordinate, int yCoordinate,
                                       int xOffset, int yOffset) {
        LogWrapper.d(TAG, "drawArrayCellOnCanvas");
        String color = determineCellColor(hour);

        paint.setColor(Color.parseColor(color));
        DrawingCoordinates coords = new DrawingCoordinates(xCoordinate, xOffset, yCoordinate, yOffset, paint);
        LogWrapper.d(TAG, "Coords to string = " + coords.toString());

        canvas.drawRect(coords.X0, coords.Y0, coords.width, coords.height, coords.paint);
        if (areHeadersEnabled && hour != null) {
            String dayString = hour.getDatetime().getDayOfMonth() + "." + getMonthValue(hour);
            paint.setColor(Color.parseColor("#000000"));
            paint.setTextSize(HEADER_FONT_SIZE);
            canvas.drawText(dayString, 0, (yCoordinate + 1) * basicCellHeight + (basicCellHeight), paint);
        }
    }

    private String getMonthValue(HoursDataService.Hour hour) {
        int month = hour.getDatetime().getMonthOfYear();
        if (month > 9) {
            return String.valueOf(month);
        } else {
            return "0" + String.valueOf(month);
        }
    }

    private String determineCellColor(HoursDataService.Hour hour) {
        if (hour == null) {
            return "#ffffff";
        }
        if (hour.getActivitiesDuringThisHour().size() > 0) {
            return hour.getActivitiesDuringThisHour().get(0).getColor();
        } else {
            return "#ffffff";
        }
    }

    private class DrawingCoordinates {
        float X0;
        float Y0;
        float width;
        float height;
        Paint paint;

        DrawingCoordinates(int xCoordinate, int xOffset,
                           int yCoordinate, int yOffset, Paint paint) {
            this.X0 = (cellWidth * xCoordinate) + (xOffset * cellWidth);
            this.Y0 = (basicCellHeight * yCoordinate) + (yOffset * BASIC_CELL_HEIGHT) + basicCellHeight;
            this.width = X0 + cellWidth;
            this.height = Y0 + basicCellHeight;
            this.paint = paint;
        }

        @Override
        public String toString() {
            return "DrawingCoordinates{" +
                    "X0=" + X0 +
                    ", Y0=" + Y0 +
                    ", width=" + width +
                    ", height=" + height +
                    ", paint=" + paint.getColor() +
                    '}';
        }
    }
}