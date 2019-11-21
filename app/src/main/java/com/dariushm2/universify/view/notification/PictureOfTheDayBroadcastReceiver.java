package com.dariushm2.universify.view.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.AlarmClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.view.MainActivity;

import java.util.Calendar;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static androidx.core.app.NotificationCompat.DEFAULT_LIGHTS;
import static androidx.core.app.NotificationCompat.DEFAULT_SOUND;
import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;

public class PictureOfTheDayBroadcastReceiver extends BroadcastReceiver {


    private Context context;

    private void setContext(Context context) {
        this.context = context;
    }
    private Context getContext() {
        return this.context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(App.TAG, "onReceive");
        setContext(context);
        getImage();

    }

    private void getImage() {
        NasaServices.NASA_SERVICES
                .getPictureOfTheDay(NasaServices.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PictureOfTheDay>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //d.dispose();
                    }

                    @Override
                    public void onSuccess(Response<PictureOfTheDay> response) {

                        if (response.body() != null) {

                            PictureOfTheDay pictureOfTheDay = response.body();
                            notifyUser(pictureOfTheDay);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void notifyUser(PictureOfTheDay pictureOfTheDay) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        //intent.putExtra("requestCode", 3);

        //i.putExtra(ID, cPayment.getInt(1));

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getContext(),
                        3,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), getContext().getString(R.string.pictureOfTheDayNotificationChannelId));

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(pictureOfTheDay.getExplanation());
        bigText.setBigContentTitle(pictureOfTheDay.getTitle());
        bigText.setSummaryText(pictureOfTheDay.getExplanation());

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(pictureOfTheDay.getTitle());
        mBuilder.setContentText(pictureOfTheDay.getExplanation());
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigText);

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null)
        {
            String channelId = getContext().getString(R.string.pictureOfTheDayNotificationChannelId);
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    getContext().getString(R.string.pictureOfTheDayNotificationChannelId),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }


        int id = (String.valueOf(Calendar.getInstance().getTimeInMillis())).hashCode();

        if (notificationManager != null) {
            Log.e(App.TAG, "notifyUser");
            notificationManager.notify(id, mBuilder.build());
        }

    }

    public static void setAlarm(Context context) {


        Calendar calendar = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PictureOfTheDayBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarmManager != null)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, PictureOfTheDayBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.cancel(sender);
    }
}
