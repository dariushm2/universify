package com.dariushm2.universify.view.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.model.PictureOfTheDay;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.view.pictureOfTheDay.ScrollingActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PictureOfTheDayBroadcastReceiver extends BroadcastReceiver {


    private Context context;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String date = prefs.getString(context.getString(R.string.pictureOfTheDayNotificationChannelId), null);
        if (date != null)
            if (date.equals(dateFormat.format(calendar.getTime())))
                return;
        getImage();

    }

    private void getImage() {

        App app = (App) context.getApplicationContext();
        app.getRetrofitFor(NasaServices.BASE_URL_PICTURE_OF_THE_DAY)
                .getPictureOfTheDay(NasaServices.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(response -> {
                    if (response.body() != null) {
                        PictureOfTheDay pictureOfTheDay = response.body();
                        notifyUser(pictureOfTheDay);
                    }
                })
                .doOnError(throwable -> Log.e(App.TAG, throwable.getMessage()))
                //.doOnSubscribe(Disposable::dispose)
                .subscribe();
    }

    private void notifyUser(PictureOfTheDay pictureOfTheDay) {
        Intent intent = new Intent(getContext(), ScrollingActivity.class);
        intent.putExtra(PictureOfTheDay.PICTURE_OF_THE_DAY, pictureOfTheDay);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getContext(),
                        ScrollingActivity.ON_NOTIFICATION_CLICK,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), getContext().getString(R.string.pictureOfTheDayNotificationChannelId));

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(pictureOfTheDay.getTitle());
        bigPictureStyle.setSummaryText("NASA's Picture Of The Day");

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_black_hole);
        mBuilder.setContentTitle(pictureOfTheDay.getTitle());
        mBuilder.setContentText(pictureOfTheDay.getExplanation());
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigPictureStyle);

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            String channelId = getContext().getString(R.string.pictureOfTheDayNotificationChannelId);
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    getContext().getString(R.string.pictureOfTheDayNotificationChannelId),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        int id = (String.valueOf(Calendar.getInstance().getTimeInMillis())).hashCode();

        Glide.with(getContext())
                .asBitmap()
                .load(pictureOfTheDay.getUrl())
                .into(new CustomTarget<Bitmap> (){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bigPictureStyle.bigPicture(resource);
                        if (notificationManager != null) {
                            Log.e(App.TAG, "notifyUser");
                            notificationManager.notify(id, mBuilder.build());

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            prefs.edit().putString(context.getString(R.string.pictureOfTheDayNotificationChannelId),
                                   dateFormat.format(calendar.getTime()))
                            .apply();

                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    public static void setAlarm(Context context) {



        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 21);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);

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
