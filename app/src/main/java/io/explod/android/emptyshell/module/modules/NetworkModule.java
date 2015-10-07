package io.explod.android.emptyshell.module.modules;

import android.app.Application;
import android.os.Build;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.explod.android.emptyshell.Constants;
import io.explod.android.emptyshell.network.ApiEndpoint;
import io.explod.android.emptyshell.network.AppAuthorizedService;
import io.explod.android.emptyshell.network.AppService;
import io.explod.android.emptyshell.network.mock.MockAppService;
import io.explod.android.emptyshell.util.prefs.EnumPreference;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class NetworkModule {

	private static final int HTTP_DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB

	private static final String HTTP_DISK_CACHE_DIR = "cache";

	private HashMap<ApiEndpoint, AppService> mServices = new HashMap<>(ApiEndpoint.values().length);

	private HashMap<ApiEndpoint, AppAuthorizedService> mAuthorizedServices = new HashMap<>(ApiEndpoint.values().length);

	@Provides
	AppService provideAppService(OkHttpClient okHttpClient,
								 @Named(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref) {
		ApiEndpoint endpoint = apiEndpointPref.get();
		AppService service = mServices.get(endpoint);

		if (service == null) {
			if (endpoint == ApiEndpoint.MOCK_MODE) {
				service = new MockAppService();
			} else {
				final String userAgent = createUserAgent();
				Interceptor interceptor = chain -> {
					Request original = chain.request();
					Request request = original.newBuilder()
						.header(Constants.HTTP_HEADER_USER_AGENT, userAgent)
						.header(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON)
						.header(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON)
						.header(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP)
						.header(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8)
						.method(original.method(), original.body())
						.build();
					return chain.proceed(request);
				};
				service = createService(okHttpClient, interceptor, endpoint.serviceHost, AppService.class);
			}
			mServices.put(endpoint, service);
		}
		return service;
	}

	@Provides
	AppAuthorizedService provideAppAuthorizedService(OkHttpClient okHttpClient,
													 @Named(PreferenceModule.PREF_API_ENDPOINT) EnumPreference<ApiEndpoint> apiEndpointPref) {
		ApiEndpoint endpoint = apiEndpointPref.get();
		AppAuthorizedService service = mAuthorizedServices.get(endpoint);

		if (service == null) {
			if (endpoint == ApiEndpoint.MOCK_MODE) {
				service = new MockAppService();
			} else {
				final String userAgent = createUserAgent();
				Interceptor interceptor = chain -> {
					Request original = chain.request();
					Request.Builder requestBuilder = original.newBuilder();
//					Token.Response token = tokenPref.getObject();
//					if (!Token.Response.isValid(token)) {
//						doLogout(context);
//						return;
//					} else {
//						String authorization = Constants.HTTP_HEADER_AUTHORIZATION_BEARER + " " + token.accessToken;
//						requestBuilder.header(Constants.HTTP_HEADER_AUTHORIZATION, authorization);
//					}
					Request request = requestBuilder.header(Constants.HTTP_HEADER_USER_AGENT, userAgent)
						.header(Constants.HTTP_HEADER_ACCEPT, Constants.HTTP_MIME_JSON)
						.header(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_MIME_JSON)
						.header(Constants.HTTP_HEADER_ACCEPT_ENCODING, Constants.HTTP_ACCEPT_ENCODING_GZIP)
						.header(Constants.HTTP_HEADER_ACCEPT_CHARSET, Constants.HTTP_ACCEPT_CHARSET_UTF8)
						.method(original.method(), original.body())
						.build();

					return chain.proceed(request);
				};
				service = createService(okHttpClient, interceptor, endpoint.serviceHost, AppAuthorizedService.class);
			}
			mAuthorizedServices.put(endpoint, service);
		}
		return service;
	}

	private static <T> T createService(OkHttpClient okHttpClient,
									   Interceptor interceptor,
									   String host,
									   Class<? extends T> serviceClass) {
		// add the auth / header interceptor as the first interceptor
		okHttpClient.interceptors().add(0, interceptor);
		// build the retrofit service
		return new Retrofit.Builder()
			.baseUrl(host)
			.client(okHttpClient)
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(serviceClass);
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
