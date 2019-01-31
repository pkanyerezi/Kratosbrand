package com.brand.Kratos.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackResponseLogin {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("display_name")
    @Expose
    private String displayName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}