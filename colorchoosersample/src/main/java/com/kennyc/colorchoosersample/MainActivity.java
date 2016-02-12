package com.kennyc.colorchoosersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kennyc.colorchooser.ColorChooserDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorChooserDialog.Builder(MainActivity.this)
                        .colors(R.array.colors)
                        .listener(new ColorChooserDialog.ColorListener() {
                            @Override
                            public void onColorSelect(int color) {
                                Toast.makeText(getApplicationContext(), "Color Selected " + color, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .positiveButton("Okay")
                        .negativeButton("Cancel")
                        .title("Select Color")
                        .build()
                        .show(getFragmentManager(), null);
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }
}
