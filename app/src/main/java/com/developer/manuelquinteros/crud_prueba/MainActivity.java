package com.developer.manuelquinteros.crud_prueba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et_update_name, et_update_email;
    Button btn_update, btn_cancel;
    RecyclerView recyclerView;
    MyAdapter adapter;

    List<UserData> list = new ArrayList<>();

    DatabaseHelper db;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    String name, email;
    //https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        list.addAll(db.getAllCrud());

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.btn_add);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);

        final int list = db.getCrudCount();
        TextView totalReg = (TextView)findViewById(R.id.totalregistros);
        totalReg.setText(String.valueOf(list));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


 //https://www.androidtutorialpoint.com/storage/android-sqlite-database-tutorial/
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, UserData userData) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Update User Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_update, null, false);
                InitUpdateDialog(position, view);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void OnItemDeleteClick(int position, UserData userData) {
                deleteCrud(position);
            }
        });

      //  countRegistros();
    }

    private void InitUpdateDialog(final int position, View view) {
        et_update_name = view.findViewById(R.id.et_update_name);
        et_update_email = view.findViewById(R.id.et_update_email);
        btn_update = view.findViewById(R.id.btn_update_user);
        btn_cancel = view.findViewById(R.id.btn_update_cancel);


        et_update_name.setText(list.get(position).getName());
        et_update_email.setText(list.get(position).getEmail());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_update_name.getText().toString();
                email = et_update_email.getText().toString();

                UserData userData = new UserData();

                //userData.setName(name);
                //userData.setEmail(email);

               // adapter.UpdateData(position, userData);
                updateCrud(name,email, position);
                Toast.makeText(MainActivity.this, "Usuario Actualizado...", Toast.LENGTH_SHORT);
            }
        });
       btn_cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
    }

    private void updateCrud(String name, String email, int position) {
        UserData userData = list.get(position);

        //Updating crud text
        userData.setName(name);
        userData.setEmail(email);

        //Updating crud in db
        db.updateCrud(userData);

        list.set(position, userData);
        adapter.notifyItemChanged(position);

    }

    private void deleteCrud(int position) {
        //deleting the crud from db
        db.deleteCrud(list.get(position));

        //removing the crud from the list
        list.remove(position);
        adapter.notifyItemRemoved(position);


    }



}
