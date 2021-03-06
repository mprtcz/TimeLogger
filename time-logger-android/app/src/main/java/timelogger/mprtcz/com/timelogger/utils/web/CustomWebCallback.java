package timelogger.mprtcz.com.timelogger.utils.web;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

import static timelogger.mprtcz.com.timelogger.utils.web.WebHandler.handleWebCallbackException;

/**
 * Created by mprtcz on 2017-01-30.
 */
public abstract class CustomWebCallback<T> implements Callback<T> {
    private static final String TAG = "CustomWebCallback";

    public abstract void onSuccessfulCall(Response<T> response) throws Exception;

    public CustomWebCallback() {}

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            LogWrapper.i(TAG, "Call successful, call = " +call);
            try {
                onSuccessfulCall(response);
            } catch (Exception e) {
                e.printStackTrace();
                handleWebCallbackException(call, e);
            }
        } else {
            WebHandler.handleBadCodeResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        handleWebCallbackException(call, throwable);
    }
}