package com.example.step_counter_sensor_byankur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

// implement the SensorEventListener and override its 2 functions
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textview1,textview2;
    private SensorManager sensorManager;
    private Sensor sensorCount, sensorDetector;
    private boolean isStepCounterSensorAvailable, isStepDetectorSensorAvailable;
    int stepcount,stepDetect;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // permission required to use activity recognition for our application
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        //access textview of UI in code
        textview1 = (TextView)findViewById(R.id.stepcount);
        textview2 = (TextView)findViewById(R.id.stepdetect);

        //access sensosrmanager object by checking Sensor Service over context object
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Keep the device screen in active/awake mode to test our application
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //check whether the TYPE_STEP_COUNTER id=19 is available or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            // access the TYPE_STEP_COUNTER to sensor object
            sensorCount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            // make the boolean variable true as sensor found in device
            isStepCounterSensorAvailable = true;
            Log.d("myStepAPP","Found Step Counter"+isStepCounterSensorAvailable);
        }
        // if sensor not found then keep the boolean variable false and display the error in textview
        else{
            // make the boolean variable false as sensor not found in device
            isStepCounterSensorAvailable = false;
            //Display error message to textview on UI
            textview1.setText("Step counter sensor not found on device");
        }

        //check whether the TYPE_STEP_DETECTOR id=18 is available or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            // access the TYPE_STEP_DETECTOR to sensor object
            sensorDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            // make the boolean variable true as sensor found in device
            isStepDetectorSensorAvailable=true;
            Log.d("myStepAPP","Found Step Detector"+isStepDetectorSensorAvailable);
        }
        // if sensor not found then keep the boolean variable false and display the error in textview
        else{
            // make the boolean variable false as sensor not found in device
            isStepDetectorSensorAvailable = false;
            //Display error message to textview on UI
            textview2.setText("Step detector sensor not found on device");
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("myStepAPP","Inside Sensor changed function call");

        //check the sensor type of TYPE_STEP_COUNTER and access value from last reboot
        if(sensorEvent.sensor == sensorCount){
            stepcount = (int) sensorEvent.values[0];
            Log.d("myStepAPP","StepCount value: "+stepcount);
            textview1.setText(String.valueOf(stepcount));
        }

        //check the sensor type of TYPE_STEP_DETECTOR and add the value to last value of step detected
        if(sensorEvent.sensor == sensorDetector){
            stepDetect = (int) (stepDetect + sensorEvent.values[0]);
            Log.d("myStepAPP","StepDetector value: "+stepDetect);
            textview2.setText(String.valueOf(stepDetect));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //register the sensor event listener in onStart function
    @Override
    protected void onStart() {
        super.onStart();

        // check whether the TYPE_STEP_COUNTER sensor is available in device to register or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null) {
            // if sensor detected then register it
            sensorManager.registerListener(this,sensorCount,SensorManager.SENSOR_DELAY_FASTEST);
            Log.d("myStepAPP","Step Counter registered  ");
        }
        // check whether the TYPE_STEP_DETECTOR sensor is available in device to register or not
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null) {
            // if sensor detected then register it
            sensorManager.registerListener(this,sensorDetector,SensorManager.SENSOR_DELAY_FASTEST);
            Log.d("myStepAPP","Step Detector registered  ");
        }
    }

    // unregister the sensor event listener in onStop function

    @Override
    protected void onStop() {
        super.onStop();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null) {
            // if sensor detected then unregister it
            sensorManager.unregisterListener(this);
            Log.d("myStepAPP","Step Counter unregistered  ");
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null) {
            // if sensor detected then unregister it
            sensorManager.unregisterListener(this);
            Log.d("myStepAPP","Step Detector unregistered  ");
        }
    }
}