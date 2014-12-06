package com.restotesis.mozo.networking;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Conexion {
	private static Conexion mInstance = null;
	private RequestQueue mRequestQueue;
	private static String user;
	private static String password;

	private Conexion(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static Conexion getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new Conexion(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		return this.mRequestQueue;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Conexion.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Conexion.password = password;
	}

	public static Map<String, String> createBasicAuthHeader() {
		Map<String, String> headerMap = new HashMap<String, String>();

		String credentials = user + ":" + password;
		String encodedCredentials = Base64.encodeToString(
				credentials.getBytes(), Base64.NO_WRAP);
		headerMap.put("Authorization", "Basic "
				+ encodedCredentials);
		System.out.println(headerMap.toString());
		return headerMap;
	}

}
