package com.fruitshop.constant;

public class ApiPath {
	public static final String AUTH =  "/auth/";
	public static final String LOGIN = AUTH + "login";
  public static final String REGISTER = AUTH +"register";
  public static final String VERIFICATION = AUTH + "verification";
	public static final String REFRESH_TOKEN = AUTH + "refresh-token";
	public static final String LOGOUT = AUTH + "logout";
	private static final String BASE_URL = "/api/";
	public static final String PUBLIC = BASE_URL + "public/";
	public static final String PRODUCT = BASE_URL + "products";
	public static final String CATEGORY = BASE_URL + "categorys";
	public static final String CART = BASE_URL + "carts";
	public static final String SHIPPING_INFORMATION = BASE_URL + "shipping-informations";
	public static final String ORDER = BASE_URL+ "orders";
	public static final String PAY = BASE_URL + "payments";
	public static final String USER = BASE_URL+ "users";
	public static final String REPORT = BASE_URL +"reports";
	public static final String DISCOUNT = BASE_URL +"discounts";
}
