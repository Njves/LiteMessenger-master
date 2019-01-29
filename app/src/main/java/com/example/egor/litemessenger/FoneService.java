package com.example.egor.litemessenger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FoneService extends Service {

	// ��� ������� (url ������������������� ���� �����)
	// �������� http://l29340eb.bget.ru
    SQLiteDatabase chatDBlocal;
    HttpURLConnection conn;
    Cursor cursor;
    Thread thr;
    ContentValues new_mess;
    Long last_time;
    public FoneService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("chat", "+ FoneService - Запущен");

        chatDBlocal = openOrCreateDatabase("chatDBlocal.db",
                Context.MODE_PRIVATE, null);
        chatDBlocal
                .execSQL("CREATE TABLE IF NOT EXISTS chat (_id integer primary key autoincrement, author, client, data, text)");


        Intent iN = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pI = PendingIntent.getActivity(getApplicationContext(),
                0, iN, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder bI = new Notification.Builder(
                getApplicationContext());

        bI.setContentIntent(pI)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getApplicationContext()
                                .getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Работает...");

        Notification notification = bI.build();
        startForeground(101, notification);

        startLoop();
        return START_REDELIVER_INTENT;
    }
    private void startLoop() {

        thr = new Thread(new Runnable() {


            String answerHTTP, link;

            public void run() {

                while (true) {
                    cursor = chatDBlocal.rawQuery(
                            "SELECT * FROM chat ORDER BY data", null);


                    if (cursor.moveToLast()) {
                        last_time = cursor.getLong(cursor
                                .getColumnIndex("data"));
                        link = "http://litemessenger.ru" + "/chat.php?action=select&data="
                                + last_time.toString();


                    } else {
                        link = "http://litemessenger.ru" + "/chat.php?action=select";
                    }

                    cursor.close();


                    try {
                        Log.i("chat",
                                "+ FoneService --------------- ОТКРОЕМ СОЕДИНЕНИЕ");


                        conn = (HttpURLConnection) new URL(link)
                                .openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        conn.setDoInput(true);
                        conn.connect();

                    } catch (Exception e) {
                        Log.i("chat", "+ FoneService ошибка: " + e.getMessage());

                    }

                    try {
                        InputStream is = conn.getInputStream();
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String bfr_st = null;
                        while ((bfr_st = br.readLine()) != null) {
                            sb.append(bfr_st);
                        }

                        Log.i("chat", "+ FoneService - полный ответ сервера:\n"
                                + sb.toString());


                        answerHTTP = sb.toString();
                        answerHTTP = answerHTTP.substring(0, answerHTTP.indexOf("]") + 1);

                        is.close();
                        br.close();

                    } catch (Exception e) {
                        Log.i("chat", "+ FoneService ошибка: " + e.getMessage());


                    } finally {
                        conn.disconnect();
                        Log.i("chat",
                                "+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");


                    }

                    // ������� ����� � �� ---------------------------------->
                    if (answerHTTP != null && !answerHTTP.trim().equals("")) {

                        Log.i("chat",
                                "+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");

                        try {


                            JSONArray ja = new JSONArray(answerHTTP);
                            JSONObject jo;

                            Integer i = 0;

                            while (i < ja.length()) {


                                jo = ja.getJSONObject(i);

                                Log.i("chat",
                                        "=================>>> "
                                                + jo.getString("author")
                                                + " | "
                                                + jo.getString("client")
                                                + " | " + jo.getLong("data")
                                                + " | " + jo.getString("text"));

                                // �������� ����� ���������
                                new_mess = new ContentValues();
                                new_mess.put("author", jo.getString("author"));
                                new_mess.put("client", jo.getString("client"));
                                new_mess.put("data", jo.getLong("data"));
                                new_mess.put("text", jo.getString("text"));
                                // ������� ����� ��������� � ��
                                chatDBlocal.insert("chat", null, new_mess);
                                new_mess.clear();

                                i++;


                                sendBroadcast(new Intent(
                                        "by.com.example.litemessenger.action.UPDATE_ListView"));
                            }
                        } catch (Exception e) {
                            // ���� ����� ������� �� �������� �������� JSON
                            Log.i("chat",
                                    "+ FoneService ---------- ошибка ответа сервера:\n"
                                            + e.getMessage());

                        }
                    } else {
                        // ���� ����� ������� ������
                        Log.i("chat",
                                "+ FoneService ---------- ответ не содержит JSON!");

                    }

                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.i("chat",
                                "+ FoneService - ������ ��������: "
                                        + e.getMessage());
                    }
                }
            }
        });

        thr.setDaemon(true);
        thr.start();

    }
}
