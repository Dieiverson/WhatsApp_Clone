package com.agiliziumapps.whats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.agiliziumapps.whats.helper.Permissao;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarPrincipal;
    private FirebaseUser user;
    private String[] permissoesNecessarias = new String[]
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.setTitle("WhatsApp");
        setSupportActionBar(toolbarPrincipal);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(),
                                        FragmentPagerItems.with(this).
                                                add("Conversas", conversas_fragment.class).
                                                add("Contatos",fragment_contatos.class).create());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
       user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            startActivity(new Intent(getApplicationContext(),Cadastrar.class));
            finish();
            return;
        }
        if(!isGooglePlayServicesAvailable(this))
        {
            return;
        }
        Permissao.validarPermissoes(permissoesNecessarias,this,1);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults)
        {
            if(permissaoResultado == PackageManager.PERMISSION_DENIED)
            {
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões.");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuSair:
                deslogarUsuário();
                Intent intent = new Intent(this,Cadastrar.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void deslogarUsuário()
    {
        try {
            FirebaseAuth.getInstance().signOut();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void abrirConfiguracoes()
    {
        Intent intent = new Intent(this,configuracoesActivity.class);
        startActivity(intent);
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
}
