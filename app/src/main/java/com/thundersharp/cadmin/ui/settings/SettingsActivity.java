package com.thundersharp.cadmin.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.facebook.login.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thundersharp.cadmin.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                getActivity().getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
            }

            Preference button = findPreference("btndelforever");
            Preference resetOrganisation = findPreference("resetOrganisation");
            Preference resetProj = findPreference("resetProjects");

            if (button != null){
                AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.delete_user,null);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
                }
                final EditText pass = view.findViewById(R.id.pass);
                alert.setTitle("Confirm Deletion");
                alert.setView(view);
                alert.setMessage("Sad to see you go, do you really want to delete your account from CAdmin? deleting your account will remove all your data including all organisations, all projects and this action cannot be reverted !");

                alert.setCancelable(false);

                final LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.delete_loading,null);
                final AlertDialog.Builder alert1 = new AlertDialog.Builder(getActivity());
                alert1.setView(alertLayout);
                alert1.setCancelable(false);
                final Dialog delete = alert1.create();
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        delete.show();
                        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        if (!pass.getText().toString().equals("")){
                            AuthCredential authCredential = EmailAuthProvider
                                    .getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),pass.getText().toString());
                            firebaseAuth.getCurrentUser()
                                    .reauthenticate(authCredential)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            delete.dismiss();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("users")
                                                .child(firebaseAuth.getUid())
                                                .child("personal_data")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String pic = snapshot.child("Profile Images").getValue(String.class);
                                                        if (pic != null){
                                                            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                                                                    .child(pic);
                                                            storageReference.delete();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("users")
                                                .child(firebaseAuth.getUid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            firebaseAuth.getCurrentUser()
                                                                    .delete()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                startActivity(new Intent(getActivity(), LoginFragment.class));
                                                                                getActivity().finish();
                                                                                Intent intent = new Intent("close12");
                                                                                getActivity().sendBroadcast(intent);
                                                                                dialog.dismiss();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }else {
                            delete.dismiss();
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog alertDialog = alert.create();
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        alertDialog.show();
                        return true;
                    }
                });
            }
        }
    }
}