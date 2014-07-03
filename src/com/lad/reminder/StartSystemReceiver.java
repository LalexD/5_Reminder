package com.lad.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartSystemReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		AlarmReceiver alarmReceiver = new AlarmReceiver();

		alarmReceiver.setAlarm(context);

	}

}
