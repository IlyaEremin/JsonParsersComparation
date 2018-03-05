package com.ilyaeremin.jsonparserscomparation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.ason.Ason;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilyaeremin.jsonparserscomparation.models.Human;
import com.ilyaeremin.jsonparserscomparation.models.SmallObject;
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

/**
 * Created by ereminilya on 22/2/17.
 */

public class ParseNTimesSmallJson extends AppCompatActivity {

    private static final int TIMES = 1;

    private final Type HUMAN_LIST_GSON = new TypeToken<ArrayList<SmallObject>>() {
    }.getType();

    private String jsonString;
    private Gson gson;
    private ObjectMapper mapper;
    private JsonAdapter<List<SmallObject>> moshiAdapter;
    private GcTracker gcTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jsonString = Utils.readJsonAsStringFromDisk(this, R.raw.small);
        gson = new Gson();
        mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        Moshi moshi = new Moshi.Builder().build();
        moshiAdapter = moshi.adapter(Types.newParameterizedType(List.class, Human.class));
        gcTracker = new GcTracker();
        gcTracker.startListening();
    }


    @OnClick(R.id.gson)
    void onGsonClick() {
        printToast(Utils.doNTimes(TIMES, new Runnable() {
            @Override
            public void run() {
                List<Human> humanList = gson.fromJson(jsonString, HUMAN_LIST_GSON);
            }
        }));
    }

    @OnClick(R.id.jackson)
    void onJacksonClick() {
        printToast(Utils.doNTimes(TIMES, new Runnable() {
            @Override
            public void run() {
                try {
                    List<SmallObject> humanList = Arrays.asList(mapper.readValue(jsonString, SmallObject[].class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


    @OnClick(R.id.moshi)
    void onMoshiClick() {
        printToast(Utils.doNTimes(TIMES, new Runnable() {
            @Override
            public void run() {
                try {
                    List<SmallObject> humans = moshiAdapter.fromJson(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @OnClick(R.id.ason)
    void onAsonClick() {
        printToast(Utils.doNTimes(TIMES, new Runnable() {
            @Override
            public void run() {
                List<Human> humans = Ason.deserializeList(jsonString, Human.class);
            }
        }));
    }

    private void printToast(long millis) {
        Toast.makeText(this, millis + "millis", Toast.LENGTH_LONG).show();
        System.out.println(millis);
    }

    @Override
    protected void onDestroy() {
        gcTracker.stopListening();
        super.onDestroy();
    }
}
