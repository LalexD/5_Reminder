package com.lad.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent notificationIntent = new Intent(context,
				ReminderMainActivity.class);
		SharedPreferences sharedPreference = context.getSharedPreferences(
				"save_data", Context.MODE_PRIVATE);
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(
						sharedPreference.getString(
								ReminderMainActivity.SAVE_TITLE, ""))
				.setContentText(
						sharedPreference.getString(
								ReminderMainActivity.SAVE_DESC, ""))
				.setContentIntent(
						PendingIntent.getActivity(context, 0,
								notificationIntent,
								PendingIntent.FLAG_CANCEL_CURRENT))
				.setAutoCancel(true)
				.setTicker(
						sharedPreference.getString(
								ReminderMainActivity.SAVE_TITLE, ""));

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, nBuilder.build());

		// maybe it bad, but i found no other way

		PowerManager pm = (PowerManager) context.getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = pm
				.newWakeLock(
						(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
								| PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
						"TAG");
		wakeLock.acquire();

		/*
		 * or may be so pm.wakeUp(10000); but it not work((
		 */
	}

	public void setAlarm(Context context) {
		SharedPreferences sharedPreference = context.getSharedPreferences(
				"save_data", Context.MODE_PRIVATE);
		if (sharedPreference.getLong(ReminderMainActivity.SAVE_TIME, 0) > System
				.currentTimeMillis()) {
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent pi = PendingIntent
					.getBroadcast(context, 0, intent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					sharedPreference.getLong(ReminderMainActivity.SAVE_TIME,
							System.currentTimeMillis()), pi);

		}
	}
}
