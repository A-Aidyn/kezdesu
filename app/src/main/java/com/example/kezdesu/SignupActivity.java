package com.example.kezdesu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText passwordView;
    private EditText emailView;
    private EditText passwordAgainView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);

        mAuth = FirebaseAuth.getInstance();

        final Button signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
                if (isEmpty(emailView)||!isEmailValid(emailView.getText().toString())) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a valid KAIST email address");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }
                if (isEmpty(passwordAgainView)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your password again");
                }
                else {
                    if (!isMatching(passwordView, passwordAgainView)) {
                        if (validationError) {
                            validationErrorMessage.append(" and ");
                        }
                        validationError = true;
                        validationErrorMessage.append("the same password twice.");
                    }
                }
                validationErrorMessage.append(".");

                Log.i("EMAIL VALIDATION", isEmailValid(emailView.getText().toString()).toString());

                if (validationError) {
                    Toast.makeText(SignupActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                //Setting up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignupActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Signing up...");
                dlg.show();

                try {
                    // Reset errors.
                    emailView.setError(null);
                    passwordView.setError(null);

                    mAuth.createUserWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Sign Up", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("Sign Up", "Email sent.");
                                            }
                                        });

                                        mAuth.signOut();
                                        alertDisplayer("Account Created Successfully!", "Please verify your email before Login", false);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Sign up", "createUserWithEmail:failure", task.getException());
                                        alertDisplayer("Error Account Creation failed", "Account could not be created. " + task.getException().getMessage(), true);
                                    }
                                }
                            });

                } catch (Exception e) {
                    dlg.dismiss();
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText text1, EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }

    private Boolean isEmailValid(String email) {
        Log.d("EMAIL", email);
        return email.contains("@");
    } // TODO: check for kaist email

    private void alertDisplayer(String title,String message,final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
