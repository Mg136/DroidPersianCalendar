package com.byagowi.persiancalendar.view.reminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.byagowi.persiancalendar.view.reminder.constants.Constants;
import com.byagowi.persiancalendar.view.reminder.model.ReminderDetails;
import com.byagowi.persiancalendar.view.reminder.model.ReminderUnit;
import com.byagowi.persiancalendar.service.ReminderAlert;

/**
 * @author MEHDI DIMYADI
 * MEHDIMYADI
 */
public class Reminder {

	public static void turnON(Context context, ReminderDetails event) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		long start_time_ms = event.getStartTime().getTime();
		long period_ms = PeriodToMs(context, event.getReminderPeriod());
		while (start_time_ms < System.currentTimeMillis())
			start_time_ms += period_ms;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time_ms,
				period_ms, prepareIntent(context, event.getId()));
	}

	public static void turnOFF(Context context, long event_id) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(prepareIntent(context, event_id));
	}

	private static PendingIntent prepareIntent(Context context, long event_id) {
		Intent intent = new Intent(context, ReminderAlert.class);
		intent.setAction(String.valueOf(event_id));
		intent.putExtra(Constants.EVENT_ID, event_id);
		return PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

	private static long PeriodToMs(Context context, ReminderUnit period) {
		long ms = 1000;
		int periodUnitPos = Utils.getUnit(context, period.getUnit());
		// Minutes
		if (periodUnitPos >= 0)
			ms *= 60;
		// Hours
		if (periodUnitPos >= 1)
			ms *= 60;
		// Days
		if (periodUnitPos >= 2)
			ms *= 24;
		// Weeks
		if (periodUnitPos >= 3)
			ms *= 7;
		ms *= period.getQuantity();
		return ms;
	}
}
