package io.explod.android.emptyshell;

import io.explod.android.emptyshell.network.ApiEndpoint;

public interface Constants {

	ApiEndpoint DEFAULT_ENDPOINT = ApiEndpoint.DEVELOPMENT;

	// HTTP Headers
	String HTTP_HEADER_AUTHORIZATION = "Authorization";
	String HTTP_HEADER_AUTHORIZATION_BEARER = "Bearer";
	String HTTP_HEADER_USER_AGENT = "User-Agent";
	String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	String HTTP_HEADER_ACCEPT = "Accept";
	String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	String HTTP_HEADER_ACCEPT_CHARSET = "Accept-Charset";

	String HTTP_USER_AGENT = "Learner/" + BuildConfig.VERSION_CODE;
	String HTTP_MIME_JSON = "application/json";
	String HTTP_ACCEPT_ENCODING_GZIP = "compress, gzip";
	String HTTP_ACCEPT_CHARSET_UTF8 = "utf-8";

}
