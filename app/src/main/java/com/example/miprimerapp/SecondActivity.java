package com.example.miprimerapp; // Asegúrate de que este sea tu nombre de paquete

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    // 1. Declaración de elementos de la interfaz de usuario y el DBHelper
    private EditText etFullname;
    private EditText etEmail;
    private Button btnSaveData;
    private DBHelper dbHelper; // Instancia de nuestra clase DBHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // 2. Enlazar variables Java con IDs del XML
        etFullname = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        btnSaveData = findViewById(R.id.btn_save_data);

        // 3. Inicializar el DBHelper
        dbHelper = new DBHelper(this);

        // 4. Configurar el evento click del botón
        btnSaveData.setOnClickListener(view -> {
            saveDataRecord();
        });
    }

    /**
     * Método para capturar los datos e insertarlos en la base de datos.
     */
    private void saveDataRecord() {
        // Obtener los valores de los campos de texto como Strings
        String fullName = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validación simple: verificar que no estén vacíos
        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, completa Nombre y Email para guardar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // -----------------------------------------------------
        // 5. LLAMADA A LA LÓGICA DE INSERCIÓN EN BASE DE DATOS
        // -----------------------------------------------------

        // Llamamos al método que creamos en DBHelper
        boolean isInserted = dbHelper.insertDataRecord(fullName, email);

        if (isInserted) {
            // Requisito cumplido: el registro persiste en la DB local
            Toast.makeText(this, "✅ ¡Registro GUARDADO correctamente en la DB!", Toast.LENGTH_LONG).show();

            // Opcional: limpiar campos después de guardar
            etFullname.setText("");
            etEmail.setText("");
        } else {
            // Este mensaje se mostrará si la inserción en la DB falla.
            Toast.makeText(this, "❌ Error al intentar guardar el registro.", Toast.LENGTH_SHORT).show();
        }
    }
}