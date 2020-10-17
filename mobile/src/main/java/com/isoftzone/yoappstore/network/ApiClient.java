package com.isoftzone.yoappstore.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static ApiClient sharedInstance = new ApiClient();
    private ApiInterface apiService;
    public Enums.UrlType urlType = Enums.UrlType.API;
    public String otherUrl = "";

    public ApiInterface getApiService() {
        apiService = getClient().create(ApiInterface.class);
        return apiService;
    }

    /**
     * Private singleton constructor.
     */
/*
    private ApiClient() {
        apiService = getClient().create(ApiInterface.class);
    }
*/


    public static ApiClient getInstance() {
  /*      try {
            if (sharedInstance == null) {
                sharedInstance = new ApiClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return sharedInstance;
    }


    public void reset() {
        urlType = Enums.UrlType.API;
    }


    public void setRefreshTokenClient() {
        reset();
        urlType = Enums.UrlType.REFRESH_TOKEN;
    }

    public void setServerClient(Enums.UrlType setUrlType) {
        urlType = setUrlType;
    }


    public Retrofit getClient() {

        String path = "";

        switch (urlType) {
            /*case LOGIN:
                path = Constants.PROD_BASE_URL;  //   PROD_BASE_URL STAGING_BASE_URL
                break;
            case SIGNUP:
                path = Constants.TRAXPOD_BASE_URL;
                break;

            case REFRESH_TOKEN:
                path = Constants.PROD_BASE_URL;
                break;*/

            case OTHER:
                path = otherUrl;
                break;

            case API:  // http://www.chillybuz.com/api/
                path = Constants.APIMAIN_URL;
                break;
        }

        return new Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                // .client(trustCert(context))
                .build();


    }
}
