package com.example.miprimerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaración de elementos y el DBHelper
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSignUp;
    private DBHelper dbHelper; // Instancia de nuestra clase DBHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que este es el nombre de tu archivo de diseño de login (activity_main.xml)
        setContentView(R.layout.activity_main);

        // 1. Enlazar elementos del XML
        etUsername = findViewById(R.id.et_username); // ID del campo de usuario
        etPassword = findViewById(R.id.et_password); // ID del campo de contraseña
        btnSignUp = findViewById(R.id.btn_sign_up);   // ID del botón "REGISTRARSE" (usado como Login)

        // 2. Inicializar el DBHelper
        // Esto creará la DB y la tabla de USERS con el usuario por defecto (test@app.com / 1234)
        dbHelper = new DBHelper(this);

        // 3. Configurar el evento click del botón para el Login
        btnSignUp.setOnClickListener(view -> {
            checkLogin();
        });
    }

    /**
     * Método para manejar la lógica de autenticación contra la Base de Datos.
     */
    private void checkLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Validación de campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa usuario y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Validación de credenciales contra la DB usando el método checkUser del DBHelper
        boolean isAuthenticated = dbHelper.checkUser(username, password);

        if (isAuthenticated) {
            // Login Exitoso: Navegar a la Segunda Pantalla
            Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish(); // Cierra MainActivity para que el usuario no pueda volver con el botón atrás
        } else {
            // Login Fallido
            Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show();
        }
    }
}