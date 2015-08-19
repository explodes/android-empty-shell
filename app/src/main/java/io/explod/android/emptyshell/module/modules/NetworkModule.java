package io.explod.android.emptyshell.module.modules;

import android.app.Application;
import android.os.Build;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.BuildConfig;
import io.explod.android.emptyshell.Constants;
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

@Module
public class NetworkModule {

	private static final String TAG = "NetworkModule";

	private static final int HTTP_DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB

	private static final String HTTP_DISK_CACHE_DIR = "cache";

	private HashMap<ApiEndpoint, AppService> mServices = new HashMap<>(ApiEndpoint.values().length);

	private HashMap<ApiEndpoint, AppAuthorizedService> mAuthorizedServices = new HashMap<>(ApiEndpoint.values().length);

	@Provides
	AppService provideAppService(OkClient okClient,
								 Gson gson,
								 @Named(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref) {
		ApiEndpoint endpoint = apiEndpointPref.get();
		AppService service = mServices.get(endpoint);

		if (service == null) {
			if (endpoint == ApiEndpoint.MOCK_MODE) {
				service = new MockAppService();
			} else {
				final String userAgent = createUserAgent();
				RequestInterceptor interceptor = request -> {
					request.addHeader(Constants.HTTP_HEADER_USER_AGENT, userAgent);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON);
					request.addHeader(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8);
				};
				service = createService(okClient, gson, interceptor, endpoint.serviceHost, AppService.class);
			}
			mServices.put(endpoint, service);
		}
		return service;
	}

	@Provides
	AppAuthorizedService provideAppAuthorizedService(OkClient okClient,
													 Gson gson,
													 @Named(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref) {
		ApiEndpoint endpoint = apiEndpointPref.get();
		AppAuthorizedService service = mAuthorizedServices.get(endpoint);

		if (service == null) {
			if (endpoint == ApiEndpoint.MOCK_MODE) {
				service = new MockAppService();
			} else {
				final String userAgent = createUserAgent();
				RequestInterceptor interceptor = request -> {
//					Token.Response token = tokenPref.getObject();
//					if (!Token.Response.isValid(token)) {
//						doLogout(context);
//						return;
//					} else {
//						String authorization = Constants.HTTP_HEADER_AUTHORIZATION_BEARER + " " + token.accessToken;
//						request.addHeader(Constants.HTTP_HEADER_AUTHORIZATION, authorization);
//					}
					request.addHeader(Constants.HTTP_HEADER_USER_AGENT, userAgent);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON);
					request.addHeader(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP);
					request.addHeader(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8);
				};
				service = createService(okClient, gson, interceptor, endpoint.serviceHost, AppAuthorizedService.class);
			}
			mAuthorizedServices.put(endpoint, service);
		}
		return service;
	}

	private static <T> T createService(OkClient okClient,
									   Gson gson,
									   RequestInterceptor interceptor,
									   String host,
									   Class<? extends T> serviceClass) {
		Converter converter = new GsonConverter(gson);
		RestAdapter.Builder builder = new RestAdapter.Builder()
			.setEndpoint(host)
			.setClient(okClient)
			.setConverter(converter)
			.setLogLevel(RestAdapter.LogLevel.BASIC)
			.setRequestInterceptor(interceptor);
		if (BuildConfig.DEBUG) {
			builder.setLogLevel(RestAdapter.LogLevel.FULL);
		}
		RestAdapter adapter = builder.build();
		return adapter.create(serviceClass);
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
	Cache providesHttpResponseCache(Application context) {
		// Create an HTTP cache in the application cache directory.
		File cacheDir = new File(context.getCacheDir(), HTTP_DISK_CACHE_DIR);
		return new Cache(cacheDir, HTTP_DISK_CACHE_SIZE);
	}

	private String createUserAgent() {
		return Constants.HTTP_USER_AGENT + " (Android " + Build.VERSION.SDK_INT + "; " + Build.MANUFACTURER + " " + Build.DEVICE + " " + Build.MODEL + ")";
	}

}
