package com.example.egor.litemessenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.egor.litemessenger.Models.ErrorTranslator;
import com.example.egor.litemessenger.Models.NetworkService;
import com.example.egor.litemessenger.Models.SessionManager;
import com.example.egor.litemessenger.POJO.ServerInformation;
import com.example.egor.litemessenger.Requests.RegisterUser;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private Button mButtonRegisterSubmit;
    private EditText mEditTextRegisterEmail;
    private EditText mEditTextRegisterLogin;
    private EditText mEditTextRegisterPassword;
    private Context context;
    private SQLiteHandler db;
    private SessionManager session;
    public static final String TAG = RegisterActivity.class.getSimpleName();
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();

        session = new SessionManager(context);
        mEditTextRegisterLogin = findViewById(R.id.editTextLogin);
        mEditTextRegisterEmail = findViewById(R.id.editTextEmail);
        mEditTextRegisterPassword = findViewById(R.id.editTextPassword);
        mButtonRegisterSubmit = findViewById(R.id.buttonSubmitRegister);

        db = new SQLiteHandler(getApplicationContext());
        mButtonRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditTextRegisterLogin.getText().toString().isEmpty() && !mEditTextRegisterEmail.getText().toString().isEmpty() && !mEditTextRegisterPassword.getText().toString().isEmpty()) {
                    RegisterUser mcu = NetworkService.getInstance().getRetrofit().create(RegisterUser.class);
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put("login", mEditTextRegisterLogin.getText().toString().trim());
                    requestMap.put("password", mEditTextRegisterPassword.getText().toString().trim());
                    requestMap.put("email", mEditTextRegisterEmail.getText().toString().trim());

                    Call<ServerInformation> createUserOnServer = mcu.createUser(requestMap);
                    createUserOnServer.enqueue(new Callback<ServerInformation>() {
                        @Override
                        public void onResponse(Call<ServerInformation> call, Response<ServerInformation> response) {
                            int error = response.body().getError();

                            if (error <= 0) {
                                String login = response.body().getUser().getLogin();
                                String email = response.body().getUser().getEmail();
                                String uid = response.body().getUid();
                                String date = response.body().getUser().getCreatedAt();
                                db.addUser(login, email, uid, date);
                                HashMap<String, String> map = db.getUserDetails();
                                Log.d(TAG, map.toString());
                                Toast.makeText(context, "Зарегистрирован", Toast.LENGTH_SHORT).show();
                                session.setLogin(true);
                                intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                String error_msg = response.body().getErrorMsg();
                                Log.d(TAG, error_msg);
                                Log.d(TAG, ErrorTranslator.translator(error_msg));
                            }

                        }


                        @Override
                        public void onFailure(Call<ServerInformation> call, Throwable t) {
                            Log.d(TAG, t.toString());
                        }
                    });

                }
                else
                {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
