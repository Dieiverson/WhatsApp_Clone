package com.agiliziumapps.whats;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase
{
    private static DatabaseReference Database;
    private static FirebaseAuth Auth;

    public static DatabaseReference getDatabaseFirebase()
    {
        if(Database == null)
        {
            Database = FirebaseDatabase.getInstance().getReference();
        }
        return Database;
    }

    public static FirebaseAuth getAuth()
    {
        if(Auth == null)
        {
            Auth = FirebaseAuth.getInstance();
        }
        return Auth;
    }
}
