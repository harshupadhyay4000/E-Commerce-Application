package com.example.e_commerceapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Timer extends AppCompatActivity {

    TextView minutestext;
    Button start;
    long totalTimeInMillis;
    CountDownTimer countDownTimer;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;
    int notificationId = 1;
    boolean isTimerRunning = false; // Flag to track if the timer is running

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        minutestext = findViewById(R.id.minutes_text);
        start = findViewById(R.id.starttimer);

        minutestext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    Toast.makeText(Timer.this, "Timer is running", Toast.LENGTH_SHORT).show();
                } else if (minutestext.getText().toString().isEmpty()) {
                    Toast.makeText(Timer.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Timer.this, "Timer Started", Toast.LENGTH_SHORT).show();
                    startTimer();
                }
            }
        });
    }

    private void showTimePicker() {
        int currentMinutes = 0;

        try {
            String[] parts = minutestext.getText().toString().split(":");
            currentMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        minutestext.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, currentMinutes / 60, currentMinutes % 60, true);
        timePickerDialog.show();
    }

    private void startTimer() {
        isTimerRunning = true; // Set the flag to indicate timer is running
        String timeString = minutestext.getText().toString();
        String[] parts = timeString.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        totalTimeInMillis = (minutes * 60 + seconds) * 1000; // Convert to milliseconds

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateNotification(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                updateNotification(0);
                vibrateDevice();
                showNotification("Timer Finished", "Time Completed");
                isTimerRunning = false; // Reset the flag after the timer finishes
            }
        };

        countDownTimer.start();
    }

    private void vibrateDevice() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, -1);
        }
    }

    private void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(notificationId, builder.build());
        }
    }

    private void updateNotification(long millisUntilFinished) {
        if (notificationBuilder == null) {
            notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.bell)
                    .setContentTitle("Timer")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOnlyAlertOnce(true);
        }

        int seconds = (int) (millisUntilFinished / 1000) % 60;
        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

        String content = String.format("%02d:%02d", minutes, seconds);

        notificationBuilder.setContentText(content);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (notificationManager != null) {
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }
}