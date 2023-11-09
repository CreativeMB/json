package com.creativem.json;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import com.creativem.json.ItemAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerJSON extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_json);
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("estadisticas", "");

// Parsea la cadena JSON a una lista de objetos ModelItem
        List<ModelItem> itemList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String detail = jsonObject.getString("detail");
                String consejo = jsonObject.getString("consejo");
                String imagen = jsonObject.getString("imagen");
                int cantidad = jsonObject.getInt("cantidad");
                ModelItem modelItem = new ModelItem(title, detail, imagen, consejo, cantidad);
                itemList.add(modelItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Configura el RecyclerView y el adaptador
        RecyclerView recyclerView = findViewById(R.id.recicler); // Asegúrate de que el ID sea el correcto
        ItemAdapter  adapter = new ItemAdapter(itemList, VerJSON.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}