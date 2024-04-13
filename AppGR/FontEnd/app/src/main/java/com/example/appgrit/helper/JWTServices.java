package com.example.appgrit.helper;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

public class JWTServices {

    public static String GetUserId(String token){
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("userId");
        String userId = subscriptionMetaData.asString();
        return userId;
    }
}
