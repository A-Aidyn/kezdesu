package com.example.kezdesu;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserProfile {
    public String uid;
    public String name;
    public String email;

    public UserProfile() {
    }

    public UserProfile(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("uid", uid);
        res.put("name", name);
        res.put("email", email);
        return res;
    }
}
