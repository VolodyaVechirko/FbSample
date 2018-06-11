package com.vechirko.fbsample.data;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class Errors {

    public static Consumer<Throwable> handle(Consumer<String> errorAction) {
        return throwable -> errorAction.accept(new ErrorParser(throwable).getUserFriendlyMessage());
    }

    static class ErrorParser {

        private static final String TAG = ErrorParser.class.getSimpleName();
        private Throwable throwable;

        public ErrorParser(Throwable throwable) {
            this.throwable = throwable;
        }

        public String getUserFriendlyMessage() {
            String humanReadableMessage = throwable.getMessage();
            if (humanReadableMessage == null && throwable.getCause() != null)
                humanReadableMessage = throwable.getCause().getMessage();
            if (throwable instanceof UnknownHostException)
                humanReadableMessage = "This action requires internet connection!";

            if (throwable instanceof HttpException || throwable.getCause() instanceof HttpException) {

                if (throwable.getCause() instanceof HttpException)
                    throwable = throwable.getCause();

                String errorJson = null;
                try {
                    errorJson = ((HttpException) throwable).response().errorBody().string();
                    JSONObject errorJsonObject = new JSONObject(errorJson);
                    humanReadableMessage = parseErrorJson(errorJsonObject);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    if (errorJson != null && !errorJson.equals(""))
                        humanReadableMessage = errorJson;
                }
            }

            if (humanReadableMessage == null)
                humanReadableMessage = "Network error";
            return humanReadableMessage;
        }

        private String parseErrorJson(JSONObject errorJsonObject) throws JSONException {
            ArrayList<String> errorsList = new ArrayList<>();
            if (!errorJsonObject.has("errors"))
                return errorJsonObject.getString("message");

            JSONObject errorsJsonList = errorJsonObject.getJSONObject("errors");
            Iterator<String> iterator = errorsJsonList.keys();
            while (iterator.hasNext()) {
                String errorArrayKey = iterator.next();
                JSONArray errorTypeArray = errorsJsonList.getJSONArray(errorArrayKey);
                for (int i = 0; i < errorTypeArray.length(); i++) {
                    errorsList.add(errorTypeArray.getString(i));
                }
            }

            return TextUtils.join("\n", errorsList);
        }
    }
}
