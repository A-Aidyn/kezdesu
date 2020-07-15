package com.example.kezdesu;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserProfile {
    public String name;
    public String email;

    public UserProfile() {
    }

    public UserProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("email", email);
        return res;
    }
}
