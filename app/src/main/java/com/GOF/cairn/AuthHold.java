package com.GOF.cairn;

import com.google.firebase.auth.FirebaseUser;

public class AuthHold { //singleton for authentication

    private static AuthHold sinstance = null;
    private AuthHold() {

    }
    public static AuthHold getInstance() {
        if (sinstance == null)
            sinstance = new AuthHold();

        return sinstance;
    }

    public FirebaseUser fireUser;
    public UserI loggedInUser;

}
