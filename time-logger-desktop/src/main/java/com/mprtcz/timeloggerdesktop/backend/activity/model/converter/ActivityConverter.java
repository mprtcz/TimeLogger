package com.mprtcz.timeloggerdesktop.backend.activity.model.converter;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.ActivityDto;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class ActivityConverter {

    public static Activity toEntity(ActivityDto activityDto) {
        Activity activity = new Activity();
        activity.setName(activityDto.getName());
        activity.setColor(activityDto.getColor());
        activity.setDescription(activityDto.getDescription());
        activity.setLastModified(activityDto.getLastModified());
        activity.setServerId(activityDto.getServerId());
        return activity;
    }

    public static ActivityDto toDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setName(activity.getName());
        activityDto.setColor(activity.getColor());
        activityDto.setDescription(activity.getDescription());
        activityDto.setLastModified(activity.getLastModified());
        activityDto.setServerId(activity.getServerId());
        return activityDto;
    }
}
