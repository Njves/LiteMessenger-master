package com.example.egor.litemessenger.Models;

public class ErrorTranslator {
    private String errorUserAlreadyExisists;
    private String errorPasswordLess;
    private String errorUserLoginAlreadyExists;
    private String errorUserEmailAlreadyExists;
    private String errorUserEmailFieldBlank;
    private String errorUserFieldPasswordBlank;
    private String errorUserFieldLoginBlank;
    private String error_user_email_exists_invalid_chars;
    public static String translator(String error)
    {
        String translateError = "Ошибки отсутсвуют";
        switch (error)
        {
            case "error_user_login_already_exists":
            {
                translateError = "Пользователь с таким логином уже существует";
                break;
            }
            case "error_user_password_blank":
            {
                translateError = "Заполните поле пароля";
                break;
            }
            case "error_login_contains_invalid_chars":
            {
                translateError = "Логин содержит недопустимые символы";
                break;
            }
            case "error_user_email_already_exitsts":
            {
                translateError = "Пользователь с такой почтой уже существует";
                break;
            }
            case "error_user_login_blank":
            {
                translateError = "Пожалуйста, заполните поле логина";
                break;
            }
            case "error_user_email_blank":
            {
                translateError = "Пожалуйста, заполните поле почты";
                break;
            }
            case "error_user_email_exists_invalid_chars":
            {
                translateError = "Почта содержит недопустимые символы";
                break;
            }
        }
        return translateError;
    }
}
