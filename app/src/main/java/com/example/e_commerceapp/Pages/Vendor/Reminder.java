package com.example.e_commerceapp.Pages.Vendor;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.e_commerceapp.NotificationReceiver;
import com.example.e_commerceapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reminder extends AppCompatActivity {

    EditText text;
    TextView dateText;
    TextView timeText;
    Calendar calendar;
    Button send;
    int notificationId = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        text = findViewById(R.id.text_data);
        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);
        send = findViewById(R.id.final_btn);

        calendar = Calendar.getInstance();

        findViewById(R.id.calender_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        findViewById(R.id.time_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Reminder.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Reminder.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                } else {
                    scheduleNotification();
                }
            }
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Validate if the selected date is equal to or after the current date
            if (!selectedDate.before(Calendar.getInstance()) || selectedDate.equals(Calendar.getInstance())) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText();
            } else {
                // Show a message indicating that the selected date is invalid
                Toast.makeText(Reminder.this, "Please select a date in the future", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showTimePicker() {
        new TimePickerDialog(this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
                .show();
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);


            if (!selectedTime.before(Calendar.getInstance()) || selectedTime.equals(Calendar.getInstance())) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeText();
            } else {

                Toast.makeText(Reminder.this, "Please select a time in the future", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void updateDateText() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateText.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeText() {
        String timeFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        timeText.setText(sdf.format(calendar.getTime()));
    }

    private void scheduleNotification() {
        String notificationText = text.getText().toString();

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("notification_text", notificationText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Notification scheduled", Toast.LENGTH_SHORT).show();
    }

}