package com.example.kezdesu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String uid = getIntent().getStringExtra("uid");
        Log.i(TAG, "UID: " + uid);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get UserProfile object and use the values to update the UI
                UserProfile user = snapshot.getValue(UserProfile.class);
                TextView name = findViewById(R.id.user_name);
                name.setText(user.name);

                TextView email = findViewById(R.id.user_email);
                email.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting User failed, log a message
                Log.w(TAG, "loadUser:onCancelled", error.toException());
                // [START_EXCLUDE]
                Toast.makeText(UserProfileActivity.this, "Failed to load user.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });

        Button invite = (Button) findViewById(R.id.invite_button);
        invite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InvitationsDialogFragment dialog = new InvitationsDialogFragment();
                dialog.show(getSupportFragmentManager(), "NoticeDialog");
                // Code here executes on main thread after user presses button
            }
        });
    }

    public static class InvitationsDialogFragment extends DialogFragment {
        public void createInvitation(int type) {
            /* ParseObject invitation = new ParseObject("Invitations");
            Log.i("UserProfileActivity", "current user:" + ParseUser.getCurrentUser().getUsername() + " destination user:" + user.getUsername() + " type: " + getResources().getStringArray(R.array.invitations)[type]); ;
            invitation.put("from", ParseUser.getCurrentUser().getObjectId());
            invitation.put("to", user.getObjectId());
            invitation.put("type", getResources().getStringArray(R.array.invitations)[type]);

            ParseACL invitationACL = new ParseACL();
            invitationACL.setReadAccess(ParseUser.getCurrentUser().getObjectId(), true);
            invitationACL.setWriteAccess(ParseUser.getCurrentUser().getObjectId(), true);
            invitationACL.setReadAccess(user.getObjectId(), true);
            invitation.setACL(invitationACL);

            invitation.saveInBackground(); */
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Invite for a")
                    .setItems(R.array.invitations, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            createInvitation(i);
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create(); */
            return null;
        }
    }

}
