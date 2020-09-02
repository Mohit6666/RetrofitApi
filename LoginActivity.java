package com.firebaseconfigration.demo15_07_2020mohit.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebaseconfigration.demo15_07_2020mohit.R;
import com.firebaseconfigration.demo15_07_2020mohit.controller.api.APIClient;
import com.firebaseconfigration.demo15_07_2020mohit.controller.api.APIInterface;
import com.firebaseconfigration.demo15_07_2020mohit.model.login.MainResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    private Button signUpBT;
    private Button loginTV;
    private EditText passwordLoginET;
    private EditText emailLoginET;


    public Context context;
    private  String deviceToken;
    private ProgressDialog progressDoalog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_login);

   /// device token Method
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    deviceToken = task.getResult().getToken();
                    Log.d("deviceToken", deviceToken);
                }
            }
        });

        signUpBT = findViewById(R.id.signUpBT);
        loginTV = findViewById(R.id.loginTV);
        emailLoginET = findViewById(R.id.emailLoginET);
        passwordLoginET = findViewById(R.id.passwordLoginET);

        progressBar = findViewById(R.id.progressBar);

        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please Wait...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation();


               /* loginMethod(emailLoginET.getText().toString(), passwordLoginET.getText().toString(),
                        "123", "010", "12345", "1");*/
            }
        });


        signUpBT = findViewById(R.id.signUpBT);
        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }


        });

    }

    private void loginValidation() {


        String email = emailLoginET.getText().toString();
        String password = passwordLoginET.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@") || !email.contains(".com")) {
            Toast.makeText(this, "Please enter  valid Email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Password should not be less than 5", Toast.LENGTH_SHORT).show();
        } else {

            // loginMethod(email,password,deviceToken);

            loginMethod(emailLoginET.getText().toString(), passwordLoginET.getText().toString(),
                    deviceToken, "native", "", "1");
        }

    }

    private void loginMethod(String email, String password, String deviceToken, String socialType, String soicalId, String userType) {

        progressDoalog.create();
        progressBar.setVisibility(View.VISIBLE);
        APIInterface webClient = APIClient.getClientMethod(LoginActivity.this).create(APIInterface.class);
        Call<MainResponse> webresuest = webClient.loginuser(email, password, deviceToken, socialType, soicalId, userType);

        webresuest.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {

                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        // Toast.makeText(LoginActivity.this, "success " + response.message(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(LoginActivity.this, " Login Failed", Toast.LENGTH_SHORT).show();

                        String jsonobj = response.toString();
                        String jsoresponse = response.body().toString();
                       // Toast.makeText(LoginActivity.this, "success " + response.message(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().source().readUtf8Line());
                       // Toast.makeText(LoginActivity.this, "success " + response.message(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(context, "No Login", Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "Failed unable data " + call.toString(), Toast.LENGTH_SHORT).show();

                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
            }


        });

    }
}


