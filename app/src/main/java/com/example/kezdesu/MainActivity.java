package com.example.kezdesu;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        // Hide the action bar
        ab.hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private EditText emailView;
    private EditText passwordView;

    /** Called when the user taps the Login button */
    public void login(View view) {
        //Validating the log in data

        emailView = (EditText) findViewById(R.id.reset_email_edittext);
        passwordView = (EditText) findViewById(R.id.password);

        boolean validationError = false;

        StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
        if (isEmpty(emailView)) {
            validationError = true;
            validationErrorMessage.append("an email");
        }
        if (isEmpty(passwordView)) {
            if (validationError) {
                validationErrorMessage.append(" and ");
            }
            validationError = true;
            validationErrorMessage.append("a password");
        }
        validationErrorMessage.append(".");

        if (validationError) {
            Toast.makeText(this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        //Setting up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Logging in...");
        dlg.show();

        passwordView.setError(null);

        mAuth.signInWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN IN", "signInWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null && user.isEmailVerified()) {
                                Query curUser = mDatabase.child("users").child(user.getUid()).child("email");
                                curUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Log.i(TAG, "Snapshot exists!!");
                                            Log.i(TAG, "email: " + snapshot.getValue());
                                        } else {
                                            updateDB(user.getUid(), emailView.getText().toString());
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d(TAG, error.getDetails());
                                    }
                                });
                                alertDisplayer("Login Sucessful", "Welcome!", false);
                            } else {
                                mAuth.signOut();
                                alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            alertDisplayer("Login Fail", "Authentication failed: " + task.getException().getMessage(), true);
                        }
                    }
                });
        dlg.cancel();
    }

    public void updateDB(String uid, String email) {
        Log.i(TAG, "New user with uid: [" + uid + "] was added into database!");
        UserProfile userProfile = new UserProfile("", email);
        HashMap<String, Object> user = new HashMap<>();
        user.put(uid, userProfile);
        mDatabase.child("users").setValue(user);
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void resetPassword(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}
