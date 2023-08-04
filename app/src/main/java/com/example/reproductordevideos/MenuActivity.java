package com.example.reproductordevideos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Obtener los datos enviados desde MainActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);

        // Mostrar el mensaje de bienvenida personalizado
        TextView textViewWelcomeMessage = findViewById(R.id.textViewWelcomeMessage);
        String welcomeMessage = "Hola " + name + ", de acuerdo a su edad las categorías disponibles son: ";

        if (age < 12) {
            welcomeMessage += "Caricaturas";
        } else if (age >= 12 && age < 18) {
            welcomeMessage += "Caricaturas y Acción";
        } else {
            welcomeMessage += "Caricaturas, Acción y Terror";
        }

        textViewWelcomeMessage.setText(welcomeMessage);

        // Mostrar u ocultar las categorías según la edad
        LinearLayout layoutCaricaturas = findViewById(R.id.layoutCaricaturas);
        LinearLayout layoutAccion = findViewById(R.id.layoutAccion);
        LinearLayout layoutTerror = findViewById(R.id.layoutTerror);

        if (age < 12) {
            layoutCaricaturas.setVisibility(View.VISIBLE);
            layoutAccion.setVisibility(View.VISIBLE);
            layoutTerror.setVisibility(View.VISIBLE);
        } else if (age >= 12 && age < 18) {
            layoutCaricaturas.setVisibility(View.VISIBLE);
            layoutAccion.setVisibility(View.VISIBLE);
            layoutTerror.setVisibility(View.VISIBLE);
        } else {
            layoutCaricaturas.setVisibility(View.VISIBLE);
            layoutAccion.setVisibility(View.VISIBLE);
            layoutTerror.setVisibility(View.VISIBLE);
        }
    }

    public void onCategoryClicked(View view) {
        String categoryName = view.getTag().toString();
        Toast.makeText(this, "Has seleccionado la categoría: " + categoryName, Toast.LENGTH_SHORT).show();

        // Abrir el cuadro de diálogo para pedir la foto de perfil
        showProfilePhotoDialog();
    }

    private void showProfilePhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_photo, null);
        builder.setView(dialogView);

        final ImageView imageViewProfilePhoto = dialogView.findViewById(R.id.imageViewProfilePhoto);
        Button btnChoosePhoto = dialogView.findViewById(R.id.btnChoosePhoto);
        Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Lógica para seleccionar una foto de perfil desde la galería (Aquí debes implementar la funcionalidad según tus necesidades)
        // ...

        final AlertDialog dialog = builder.create();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes agregar la lógica para tomar una fotografía con la cámara y mostrarla en la pantalla de reproductor
                dispatchTakePictureIntent();

                dialog.dismiss(); // Cierra el cuadro de diálogo y vuelve al menú
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Cierra el cuadro de diálogo y vuelve al menú
            }
        });

        dialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Guardar la imagen capturada en una variable global (o en la base de datos)
            // Aquí lo estamos almacenando en una variable global para mostrarlo en la pantalla de reproductor
            AppData.profilePhotoBitmap = imageBitmap;

            // Lógica para pasar los datos a la pantalla de reproductor
            Intent intent = new Intent(MenuActivity.this, ReproductorActivity.class);
            intent.putExtra("name", "Nombre del Usuario"); // Aquí coloca el nombre del usuario capturado en la primera pantalla
            intent.putExtra("age", 25); // Aquí coloca la edad del usuario capturada en la primera pantalla
            intent.putExtra("gender", "Género del Usuario"); // Aquí coloca el género del usuario capturado en la primera pantalla
            startActivity(intent);
        }
    }
}