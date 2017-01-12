package com.mprtcz.timeloggerdesktop.backend.settings.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mprtcz on 2017-01-10.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AppSettings {
    public static final int MAX_VISIBLE_DAYS = 15;
    public static final int MIN_VISIBLE_DAYS = 5;

    Language language;
    int numberOfVisibleDays;
    boolean isGraphicVisible;
    boolean headersVisible;

    public static AppSettings getDefaultInstance() {
        Language BASIC_LOCALE = Language.ENGLISH;
        int DAYS_VISIBLE = 10;
        boolean IS_GRAPHIC_VISIBLE = true;
        boolean ARE_HEADERS_VISIBLE = true;
        return new AppSettings(BASIC_LOCALE, DAYS_VISIBLE, IS_GRAPHIC_VISIBLE, ARE_HEADERS_VISIBLE);
    }
}
