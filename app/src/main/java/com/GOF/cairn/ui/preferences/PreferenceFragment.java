package com.GOF.cairn.ui.preferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.GOF.cairn.AuthHold;
import com.GOF.cairn.IntentHelper;
import com.GOF.cairn.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PreferenceFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button lbtnSaveSettings, lbtnLogout,lbtnFAQ;
    RadioGroup lrdgLandmarkType;
    IntentHelper helpIt = new IntentHelper();
    SwitchMaterial lswMetric;

    EditText ledtEmail, ledtHomeAddressl;
    private View view;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();

    public PreferenceFragment() {
        // Required empty public constructor
    }

    public static PreferenceFragment newInstance(String param1, String param2) {
        PreferenceFragment fragment = new PreferenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preference, container, false);
        assignComponents();
        assignData();
        lbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthHold.getInstance().fireUser = null;  //logs user out
                helpIt.logout(getActivity());
            }
        });  //TODO - Test

        lbtnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selLandType = lrdgLandmarkType.getCheckedRadioButtonId(); //gets element id 0,1,2
                if( selLandType== -1){
                    Toast.makeText(getActivity(),"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AuthHold.getInstance().loggedInUser.homeAddr = ledtHomeAddressl.getText().toString().trim() + "";           //updates local user home affress
                    AuthHold.getInstance().loggedInUser.fLandType = selLandType + "";           //updates local user fav landmark
                    AuthHold.getInstance().loggedInUser.metric = lswMetric.isChecked();         //updates local user preferred measurement system

                    DatabaseReference myRefUsersDetails = database.getReference("Users").child(AuthHold.getInstance().fireUser.getUid()).child("UserI");   //point to user's folder
                    myRefUsersDetails.removeValue();                                                                                                            //removes value and adds updated one again
                    myRefUsersDetails.push().setValue(AuthHold.getInstance().loggedInUser).addOnSuccessListener(new OnSuccessListener<Void>() {                 //updates user in firebase (no .push overwrites)
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "User Updated, saved successfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(getActivity(), "Update was canceled", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });  //TODO - Test

        lbtnFAQ.setOnClickListener(new View.OnClickListener() {  //moves to actFAQ
            @Override
            public void onClick(View view) {
                helpIt.openIntentFAQ(getActivity());
            }
        });
        return view;
    }

    private void assignData() {
        ledtEmail.setText(AuthHold.getInstance().loggedInUser.email);
        ledtHomeAddressl.setText(AuthHold.getInstance().loggedInUser.homeAddr);
        lswMetric.setChecked(AuthHold.getInstance().loggedInUser.metric);
    }

    private void assignComponents() {
        ledtEmail = view.findViewById(R.id.edtPreferencesEmail);
        ledtHomeAddressl = view.findViewById(R.id.edtPreferencesHomeAddress);
        lbtnSaveSettings = view.findViewById(R.id.btnSaveSettings);
        lrdgLandmarkType = view.findViewById(R.id.rdgLandmarkType);
        lbtnLogout = view.findViewById(R.id.btnLogout);
        lbtnFAQ = view.findViewById(R.id.btnFAQ);
        lswMetric = view.findViewById(R.id.swMetric);
    }
}