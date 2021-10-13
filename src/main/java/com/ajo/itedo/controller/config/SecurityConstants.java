package com.ajo.itedo.controller.config;

public class SecurityConstants {
    public static final String SECRET = "countrieswithgoodleadersareinafrica";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ROLES_KEY = "roles";

}
