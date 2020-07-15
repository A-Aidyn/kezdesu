package com.example.kezdesu;

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

import com.google.firebase.auth.FirebaseAuth;


public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();

        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(mAuth.getCurrentUser().getDisplayName());

        TextView email = (TextView) findViewById(R.id.user_email);
        email.setText(mAuth.getCurrentUser().getEmail());

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
