package com.isoftzone.yoappstore.util;

import okhttp3.RequestBody;

public class GsonHandler {
    public static RequestBody getRequestJsonByString(String jsonString) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
        return body;
    }

}
