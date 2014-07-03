package com.lad.reminder;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderMainActivity extends ActionBarActivity {
	private EditText timeEditText, dateEditText, titleEditText,
			descriptionEditText;
	private Calendar timeReminder;
	private SharedPreferences sharedPreference;
	public final static String SAVE_TIME = "time_reminder";
	public final static String SAVE_TITLE = "title_reminder";
	public final static String SAVE_DESC = "desc_reminder";
	private AlarmReceiver alarmReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_main_layout);
		timeEditText = (EditText) findViewById(R.id.timeEditText);
		dateEditText = (EditText) findViewById(R.id.dateEditText);
		titleEditText = (EditText) findViewById(R.id.titleEditText);
		descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
		sharedPreference = getSharedPreferences("save_data", MODE_PRIVATE);
		timeReminder = Calendar.getInstance();
		alarmReceiver = new AlarmReceiver();
		loadData();

	}

	private void saveData() {
		Editor editor = sharedPreference.edit();
		editor.putLong(SAVE_TIME, timeReminder.getTimeInMillis());
		editor.putString(SAVE_TITLE, titleEditText.getText().toString());
		editor.putString(SAVE_DESC, descriptionEditText.getText().toString());
		editor.commit();
	}

	private void loadData() {

		timeReminder.setTimeInMillis(sharedPreference.getLong(SAVE_TIME,
				System.currentTimeMillis()));
		titleEditText.setText(sharedPreference.getString(SAVE_TITLE, ""));
		timeEditText.setText(timeReminder.get(Calendar.HOUR_OF_DAY) + ":"
				+ timeReminder.get(Calendar.MINUTE));
		dateEditText.setText(timeReminder.get(Calendar.DAY_OF_MONTH) + "/"
				+ timeReminder.get(Calendar.MONTH) + "/"
				+ timeReminder.get(Calendar.YEAR));
		descriptionEditText.setText(sharedPreference.getString(SAVE_DESC, ""));

	}

	public void onClickTimeDialog(View v) {
		if (timeReminder.getTimeInMillis() <= System.currentTimeMillis())
			timeReminder.setTimeInMillis(System.currentTimeMillis());
		new TimePickerDialog(this, timeListener,
				timeReminder.get(Calendar.HOUR_OF_DAY),
				timeReminder.get(Calendar.MINUTE), true).show();
	}

	TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeReminder.set(Calendar.HOUR_OF_DAY, hourOfDay);
			timeReminder.set(Calendar.MINUTE, minute);
			timeReminder.set(Calendar.SECOND, 0);
			timeEditText.setText(hourOfDay + ":" + minute);
		}

	};

	public void onClickDateDialog(View v) {
		if (timeReminder.getTimeInMillis() <= System.currentTimeMillis())
			timeReminder.setTimeInMillis(System.currentTimeMillis());
		new DatePickerDialog(this, dateListener,
				timeReminder.get(Calendar.YEAR),
				timeReminder.get(Calendar.MONTH),
				timeReminder.get(Calendar.DAY_OF_MONTH)).show();
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			timeReminder.set(Calendar.YEAR, year);
			timeReminder.set(Calendar.MONTH, monthOfYear);
			timeReminder.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateEditText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

		}

	};

	public void onClickSave(View v) {
		if ((titleEditText.getText().toString().matches("")))
			Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT)
					.show();
		else if (timeReminder.getTimeInMillis() <= System.currentTimeMillis() + 1000)
			Toast.makeText(this, "Time not correct", Toast.LENGTH_SHORT).show();
		else {
			saveData();
			alarmReceiver.setAlarm(this);
			Toast.makeText(this, "Reminder activated", Toast.LENGTH_SHORT)
					.show();

		}

	}

}
