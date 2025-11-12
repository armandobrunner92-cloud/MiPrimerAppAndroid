package com.example.miprimerapp; // ¡ASEGÚRATE que este sea tu nombre de paquete!

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Constantes de la Base de Datos
    public static final String DATABASE_NAME = "MiAppDB.db";
    public static final int DATABASE_VERSION = 1;

    // Tabla para USUARIOS (Para el Login)
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COL_ID = "id";
    public static final String USERS_COL_USERNAME = "username";
    public static final String USERS_COL_PASSWORD = "password";

    // Tabla para REGISTROS de la SecondActivity
    public static final String RECORDS_TABLE_NAME = "records";
    public static final String RECORDS_COL_ID = "id";
    public static final String RECORDS_COL_FULLNAME = "fullname";
    public static final String RECORDS_COL_EMAIL = "email";
    public static final String RECORDS_COL_TIMESTAMP = "timestamp";


    public DBHelper(Context context) {
        // Constructor que llama a la clase padre SQLiteOpenHelper
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Crear la tabla de USUARIOS (Para el Login)
        String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME + " ("
                + USERS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERS_COL_USERNAME + " TEXT UNIQUE, "
                + USERS_COL_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // ** (REQUISITO PARCIAL) Insertar un usuario por defecto para el Login ** [cite: 26, 27]
        insertDefaultUser(db);

        // 2. Crear la tabla de REGISTROS (Para la SecondActivity)
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + RECORDS_TABLE_NAME + " ("
                + RECORDS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RECORDS_COL_FULLNAME + " TEXT, "
                + RECORDS_COL_EMAIL + " TEXT, "
                + RECORDS_COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se llama cuando la versión de la base de datos cambia
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RECORDS_TABLE_NAME);
        onCreate(db);
    }

    // =========================================================
    // MÉTODOS DE DATOS
    // =========================================================

    // Método para insertar un usuario por defecto (para cumplir con el requisito de Login) [cite: 26, 27]
    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COL_USERNAME, "test@app.com"); // Usuario por defecto
        contentValues.put(USERS_COL_PASSWORD, "1234");          // Contraseña por defecto

        db.insertWithOnConflict(USERS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /**
     * Requisito de Login: Verifica si las credenciales de un usuario son válidas contra la DB. [cite: 17]
     * @return true si las credenciales coinciden, false en caso contrario.
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Consulta para buscar un usuario con el nombre y contraseña dados
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COL_USERNAME + " = ? AND " + USERS_COL_PASSWORD + " = ?", new String[]{username, password});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }


    /**
     * Requisito de SecondActivity: Inserta un nuevo registro de datos en la DB local. [cite: 15, 21]
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertDataRecord(String fullname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // 1. Prepara los datos a insertar
        contentValues.put(RECORDS_COL_FULLNAME, fullname);
        contentValues.put(RECORDS_COL_EMAIL, email);

        // 2. Ejecuta la inserción.
        long result = db.insert(RECORDS_TABLE_NAME, null, contentValues);

        db.close();
        // 3. Verifica el resultado (insert devuelve -1 si falla)
        return result != -1;
    }
}