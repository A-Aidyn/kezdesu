package com.example.kezdesu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class UserListActivity extends AppCompatActivity
        implements
            ItemFragment.OnListFragmentInteractionListener,
            RoomsFragment.OnFragmentInteractionListener,
            NotificationsFragment.OnFragmentInteractionListener,
            SettingsFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private ProgressDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        mAuth = FirebaseAuth.getInstance();

        loadFragment(new ItemFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_users:
                        fragment = new ItemFragment();
                        Toast.makeText(UserListActivity.this, "Users", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_rooms:
                        fragment = new RoomsFragment();
                        Toast.makeText(UserListActivity.this, "Rooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notifications:
                        fragment = new NotificationsFragment();
                        Toast.makeText(UserListActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        fragment = new SettingsFragment();
                        Toast.makeText(UserListActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                dlg = new ProgressDialog(UserListActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Signing Out...");
                dlg.show();

                // logging out of Firebase
                mAuth.signOut();

                alertDisplayer("So, you're going...", "Ok...Bye-bye then");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(UserListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }


    // Doing sth when Item was clicked
    public void onListFragmentInteraction(UserProfile userProfile) {
        Intent intent = new Intent(UserListActivity.this, UserProfileActivity.class);
        intent.putExtra("uid", userProfile.uid);
        startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onStop() {
        if(dlg != null) {
            dlg.dismiss();
            dlg = null;
        }
        super.onStop();
    }
}