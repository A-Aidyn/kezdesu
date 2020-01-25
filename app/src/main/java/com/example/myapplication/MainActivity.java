package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseInstallation;
import com.parse.LogInCallback;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
        setContentView(R.layout.activity_main);
    }


    private EditText usernameView;
    private EditText passwordView;

    /** Called when the user taps the Login button */
    public void login(View view) {
        //Validating the log in data

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);

        boolean validationError = false;

        StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
        if (isEmpty(usernameView)) {
            validationError = true;
            validationErrorMessage.append("an username");
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

        ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if(parseUser.getBoolean("emailVerified")) {
                        dlg.dismiss();
                        alertDisplayer("Login Sucessful", "Welcome, " + parseUser.getUsername().toString() + "!", false);
                    }
                    else {
                        ParseUser.logOut();
                        dlg.dismiss();
                        alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                        // dlg.dismiss();
                        // alertDisplayer("Sucessful Login","Welcome back " + usernameView.getText().toString() + "!");
                    }
                } else {
                    dlg.dismiss();
                    ParseUser.logOut();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    alertDisplayer("Login Fail", e.getMessage() + " Please re-try", true);
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

    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (!error) {
                            Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
