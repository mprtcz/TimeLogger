package timelogger.mprtcz.com.timelogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.interfaces.Synchrotron;
import timelogger.mprtcz.com.timelogger.record.service.RecordService;
import timelogger.mprtcz.com.timelogger.record.service.RecordSyncService;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.task.service.TaskSyncService;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

public class SyncingActivity extends AppCompatActivity implements Synchrotron {
    private static final String TAG = "SyncingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing);
        this.synchronizeTasks();
    }

    @Override
    public void synchronizeTasks() {
        TaskSyncService taskSyncService = TaskService.getInstance(this).getTaskSyncService();
        try {
            taskSyncService.synchronizeActivities(this);
        } catch (Exception e) {
            Log.e(TAG, "exception during task sync");
            e.printStackTrace();
            UiUtils.messageBox(this, "processSynchronization()", e.toString());
        }
    }

    @Override
    public void synchronizeRecords() {
        RecordSyncService recordSyncService = RecordService.getInstance(this).getRecordSyncService();
        try {
            recordSyncService.synchronizeRecords(this);
        } catch (Exception e) {
            e.printStackTrace();
            UiUtils.messageBox(this, "processSynchronization()", e.toString());
        }
    }

    @Override
    public void completeSynchronization() {
        Intent intent = new Intent(this, TasksListActivity.class);
        startActivity(intent);
    }
}