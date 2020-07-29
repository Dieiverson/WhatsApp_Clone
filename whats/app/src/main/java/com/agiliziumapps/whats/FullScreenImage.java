package com.agiliziumapps.whats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {
    String imagem;
    ImageView imageViewPrincipal;
    Toolbar toolbarPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        toolbarPrincipal  = findViewById(R.id.toolbar);
        toolbarPrincipal.setTitle("");
        setSupportActionBar(toolbarPrincipal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageViewPrincipal = findViewById(R.id.imageViewPrincipal);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            imagem =  bundle.getString("imagem");

        if(imagem != null)
        {
            Uri url = Uri.parse(imagem);
            Glide.with(this)
                    .load(url)
                    .thumbnail(Glide.with(this).load(R.drawable.loading))
                    .into(imageViewPrincipal);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}