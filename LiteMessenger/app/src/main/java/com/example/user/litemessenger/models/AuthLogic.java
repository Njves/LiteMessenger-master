package com.example.user.litemessenger.models;

public class AuthLogic {
    String login;
    String password;
    String rePassword;
    String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void attemptLogin(String login)
    {


    }
    private boolean loginValid(String login)
    {
        return login.length() > 4 && login.contains("");
    }
    private boolean passwordValid(String password)
    {
        return password.length() > 6;
    }
}
