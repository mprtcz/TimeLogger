package com.mprtcz.timeloggerdesktop.frontend.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor.getBackgroundOfColor;

/**
 * Created by mprtcz on 2017-01-07.
 */
@Getter
public class AddRecordPopup extends JFXPopup {

    private JFXDatePicker startDatePicker;
    private JFXDatePicker endDatePicker;
    private JFXDatePicker startTimePicker;
    private JFXDatePicker endTimePicker;
    private Label startTimeLabel;
    private Label endTimeLabel;
    private JFXButton okButton;
    private JFXButton closeButton;
    private Label summaryLabel;
    private Activity activity;
    private ResourceBundle messages;

    public static final int WIDTH = 300;

    public AddRecordPopup(Activity activity, LocalDateTime latestRecord, ResourceBundle messages) {
        this.messages = messages;
        this.activity = activity;
        this.initializeElements();
        this.addListeners();
        initializeTimeElements(latestRecord);
        this.setContent(this.createLayout());
    }

    private VBox createLayout() {
        Insets defaultInsets = new Insets(10);
        VBox startVBox = generateCardLayout(this.startTimePicker, this.startDatePicker, this.startTimeLabel);
        VBox endVBox = generateCardLayout(this.endTimePicker, this.endDatePicker, this.endTimeLabel);
        startVBox.setBackground(getBackgroundOfColor("white"));
        endVBox.setBackground(getBackgroundOfColor("white"));
        startVBox.setAlignment(Pos.CENTER);
        endVBox.setAlignment(Pos.CENTER);
        JFXDepthManager.setDepth(startVBox, 1);
        JFXDepthManager.setDepth(endVBox, 1);
        HBox buttonsHBox = new HBox(this.okButton, this.closeButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        HBox.setMargin(okButton, defaultInsets);
        HBox.setMargin(closeButton, defaultInsets);
        VBox vBox = new VBox(startVBox, endVBox, this.summaryLabel, buttonsHBox);
        VBox.setMargin(startVBox, defaultInsets);
        VBox.setMargin(endVBox, defaultInsets);
        VBox.setMargin(summaryLabel, defaultInsets);
        vBox.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        JFXDepthManager.setDepth(this, 1);
        vBox.setPrefWidth(WIDTH);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private static VBox generateCardLayout(JFXDatePicker timePicker, JFXDatePicker datepicker, Label label) {
        VBox vBox = new VBox(label, timePicker, datepicker);
        VBox.setMargin(label, new Insets(10));
        VBox.setMargin(timePicker, new Insets(10));
        VBox.setMargin(datepicker, new Insets(10));
        return vBox;
    }

    private void initializeElements() {
        this.startDatePicker = new JFXDatePicker();
        this.endDatePicker = new JFXDatePicker();
        this.startTimePicker = new JFXDatePicker();
        this.startTimePicker.setShowTime(true);
        this.endTimePicker = new JFXDatePicker();
        this.endTimePicker.setShowTime(true);
        this.okButton = new JFXButton(this.messages.getString("save_record_button"));
        this.closeButton = new JFXButton(this.messages.getString("cancel_button"));
        this.startTimeLabel = new Label(this.messages.getString("start_date_label"));
        this.endTimeLabel = new Label(this.messages.getString("end_date_label"));
        this.summaryLabel = new Label();
        this.summaryLabel.setVisible(false);
        setStyles();
    }

    private void setStyles() {
        this.okButton.setBackground(getBackgroundOfColor(StyleSetter.ACCENT_COLOR));
        this.closeButton.setBackground(getBackgroundOfColor(StyleSetter.GRAY_COLOR));
        this.startTimePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.endTimePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.startDatePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.endDatePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.summaryLabel.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        this.summaryLabel.setStyle(" -fx-padding: 10");
    }

    private void addListeners() {
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                AddRecordPopup.this.close();
            }
        });
        this.closeButton.setOnAction(event -> AddRecordPopup.this.close());
    }

    private void initializeTimeElements(LocalDateTime latestRecord) {
        DateTimeInitializer dateTimeInitializer = new DateTimeInitializer();
        dateTimeInitializer.getItemsList().add(startDatePicker);
        dateTimeInitializer.getItemsList().add(endDatePicker);
        dateTimeInitializer.getItemsList().add(startTimePicker);
        dateTimeInitializer.getItemsList().add(endTimePicker);

        dateTimeInitializer.initializeElements(getSummaryEventHandler(), latestRecord);
    }

    private <T extends Event> EventHandler<T> getSummaryEventHandler() {
        return event -> AddRecordPopup.this.updateSummary();
    }

    private void updateSummary() {
        this.summaryLabel.setVisible(true);
        this.summaryLabel.setBackground(getBackgroundOfColor("white"));
        this.summaryLabel.setTextAlignment(TextAlignment.RIGHT);
        JFXDepthManager.setDepth(this.summaryLabel, 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.activity.getName()).append("\n");
        stringBuilder.append("Start: ");
        stringBuilder.append(this.getFormattedDatetime(this.startDatePicker.getValue(), this.startTimePicker.getTime()));
        stringBuilder.append("\nEnd: ");
        stringBuilder.append(this.getFormattedDatetime(this.endDatePicker.getValue(), this.endTimePicker.getTime()));
        this.summaryLabel.setText(stringBuilder.toString());
    }

    private String getFormattedDatetime(LocalDate date, LocalTime time) {
        LocalDateTime datetime = LocalDateTime.of(date, time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        return datetime.format(formatter);
    }

    public RecordValidator.ValidationObject getObjectToValidate() {
        return new RecordValidator.ValidationObject(
                this.startTimePicker.getTime(),
                this.endTimePicker.getTime(),
                this.startDatePicker.getValue(),
                this.endDatePicker.getValue(),
                this.activity
        );
    }


}