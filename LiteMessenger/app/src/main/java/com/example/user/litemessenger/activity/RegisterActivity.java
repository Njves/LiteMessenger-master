package com.example.user.litemessenger.activity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.litemessenger.AppConfig;
import com.example.user.litemessenger.R;
import com.example.user.litemessenger.requests.IRegister;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private Button btnSubmitRegister;
    private Button btnGoToLogin;
    private EditText mEditTextEnterLogin;
    private EditText mEditTextEnterPassword;
    private EditText mEditTextEnterPasswordRe;
    private TextView mTextViewErrors;
    private SharedPreferences mSharedPreferences;
private String login, pass, answerHTTP;
private Gson gson = new GsonBuilder().create();
private Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(AppConfig.SERVER_AUTH_NAME).build();
private IRegister req = retrofit.create(IRegister.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnSubmitRegister = findViewById(R.id.buttonSubmitRegister);
        btnGoToLogin = findViewById(R.id.buttonToLoginScreen);
        mEditTextEnterLogin = findViewById(R.id.editTextLogin);
        mEditTextEnterPassword = findViewById(R.id.editTextPassword);
        mEditTextEnterPasswordRe = findViewById(R.id.editTextPassword_2);
        mTextViewErrors = findViewById(R.id.textViewErrors);

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String login = mEditTextEnterLogin.getText().toString();
String pass = mEditTextEnterPassword.getText().toString();
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("login", login);
                postDataParams.put("password", pass);
                Call<Object> call = req.performPostCall(postDataParams);
            call.enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
            }
        });

    }
}

