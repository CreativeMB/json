package com.creativem.json;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ItemAdapter itemAdapter;
    private ArrayList<ModelItem> listItem;
    private RecyclerView recyclerView;
    private ModelItem selectedModelItem;
    private JSONObject jsonSeleccionado;
    private ModelItem selectedItem;
    Button ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonSeleccionado = new JSONObject();

        recyclerView = findViewById(R.id.rvItemLis);
        listItem = new ArrayList<>();
        itemAdapter = new ItemAdapter(listItem, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
        ver = findViewById(R.id.btnVerJSON);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividadVerJSON();
            }
        });

        jsonRead();

        Button button = findViewById(R.id.btn_guardar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarJSON();
            }
        });
    }
    private void guardarItemSeleccionado(){

        SharedPreferences prefs = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("titulo", selectedModelItem.getTitle());
        int imageId = selectedItem.getImagen();
        String imageIdString = String.valueOf(imageId);
        editor.putString("imagen", imageIdString);
        editor.putString("descripcion", selectedModelItem.getDetail());
        editor.putString("consejos", selectedModelItem.getConsejos());

        editor.apply();

    }
    public void irAVerItem(){

        Intent intent = new Intent(MainActivity.this, VerJSON.class);
        startActivity(intent);

    }
    private void guardarJSON() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);

        // Recupera la lista actual de objetos JSON almacenados en las preferencias compartidas
        String jsonString = sharedPreferences.getString("estadisticas", "");
        JSONArray jsonArray;

        try {
            // Si ya hay datos guardados, convierte la cadena JSON en un JSONArray existente
            if (!jsonString.isEmpty()) {
                jsonArray = new JSONArray(jsonString);
            } else {
                // Si no hay datos guardados, crea un nuevo JSONArray
                jsonArray = new JSONArray();
            }

            if (selectedModelItem != null) {
                // Crea un nuevo objeto JSON para el elemento seleccionado
                JSONObject itemJSON = new JSONObject();
                itemJSON.put("title", selectedModelItem.getTitle());
                itemJSON.put("detail", selectedModelItem.getDetail());
                itemJSON.put("consejo", selectedModelItem.getConsejos());
                itemJSON.put("imagen", selectedModelItem.getImagen());
                itemJSON.put("cantidad", selectedModelItem.getCantidad());

                // Agrega el objeto JSON a la lista
                jsonArray.put(itemJSON);
            } else {
                Toast.makeText(MainActivity.this, "Ningún elemento seleccionado", Toast.LENGTH_SHORT).show();
            }

            // Guarda la lista actualizada en las preferencias compartidas
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("estadisticas", jsonArray.toString());
            editor.apply();
            Log.d("JSON", jsonArray.toString());
            // Puedes realizar otras acciones necesarias
        } catch (JSONException e) {
            e.printStackTrace();
        }

   /* private void guardarJSON() {
        if (selectedModelItem != null) {
            try {
                jsonSeleccionado.put("title", selectedModelItem.getTitle());
                jsonSeleccionado.put("detail", selectedModelItem.getDetail());
                jsonSeleccionado.put("consejo", selectedModelItem.getConsejos());
                jsonSeleccionado.put("imagen", selectedModelItem.getImagen());
                jsonSeleccionado.put("cantidad", selectedModelItem.getCantidad());
                // Agrega más datos al JSON si es necesario
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, "Ningún elemento seleccionado", Toast.LENGTH_SHORT).show();
        }

        if (jsonSeleccionado != null) {
            String jsonString = jsonSeleccionado.toString();
            SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("estadisticas", jsonString);
            editor.apply();
            Log.d("JSON", jsonString);
            // Aquí puedes realizar otras acciones necesarias
        }*/
    }
    private void abrirActividadVerJSON(){

        SharedPreferences prefs = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
        String jsonGuardado = prefs.getString("estadisticas", null);

        Intent intent = new Intent(MainActivity.this, VerJSON.class);
        intent.putExtra("json", jsonGuardado);
        startActivity(intent);

    }
    private void jsonRead() {
        try {
            JSONArray jsonArray = new JSONArray(readDataFromFile());
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String detail = jsonObject.getString("detail");
                String consejos = jsonObject.getString("consejo");
                String imagenName = jsonObject.getString("imagen");
                int imagenId = getResources().getIdentifier(imagenName, "drawable", getPackageName());
                ModelItem modelItem = new ModelItem(title, detail, imagenId, consejos);
                listItem.add(modelItem);
            }
            itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readDataFromFile() {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonString;
            inputStream = getResources().openRawResource(R.raw.item);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return builder.toString();
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private final ArrayList<ModelItem> modelItem;
        private final Context context;

        public ItemAdapter(ArrayList<ModelItem> modelItem, Context context) {
            this.modelItem = modelItem;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ModelItem item = modelItem.get(position);
            Glide.with(context).load(item.getImagen()).into(holder.imagen);
            holder.title.setText(item.getTitle());
            holder.detail.setText(item.getDetail());
            holder.consejos.setText(item.getConsejos());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedModelItem = item;
                    notifyDataSetChanged();
                }
            });

            if (selectedModelItem != null && selectedModelItem.equals(item)) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public int getItemCount() {
            return modelItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imagen;
            private final TextView title;
            private final TextView detail;
            private final TextView consejos;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imagen = itemView.findViewById(R.id.imagenItem);
                title = itemView.findViewById(R.id.titleItem);
                detail = itemView.findViewById(R.id.detailItem);
                consejos = itemView.findViewById(R.id.consejosItem);
            }
        }
    }
}