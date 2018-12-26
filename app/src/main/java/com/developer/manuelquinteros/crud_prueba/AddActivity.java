package com.developer.manuelquinteros.crud_prueba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    EditText et_name, et_email;
    Button add;
    String name, email;

    List<UserData> list = new ArrayList<>();

    private DatabaseHelper db;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = new DatabaseHelper(this);

        adapter = new MyAdapter(list);

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);


        add = (Button) findViewById(R.id.btn_added);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                email = et_email.getText().toString();


                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(AddActivity.this, "Ingrese Nombre!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(email)) {
                        Toast.makeText(AddActivity.this, "Ingrese Email!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        createCrud(name, email);

                        UserData userData = new UserData();
                        userData.setName(name);
                        userData.setEmail(email);

                        et_name.setText("");
                        et_email.setText("");

                    }

            }
        });

    }

    private void createCrud(String name, String email) {

        if (db.validarRepeticionCrud(name) == false) {
            long id = db.insertCrud(name, email);

            UserData userData = db.getCrud(id);

            if (userData != null) {
                list.add(0, userData);

                adapter.notifyDataSetChanged();

                Toast.makeText(AddActivity.this, "Usuario agregado...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(AddActivity.this, "Usuario Repetido", Toast.LENGTH_SHORT).show();
        }
    }
}
