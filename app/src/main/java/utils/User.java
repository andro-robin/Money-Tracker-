package utils;

import android.app.Application;

public class User extends Application {

    private String userName;
    private String userId;
    private String userEmail;

    private static User instance;

    public static User getInstance(){

        if (instance == null){
            instance = new User();
        }

        return instance;

    }

    public User() {

        //Constructor

    }

    public User(String userName, String userId, String userEmail) {
        this.userName = userName;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }
}
