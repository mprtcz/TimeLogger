package com.mprtcz.timeloggerdesktop.frontend.customfxelements;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.Language;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MAX_VISIBLE_DAYS;
import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MIN_VISIBLE_DAYS;
import static com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor.getBackgroundOfColor;

/**
 * Created by mprtcz on 2017-01-09.
 */
@Getter
public class SettingsPopup extends JFXPopup {

    private AppSettings settings;
    private ResourceBundle messages;
    public static final double WIDTH = 300;

    public SettingsPopup(Region source, ResourceBundle messages, AppSettings settings) {
        this.settings = settings;
        this.messages = messages;
        this.setContent(generateContent());
        this.setSource(source);
    }

    private VBox generateContent() {
        Region languageContent = getLanguageContent();
        Region sliderContent = getSliderContent();
        Region buttonsContent = getButtonsContent();
        Region exportDataContent = getExportDataContent();
        VBox layoutVBox = new VBox(languageContent, sliderContent, exportDataContent, buttonsContent);
        layoutVBox.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        VBox.setMargin(languageContent, new Insets(20, 30, 5, 30));
        VBox.setMargin(sliderContent, new Insets(5, 30, 5, 30));
        VBox.setMargin(exportDataContent, new Insets(5, 30, 5, 30));
        VBox.setMargin(buttonsContent, new Insets(5, 30, 20, 30));
        layoutVBox.setPrefWidth(WIDTH);
        return layoutVBox;
    }

    JFXComboBox<String> comboBox;

    private Region getLanguageContent() {
        this.comboBox = new JFXComboBox<>();
        setLanguagesInChoiceBox();
        Label label = new Label(this.messages.getString("choose_language_label"));
        VBox languageVBox = new VBox(label, comboBox);
        languageVBox.setBackground(getBackgroundOfColor("white"));
        VBox.setMargin(comboBox, new Insets(2, 10, 10, 10));
        VBox.setMargin(label, new Insets(10, 10, 2, 10));
        JFXDepthManager.setDepth(languageVBox, 1);
        return languageVBox;
    }

    JFXSlider slider;
    JFXCheckBox graphicCheckBox;
    JFXCheckBox headersCheckBox;

    private Region getSliderContent() {
        this.slider = new JFXSlider();
        Label label = new Label(this.messages.getString("days_visible_label"));
        this.graphicCheckBox = new JFXCheckBox();
        this.headersCheckBox = new JFXCheckBox();
        graphicCheckBox.setText(this.messages.getString("is_canvas_visible_checkbox"));
        headersCheckBox.setText(this.messages.getString("are_headers_visible"));
        graphicCheckBox.setSelected(this.settings.isGraphicVisible());
        headersCheckBox.setSelected(this.settings.isHeadersVisible());
        slider.setMax(MAX_VISIBLE_DAYS);
        slider.setMin(MIN_VISIBLE_DAYS);
        slider.setValue(this.settings.getNumberOfVisibleDays());
        graphicCheckBox.setOnAction(event -> slider.setDisable(!graphicCheckBox.isSelected()));
        VBox languageVBox = new VBox(label, graphicCheckBox, slider, headersCheckBox);
        languageVBox.setBackground(getBackgroundOfColor("white"));
        VBox.setMargin(label, new Insets(20, 10, 5, 10));
        VBox.setMargin(graphicCheckBox, new Insets(5, 10, 5, 10));
        VBox.setMargin(slider, new Insets(5, 10, 5, 10));
        VBox.setMargin(headersCheckBox, new Insets(5, 10, 20, 10));
        languageVBox.setAlignment(Pos.CENTER);
        JFXDepthManager.setDepth(languageVBox, 1);
        return languageVBox;
    }

    private JFXButton confirmButton;

    private Region getButtonsContent() {
        this.confirmButton = new JFXButton(this.messages.getString("save_settings_button"));
        JFXButton cancelButton = new JFXButton(this.messages.getString("cancel_settings_button"));
        cancelButton.setOnAction(event -> {
            SettingsPopup.this.close();
            System.out.println(this.settings.toString());
        });
        DialogElementsConstructor.setStyleOfConfirmCancelButtons(confirmButton, cancelButton);
        HBox buttonsHBox = new HBox(confirmButton, cancelButton);
        HBox.setMargin(confirmButton, new Insets(10));
        HBox.setMargin(cancelButton, new Insets(10));
        JFXDepthManager.setDepth(buttonsHBox, 1);
        buttonsHBox.setBackground(getBackgroundOfColor("white"));
        return buttonsHBox;
    }

    JFXButton exportDataButton;
    JFXButton importDataButton;
    private Region getExportDataContent() {
        this.exportDataButton = new JFXButton(messages.getString("export_button"));
        this.importDataButton = new JFXButton(messages.getString("import_data_button"));
        Label exportDataLabel = new Label(messages.getString("export_label"));
        Label importDataLabel = new Label(messages.getString("import_data_label"));
        HBox exportDataHBox = new HBox(exportDataButton, exportDataLabel);
        HBox importDataHBox = new HBox(importDataButton, importDataLabel);
        exportDataLabel.prefHeightProperty().bind(this.getExportDataButton().heightProperty());
        importDataLabel.prefHeightProperty().bind(this.getImportDataButton().heightProperty());
        exportDataLabel.setAlignment(Pos.CENTER);
        importDataLabel.setAlignment(Pos.CENTER);
        exportDataButton.setBackground(getBackgroundOfColor(StyleSetter.ACCENT_COLOR));
        importDataButton.setBackground(getBackgroundOfColor(StyleSetter.ACCENT_COLOR));
        HBox.setMargin(exportDataButton, new Insets(10));
        HBox.setMargin(exportDataLabel, new Insets(10));
        HBox.setMargin(importDataButton, new Insets(10));
        HBox.setMargin(importDataLabel, new Insets(10));
        VBox overlayVBox = new VBox(exportDataHBox, importDataHBox);
        JFXDepthManager.setDepth(overlayVBox, 1);
        exportDataHBox.setBackground(getBackgroundOfColor("white"));
        importDataHBox.setBackground(getBackgroundOfColor("white"));

        return overlayVBox;
    }

    private void setLanguagesInChoiceBox() {
        List<Language> list = Arrays.asList(Language.values());
        for (Language language :
                list) {
            this.comboBox.getItems().add(language.getName());
        }
        this.comboBox.setValue(this.settings.getLanguage().getName());
    }

    public AppSettings getSettingsObject() {
        this.settings.setLanguage(Language.valueOf(this.comboBox.getValue().toUpperCase()));
        this.settings.setNumberOfVisibleDays((int) Math.round(this.slider.getValue()));
        this.settings.setGraphicVisible(this.graphicCheckBox.isSelected());
        this.settings.setHeadersVisible(this.headersCheckBox.isSelected());
        return this.settings;
    }
}
