package com.dinecrew.dinecrewbackend.utils;

import java.util.regex.Pattern;

public class ValidUtils {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public static boolean isValidUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9._-]{3,}$";
        return Pattern.compile(usernameRegex).matcher(username).matches();
    }
}
