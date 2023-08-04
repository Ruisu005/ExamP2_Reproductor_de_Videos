package com.example.reproductordevideos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button IrMenu = (Button) findViewById(R.id.btnMenu);
        IrMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });
    }

    private void mostrarDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_capture_info, null);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        final Spinner spinnerGender = dialogView.findViewById(R.id.spinnerGender);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = editTextName.getText().toString().trim();
                String ageString = editTextAge.getText().toString().trim();
                String gender = spinnerGender.getSelectedItem().toString();

                if (name.isEmpty() || ageString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    mostrarDialogo(); // Pedir la captura nuevamente si algún campo está vacío
                    return;
                }

                int age = Integer.parseInt(ageString);

                // Guardar los datos en el archivo de memoria interna
                GuardarPerfil(name, age, gender);

                // Ir a la pantalla del Menú con el mensaje de bienvenida
                String welcomeMessage = "Hola " + name + ", de acuerdo a su edad las categorías disponibles son";
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("welcomeMessage", welcomeMessage);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void GuardarPerfil(String name, int age, String gender) {
        try {
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_PRIVATE);
            String data = name + "," + age + "," + gender;
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}