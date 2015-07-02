package io.explod.android.emptyshell.module.components;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.App;
import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.Constants;
import io.explod.android.emptyshell.module.annotations.ForApplication;
import io.explod.android.emptyshell.module.annotations.NamedPreference;
import io.explod.android.emptyshell.network.ApiEndpoint;
import io.explod.android.emptyshell.network.AppAuthorizedService;
import io.explod.android.emptyshell.network.AppService;
import io.explod.android.emptyshell.network.mock.MockAppService;
import io.explod.android.emptyshell.util.prefs.EnumPreference;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

@Module(
        library = true,
        complete = false
)
public class NetworkModule {

    public static final String LOWER_CASE_WITH_UNDERSCORES = "LOWER_CASE_WITH_UNDERSCORES";

    private static final String TAG = "NetworkModule";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // 2015-03-04T14:15:23.039-07:00

    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", // 2015-03-04T14:15:23.039-07:00
            "yyyy-MM-dd" // 2014-03-31
    };

    private static final int HTTP_DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB

    private static final String HTTP_DISK_CACHE_DIR = "cache";

    private HashMap<ApiEndpoint, AppService> mServices = new HashMap<>(ApiEndpoint.values().length);

    private HashMap<ApiEndpoint, AppAuthorizedService> mAuthorizedServices = new HashMap<>(ApiEndpoint.values().length);

    @Provides
    AppService provideAppService(OkClient okClient,
                                 @Named(LOWER_CASE_WITH_UNDERSCORES) Gson gson,
//                                 final @NamedPreference(PreferenceModule.PREF_TOKEN) ObjectPreference<Token.Response> tokenPref,
                                 @NamedPreference(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref
    ) {
        ApiEndpoint endpoint = apiEndpointPref.get();
        AppService service = mServices.get(endpoint);

        if (service == null) {
            if (endpoint == ApiEndpoint.MOCK_MODE) {
                service = new MockAppService();
            } else {
                Converter converter = new GsonConverter(gson);
                final String userAgent = createUserAgent();
                RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint.serviceHost)
                        .setClient(okClient)
                        .setConverter(converter)
                        .setLogLevel(RestAdapter.LogLevel.BASIC)
                        .setRequestInterceptor(new RequestInterceptor() {
                            @Override
                            public void intercept(RequestFacade request) {
                                request.addHeader(Constants.HTTP_HEADER_USER_AGENT, userAgent);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON);
                                request.addHeader(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8);
                            }
                        });
                if (BuildConfig.DEBUG) {
                    builder.setLogLevel(RestAdapter.LogLevel.FULL);
                }
                RestAdapter adapter = builder.build();
                service = adapter.create(AppService.class);
            }
            mServices.put(endpoint, service);
        }
        return service;
    }

    @Provides
    AppAuthorizedService provideAppAuthorizedService(@ForApplication final Context context,
                                                     OkClient okClient,
                                                     @Named(LOWER_CASE_WITH_UNDERSCORES) Gson gson,
//                                                     final @NamedPreference(PreferenceModule.PREF_TOKEN) ObjectPreference<Token.Response> tokenPref,
                                                     @NamedPreference(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref) {
        ApiEndpoint endpoint = apiEndpointPref.get();
        AppAuthorizedService service = mAuthorizedServices.get(endpoint);

        if (service == null) {
            if (endpoint == ApiEndpoint.MOCK_MODE) {
                service = new MockAppService();
            } else {
                Converter converter = new GsonConverter(gson);
                final String userAgent = createUserAgent();
                RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint.serviceHost)
                        .setClient(okClient)
                        .setConverter(converter)
                        .setLogLevel(RestAdapter.LogLevel.BASIC)
                        .setRequestInterceptor(new RequestInterceptor() {
                            @Override
                            public void intercept(RequestFacade request) {
//                                Token.Response token = tokenPref.getObject();
//                                if (!Token.Response.isValid(token)) {
//                                    doLogout(context);
//                                    return;
//                                } else {
//                                    String authorization = Constants.HTTP_HEADER_AUTHORIZATION_BEARER + " " + token.accessToken;
//                                    request.addHeader(Constants.HTTP_HEADER_AUTHORIZATION, authorization);
//                                }
                                request.addHeader(Constants.HTTP_HEADER_USER_AGENT, userAgent);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON);
                                request.addHeader(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP);
                                request.addHeader(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8);
                            }
                        });
                if (BuildConfig.DEBUG) {
                    builder.setLogLevel(RestAdapter.LogLevel.FULL);
                }
                RestAdapter adapter = builder.build();
                service = adapter.create(AppAuthorizedService.class);
            }
            mAuthorizedServices.put(endpoint, service);
        }
        return service;
    }

    @Provides
    @Singleton
    OkClient provideOkClient(OkHttpClient okHttpClient) {
        return new OkClient(okHttpClient);
    }

    @Provides
    OkHttpClient providesOkHttpClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        return okHttpClient;
    }

    @Provides
    @Singleton
    Cache providesHttpResponseCache(@ForApplication Context context) {
        // Create an HTTP cache in the application cache directory.
        Cache cache = null;
        File cacheDir = new File(App.get(context).getCacheDir(), HTTP_DISK_CACHE_DIR);
        try {
            cache = new Cache(cacheDir, HTTP_DISK_CACHE_SIZE);
        } catch (IOException e) {
            Log.w(TAG, "Unable to create disk cache.", e);
        }
        return cache;
    }

    @Provides
    @Singleton
    @Named(LOWER_CASE_WITH_UNDERSCORES)
    public Gson provideLowerCaseGson() {
        return build(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    private Gson build(FieldNamingPolicy fieldNamingPolicy) {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateFormatter())
                .setFieldNamingPolicy(fieldNamingPolicy)
                .create();
    }

    private String createUserAgent() {
        return Constants.HTTP_USER_AGENT + " (Android " + Build.VERSION.SDK_INT + "; " + Build.MANUFACTURER + " " + Build.DEVICE + " " + Build.MODEL + ")";
    }


    private static class DateFormatter implements JsonDeserializer<Date>, JsonSerializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString() + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return null;
            }
            String formatted = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(src);
            return new JsonPrimitive(formatted);
        }
    }
}
