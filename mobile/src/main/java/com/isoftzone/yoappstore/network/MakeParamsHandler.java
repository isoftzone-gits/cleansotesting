package com.isoftzone.yoappstore.network;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MakeParamsHandler {

    public static RequestBody getRequestBody(String request) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), request);
        return requestBody;
    }

}
