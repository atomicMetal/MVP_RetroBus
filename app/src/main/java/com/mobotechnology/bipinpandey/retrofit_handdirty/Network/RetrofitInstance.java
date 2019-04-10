package com.mobotechnology.bipinpandey.retrofit_handdirty.Network;

import com.mobotechnology.bipinpandey.retrofit_handdirty.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.myjson.com/";
    private static TimeUnit timeUnit = TimeUnit.SECONDS;

    private static int HTTP_TIMEOUT = 30; // Seconds by default


    // Cache size for the OkHttpClient
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String WS_SCHEME = "http://";
    private static final String WS_PREFIX_DOMAIN = "deardhruv.com";
    private static final String WS_HOSTNAME = "/";
    private static final String WS_SUFFIX_FOLDER = "api/";

    private static String API_BASE_URL = WS_SCHEME
            + WS_PREFIX_DOMAIN
            + WS_HOSTNAME
            + WS_SUFFIX_FOLDER;
    private static OkHttpClient httpClient;
    private static Retrofit.Builder builder;
    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getRetrofitInstance() {

        /*File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);*/

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }

        httpClient = okBuilder
                .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .build();

        builder = new Retrofit.Builder();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        retrofit = builder
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build();





        /*if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }*/
        return retrofit;
    }
}