package com.agiliziumapps.whats;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase
{
    private static DatabaseReference Database;
    private static FirebaseAuth Auth;
    private static StorageReference Storage;

    public static DatabaseReference getDatabaseFirebase()
    {
        if(Database == null)
        {
            Database = FirebaseDatabase.getInstance().getReference();
        }
        return Database;
    }

    public static StorageReference getStorageFirebase()
    {
        if(Storage == null)
        {
            Storage = FirebaseStorage.getInstance().getReference();
        }
        return Storage;

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
