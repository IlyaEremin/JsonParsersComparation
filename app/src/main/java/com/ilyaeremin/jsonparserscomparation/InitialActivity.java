package com.ilyaeremin.jsonparserscomparation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ereminilya on 22/2/17.
 */

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_initial);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.small_json)
    public void onSmallJsonClick() {
        startActivity(new Intent(this, ParseNTimesSmallJson.class));
    }

    @OnClick(R.id.big_json)
    public void onBigJsonClick() {
        startActivity(new Intent(this, ParseNTimesBigJsonActivity.class));
    }
}
