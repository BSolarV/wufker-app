package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.messirvedevs.wufker.ForoDetailActivity;
import com.messirvedevs.wufker.R;

public class CrearPostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_post);
    }

    public void publicar(View view) {
        TextInputEditText editTitle = findViewById(R.id.post_title);
        TextInputEditText editContent = findViewById(R.id.post_content);

        String postTitle = editTitle.getText().toString();
        String postContent = editContent.getText().toString();

        if (postTitle.length() > 0 && postContent.length() > 0) {
            // Agregar los nuevos post a firebase
            Toast.makeText(this, "Titulo: " + postTitle +
                    "\nContenido: " + postContent, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view) {
        finish();
    }
}
