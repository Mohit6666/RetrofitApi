package com.firebaseconfigration.demo15_07_2020mohit.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebaseconfigration.demo15_07_2020mohit.R;
import com.firebaseconfigration.demo15_07_2020mohit.controller.api.APIClient;
import com.firebaseconfigration.demo15_07_2020mohit.controller.api.APIInterface;
import com.firebaseconfigration.demo15_07_2020mohit.model.registor.BodyParametersResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private EditText moblieET;
    private CountryCodePicker countryCodePicker;
    private Button submitBT;
    EditText firstNameET, lastNameET, emailET, passwordET, confirmPasswordET;
    private String deviceToken;
    private ProgressDialog progressDoalog;

  private ImageView arrowIM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    deviceToken = task.getResult().getToken();
                    Log.d("deviceToken", deviceToken);
                }
            }
        });


        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        progressBar = findViewById(R.id.progressBar);
        arrowIM = findViewById(R.id.arrowIM);

        progressDoalog = new ProgressDialog(SignUpActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setTitle("Please Wait...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        moblieET = findViewById(R.id.moblieET);
        countryCodePicker = findViewById(R.id.ccp);
        submitBT = findViewById(R.id.submitTV);
        countryCodePicker.registerCarrierNumberEditText(moblieET);

        arrowIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registorValidation();


            }
        });


    }



    public void registorValidation() {

        String name = firstNameET.getText().toString();
        String lastname = lastNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String phoneNumber = moblieET.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
        } else if (lastname.isEmpty()) {
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@") || !email.contains(".com")) {
            Toast.makeText(this, "Please enter  valid Email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Password should not be less than 5", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(moblieET.getText().toString())) {

            Toast.makeText(SignUpActivity.this, "Enter No.", Toast.LENGTH_SHORT).show();

        } else if (moblieET.getText().toString().replace("  ", "  ").length() != 10) {
            Toast.makeText(SignUpActivity.this, "Enter Correct No....", Toast.LENGTH_SHORT).show();
        } else {
            apiSigUpRequest(firstNameET.getText().toString(), lastNameET.getText().toString(),
                    emailET.getText().toString(), passwordET.getText().toString(),
                    "91", moblieET.getText().toString(), deviceToken, "native", "", "97854123456");
        }


    }

    private void apiSigUpRequest(String name, String lastname, String email, String password, String countryCode,
                                 String phoneNumber, String deviceToken, String socail, String refferalCode, String upid) {
        progressDoalog.create();
        progressBar.setVisibility(View.VISIBLE);
        APIInterface webClient = APIClient.getClientMethod(this).create(APIInterface.class);

        Call<BodyParametersResponse> webresuest = webClient.registorUser(name, lastname, email, password, countryCode, phoneNumber,
                deviceToken, socail, refferalCode, upid);
        webresuest.enqueue(new Callback<BodyParametersResponse>() {
            @Override
            public void onResponse(Call<BodyParametersResponse> call, Response<BodyParametersResponse> response) {

                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "success " + response.message(), Toast.LENGTH_SHORT).show();


                    if (response.body() != null) {

                        String jsonobj = response.toString();
                        String jsoresponse = response.body().toString();

                        Intent intent = new Intent(SignUpActivity.this, EnterOtpActivity.class);
                        intent.putExtra("number", countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                        startActivity(intent);

                    } else {
                        Log.i("@@@@@", "empty Response");
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().source().readUtf8Line());
                        Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BodyParametersResponse> call, Throwable t) {
                progressDoalog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("%%%%", "OnFailure:: " + t);
            }
        });
    }
   /* @Override
    public void onBackPressed() {
        return;
    }*/

}


