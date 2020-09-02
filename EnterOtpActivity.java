package com.firebaseconfigration.demo15_07_2020mohit.view;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.firebaseconfigration.demo15_07_2020mohit.R;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.FirebaseApp;
        import com.google.firebase.FirebaseException;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.PhoneAuthCredential;
        import com.google.firebase.auth.PhoneAuthProvider;

        import java.util.concurrent.TimeUnit;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

public class EnterOtpActivity extends AppCompatActivity {

    private TextView resendTV;
    private String number ,id;
    private   Button submitBt;
    private  EditText otpET;
    private FirebaseAuth mAuth;
    private ImageView arrowIM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        // FirebaseApp.initializeApp(this);

        mAuth= FirebaseAuth.getInstance();
        otpET=(EditText) findViewById(R.id.otpET);
        submitBt=(Button) findViewById(R.id.submitBt);
        resendTV=(TextView) findViewById(R.id.resendTV);
        arrowIM = findViewById(R.id.arrowIM);
        number = getIntent().getStringExtra("number");

        sendVerificationCode();
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otpET.getText().toString())) {
                    Toast.makeText(EnterOtpActivity.this, " Enter Otp", Toast.LENGTH_SHORT).show();
                } else if (otpET.getText().toString().replace(" ", "").length() != 6) {

                    Toast.makeText(EnterOtpActivity.this, " Enter Right Otp", Toast.LENGTH_SHORT).show();

                } else {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otpET.getText().toString().replace(" ", ""));
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        arrowIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

    }




    private void sendVerificationCode() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                /*   resendTV.setText("" + 1 / 1000);*/
                resendTV.setEnabled(false);
            }

            @Override
            public void onFinish() {

                resendTV.setText("Resend");
                resendTV.setEnabled(true);

            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        EnterOtpActivity.this.id=id;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(EnterOtpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(EnterOtpActivity.this, LoginActivity.class));
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            //   Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(EnterOtpActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


}



