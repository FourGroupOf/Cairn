package com.GOF.cairn;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthHold { //singleton for authentication

    private static AuthHold sinstance = null;
    private AuthHold() {

    }
    public static AuthHold getInstance() {
        if (sinstance == null)
            sinstance = new AuthHold();

        return sinstance;
    }

    public FirebaseDatabase database = FirebaseDatabase.getInstance();                      //connect get my Fb instance (DB)
    public FirebaseUser fireUser;
    public UserI loggedInUser;
    public String logReturn ="";
    public String newLand = "";

    public double lati,longi;

    public String updateUser() {  //updates user details (adds fav landmark to db on favlandmark add)
        DatabaseReference myRefUsersS = database.getReference("Users").child(fireUser.getUid()).child("UserI"); //get the folder  (each user's folder)
        myRefUsersS.removeValue();
        myRefUsersS.push().setValue(loggedInUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                logReturn = "User Details Updated";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                logReturn = e.getMessage();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                logReturn = "Update was canceled";
            }
        });

        return logReturn;
    }

}
