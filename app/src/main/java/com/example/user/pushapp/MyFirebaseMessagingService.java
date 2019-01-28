package com.example.user.pushapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.user.pushapp.constant.ServerUrls;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * App을 최초로 다운로드 받아 Token이 생성되거나
     * 이 외의 Token이 새로 갱신되는 경우 호출되는 메소드
     * (즉, FCM서버가 생성된 Token을 callback으로 알려주는 메소드)
     * @param token 생성된 token 값
     */
    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    /**
     * onNewToken으로 부터 받은 Token 값과 Phone Num을 Server로 보내는 메소드
     * @param token onNewToken 콜백 메소드에서 전달받은 token값
     */
    private void sendRegistrationToServer(String token) {
        //핸드폰 번호 가져 오기
        String phoneNum = getPhoneNumber();

        // 서버로 토큰과 번호 보내기
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNum);
            jsonObject.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());

        Request request = new Request.Builder()
                .url(ServerUrls.PUSHSERVER_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@@@@@@@@ERROR", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful())
                    Log.d("@@@@@@@@@@SUCCESS", response.body().string());
                else
                    Log.d("@@@@@@@@@@@onResponse" , response.body().string());
            }
        });
    }

    /**
     * FCM 서버로 부터 메시지를 받았을 경우 호출되는 메소드
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String surveyUrl = remoteMessage.getData().get("data");
        // FCM서버로 부터 수신한 메시지중 data라는 키를 가진 값을 꺼내어 메소드로 파라미터로 넘겨준다
        sendNotification(surveyUrl);
    }

    /**
     * Android 핸드폰에 알림을 띄워주는 메소드
     *
     */
    private void sendNotification(String surveyUrl) {
        Uri uri = Uri.parse(surveyUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Survey")
                        .setContentText("설문 조사에 응해주세요!")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel channel = new NotificationChannel(channelId, "jy", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * 디바이스의 전화번호를 받아오는 메소드
     * @return
     */
    private String getPhoneNumber() {
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = "";
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                return "";

            phoneNumber = telephony.getLine1Number();
            phoneNumber = phoneNumber.replace("+82","0");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }
}