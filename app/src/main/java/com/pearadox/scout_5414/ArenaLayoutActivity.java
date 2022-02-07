package com.pearadox.scout_5414;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mlm.02000 on 2/5/2017.
 */

public class ArenaLayoutActivity extends Activity {


    String TAG = "ArenaLayoutActivity";      // This CLASS name
    TextView textView_Pos;  Button button_Set;
    Button button_Red1, button_Red2, button_Red3, button_Red4, button_Red5, button_Red6;
    Button button_Blue1, button_Blue2, button_Blue3, button_Blue4, button_Blue5, button_Blue6;
    int TarPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "<< Arena Layout >>");
        setContentView(R.layout.activity_arena_layout);
        Bundle bundle = this.getIntent().getExtras();
        String param1 = bundle.getString("dev");
        String param2 = bundle.getString("stud");
        String side = param1.substring(0,3);
        Log.w(TAG, param1 + " " + param2 + " "  + side);      // ** DEBUG **

        button_Set              = (Button) findViewById(R.id.button_Set);
        textView_Pos            = (TextView) findViewById(R.id.textView_Pos);
        button_Red1             = (Button) findViewById(R.id.button_Red1);
        button_Red2             = (Button) findViewById(R.id.button_Red2);
        button_Red3             = (Button) findViewById(R.id.button_Red3);
        button_Red4             = (Button) findViewById(R.id.button_Red4);
        button_Red5             = (Button) findViewById(R.id.button_Red5);
        button_Red6             = (Button) findViewById(R.id.button_Red6);
        button_Blue1            = (Button) findViewById(R.id.button_Blue1);
        button_Blue2            = (Button) findViewById(R.id.button_Blue2);
        button_Blue3            = (Button) findViewById(R.id.button_Blue3);
        button_Blue4            = (Button) findViewById(R.id.button_Blue4);
        button_Blue5            = (Button) findViewById(R.id.button_Blue5);
        button_Blue6            = (Button) findViewById(R.id.button_Blue6);

        // Don't let them pick wrong side
        if (side.equals("Red")) {
            button_Blue1.setVisibility(View.GONE);
            button_Blue2.setVisibility(View.GONE);
            button_Blue3.setVisibility(View.GONE);
            button_Blue4.setVisibility(View.GONE);
            button_Blue5.setVisibility(View.GONE);
            button_Blue6.setVisibility(View.GONE);
        } else {    //Blue
            Log.i(TAG, "Side is Blue");
            button_Red1.setVisibility(View.GONE);
            button_Red2.setVisibility(View.GONE);
            button_Red3.setVisibility(View.GONE);
            button_Red4.setVisibility(View.GONE);
            button_Red5.setVisibility(View.GONE);
            button_Red6.setVisibility(View.GONE);
        }

        button_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "button_Set  ");      // ** DEBUG **
                String value = textView_Pos.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("value", value);
                setResult(RESULT_OK, intent);
                Log.e(TAG, "Value = " + value);      // ** DEBUG **
                finish();
            }
        });

        //*****************************************************************
        button_Red1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red1  ");      // ** DEBUG **
                textView_Pos.setText("1");
            }
        });
        button_Red2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red2  ");      // ** DEBUG **
                textView_Pos.setText("2");
            }
        });
        button_Red3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red3  ");      // ** DEBUG **
                textView_Pos.setText("3");
            }
        });
        button_Red4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red4  ");      // ** DEBUG **
                textView_Pos.setText("4");
            }
        });
        button_Red5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red5  ");      // ** DEBUG **
                textView_Pos.setText("5");
            }
        });
        button_Red6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Red6  ");      // ** DEBUG **
                textView_Pos.setText("6");
            }
        });


        button_Blue1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue1  ");      // ** DEBUG **
                textView_Pos.setText("1");
            }
        });
        button_Blue2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue2  ");      // ** DEBUG **
                textView_Pos.setText("2");
            }
        });
        button_Blue3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue3  ");      // ** DEBUG **
                textView_Pos.setText("3");
            }
        });
        button_Blue4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue4  ");      // ** DEBUG **
                textView_Pos.setText("4");
            }
        });
        button_Blue5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue5  ");      // ** DEBUG **
                textView_Pos.setText("5");
            }
        });
        button_Blue6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "button_Blue6  ");      // ** DEBUG **
                textView_Pos.setText("6");
            }
        });


        // === End of OnCreate ===
    }


//###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");

    }

}
