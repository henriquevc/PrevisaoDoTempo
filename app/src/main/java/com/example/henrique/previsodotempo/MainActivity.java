package com.example.henrique.previsodotempo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    private EditText cidadeEditText;
    private ListView cidadesListView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference cidadesReference;
    private List<Cidade> cidades;
    private ArrayAdapter<Cidade> cidadesAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        cidadesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cidades.clear();
                for(DataSnapshot filho : dataSnapshot.getChildren()){
                    Cidade c = filho.getValue(Cidade.class);
                    c.setChave(filho.getKey());
                    cidades.add(c);
                }
                cidadesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //campo de digitar a cidade
        cidadeEditText = findViewById(R.id.cidadeEditText);

        //lista de cidades
        cidadesListView = findViewById(R.id.cidadesListView);
        cidades = new Stack<Cidade>();
        cidadesAdapter = new ArrayAdapter<Cidade>(this, android.R.layout.simple_list_item_1, cidades);
        cidadesListView.setAdapter(cidadesAdapter);


        //OnItemClick
        cidadesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cidade cidade =(Cidade) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, ListaPrevisaoActivity.class);
                intent.putExtra("cidade",  cidade);
                startActivity(intent);
            }
        });

        //onItemLongClick
        cidadesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });


        //pegando a database de cidades do firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        cidadesReference = firebaseDatabase.getReference("cidades");
        //quando clicar no fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeCidade = cidadeEditText.getEditableText().toString();
                String chave = cidadesReference.push().getKey();
                Cidade cidade = new Cidade(chave, nomeCidade);
                cidadesReference.child(chave).setValue(cidade);
                Toast.makeText(MainActivity.this, R.string.cidade_adicionada, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
