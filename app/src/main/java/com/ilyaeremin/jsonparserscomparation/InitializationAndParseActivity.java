package com.ilyaeremin.jsonparserscomparation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.ason.Ason;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilyaeremin.jsonparserscomparation.models.Human;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InitializationAndParseActivity extends AppCompatActivity {

    private final Type HUMAN_LIST_GSON = new TypeToken<ArrayList<Human>>() {
    }.getType();

    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jsonString = Utils.readJsonAsStringFromDisk(this, R.raw.big);
    }

    @OnClick(R.id.gson) void onGsonClick() {
        long before = System.currentTimeMillis();
        List<Human> humanList = new Gson().fromJson(jsonString, HUMAN_LIST_GSON);
        Toast.makeText(this, (System.currentTimeMillis() - before) + "millis", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.jackson) void onJacksonClick() {
        try {
            long before = System.currentTimeMillis();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(mapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
            List<Human> humanList = Arrays.asList(mapper.readValue(jsonString, Human[].class));
            Toast.makeText(this, (System.currentTimeMillis() - before) + "millis", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.moshi) void onMoshiClick() {
        long before = System.currentTimeMillis();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<Human>> adapter = moshi.adapter(Types.newParameterizedType(List.class, Human.class));
        try {
            List<Human> humans = adapter.fromJson(jsonString);
            Toast.makeText(this, (System.currentTimeMillis() - before) + "millis", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ason) void onAsonClick() {
        long before = System.currentTimeMillis();
        List<Human> humans = Ason.deserializeList(jsonString, Human.class);
        Toast.makeText(this, (System.currentTimeMillis() - before) + "millis", Toast.LENGTH_SHORT).show();
    }
}
