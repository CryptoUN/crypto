package com.anzusdev.cryptoun;

import com.google.api.server.spi.Constant;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API.
 */
public class Constants {
	public static final String WEB_CLIENT_ID = "880095029109-1n00qprerku742mvaot83ddnvcnsc7u6.apps.googleusercontent.com";
	public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
	public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
	public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
	public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;
	public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
}
