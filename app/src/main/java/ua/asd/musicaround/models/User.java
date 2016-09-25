package ua.asd.musicaround.models;


import java.util.HashMap;
import java.util.Map;

public class User {
    //General map key
    public static final String KEY_USER_UID = "userUid";

    //User map key
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phone";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_AVATAR = "avatar";

    public String userUid;
    public String username;
    public String email;
    public String provider;
    public String phone;
    public String avatar;

    public User(String userUid, String username, String email, String provider, String phone, String avatar) {
        this.userUid = userUid;
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.phone = phone;
        this.avatar = avatar;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(KEY_USER_UID, userUid);
        userMap.put(KEY_USER_NAME, username);
        userMap.put(KEY_EMAIL, email);
        userMap.put(KEY_PHONE_NUMBER, phone);
        userMap.put(KEY_PROVIDER, provider);
        userMap.put(KEY_AVATAR, avatar);
        return userMap;
    }

    public static String getKeyUserUid() {
        return KEY_USER_UID;
    }

    public static String getKeyUserName() {
        return KEY_USER_NAME;
    }

    public static String getKeyEmail() {
        return KEY_EMAIL;
    }

    public static String getKeyPhoneNumber() {
        return KEY_PHONE_NUMBER;
    }

    public static String getKeyProvider() {
        return KEY_PROVIDER;
    }

    public static String getKeyAvatar() {
        return KEY_AVATAR;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
