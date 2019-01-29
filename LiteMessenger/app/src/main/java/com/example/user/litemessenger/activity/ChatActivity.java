package com.example.user.litemessenger.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.litemessenger.AppConfig;
import com.example.user.litemessenger.MenuActivity;
import com.example.user.litemessenger.R;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import com.example.user.litemessenger.R;

public class ChatActivity extends AppCompatActivity {
    ListView mListViewChat;
    EditText editTextFieldForMessage;
    ImageButton buttonSubmitSend;
    SQLiteDatabase chatDBlocal;
    String author, client;
    InsertInChat insertInChat; // класс отправляет новое сообщение на сервер
    UpdateReceiver upd_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
Intent intent = getIntent();
author = intent.getStringExtra("author");
client = intent.getStringExtra("client");

        buttonSubmitSend = (ImageButton) findViewById(R.id.buttonSubmitSend);
        editTextFieldForMessage = findViewById(R.id.editTextForMessage);
        mListViewChat = findViewById(R.id.listViewMessages);
        chatDBlocal = openOrCreateDatabase("chatDBlocal.db",
                Context.MODE_PRIVATE, null);
        chatDBlocal
                .execSQL("CREATE TABLE IF NOT EXISTS chat (_id integer primary key autoincrement, author, client, data, text)");

        // Создаём и регистрируем широковещательный приёмник

        upd_res = new UpdateReceiver();
        registerReceiver(upd_res, new IntentFilter(
                "com.example.action.UPDATE_ListView"));

        setListViewChat();

        buttonSubmitSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editTextFieldForMessage.getText().toString().trim().equals("")) {

                    // кнопку сделаем неактивной
                    buttonSubmitSend.setEnabled(false);

                    // если чтото есть - действуем!
                    insertInChat = new InsertInChat();
                    insertInChat.execute();

                } else {
                    // если ничего нет - нечего и писать
                    editTextFieldForMessage.setText("");
                }

            }
        });

    }
    @SuppressLint("SimpleDateFormat")
    public void setListViewChat() {

        Cursor cursor = chatDBlocal.rawQuery(
                "SELECT * FROM chat WHERE author = '" + author
                        + "' OR author = '" + client + "' ORDER BY data", null);
        if (cursor.moveToFirst()) {

            ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> hm;

            do {

                if (cursor.getString(cursor.getColumnIndex("author")).equals(
                        author)
                        && cursor.getString(cursor.getColumnIndex("client"))
                        .equals(client)) {

                    hm = new HashMap<>();
                    hm.put("author", author);
                    hm.put("client", "");
                    hm.put("list_client", "");
                    hm.put("list_client_time", "");
                    hm.put("list_author",
                            cursor.getString(cursor.getColumnIndex("text")));
                    hm.put("list_author_time", new SimpleDateFormat(
                            "HH:mm - dd.MM.yyyy").format(new Date(cursor
                            .getLong(cursor.getColumnIndex("data")))));
                    mList.add(hm);

                }


                if (cursor.getString(cursor.getColumnIndex("author")).equals(
                        client)
                        && cursor.getString(cursor.getColumnIndex("client"))
                        .equals(author)) {

                    hm = new HashMap<>();
                    hm.put("author", "");
                    hm.put("client", client);
                    hm.put("list_author", "");
                    hm.put("list_author_time", "");
                    hm.put("list_client",
                            cursor.getString(cursor.getColumnIndex("text")));
                    hm.put("list_client_time", new SimpleDateFormat(
                            "HH:mm - dd.MM.yyyy").format(new Date(cursor
                            .getLong(cursor.getColumnIndex("data")))));
                    mList.add(hm);

                }

            } while (cursor.moveToNext());

            // покажем lv
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                    mList, R.layout.message_layout, new String[] { "list_author",
                    "list_author_time", "list_client",
                    "list_client_time", "author", "client" },
                    new int[] { R.id.list_author, R.id.list_author_time,
                            R.id.list_client, R.id.list_client_time,
                            R.id.author, R.id.client });

            mListViewChat.setAdapter(adapter);
            cursor.close();

        }

        Log.i("chat",
                "+ ChatActivity ======================== обновили поле чата");

    }


    public void onButtonPressed() {

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }



    private class InsertInChat extends AsyncTask<Void, Void, Integer> {

        HttpURLConnection conn;
        Integer res;


        protected Integer doInBackground(Void... params) {

            try {

                // соберем линк для передачи новой строки
                String post_url = AppConfig.SERVER_NAME
                        + "/chat.php?action=insert&author="
                        + URLEncoder.encode(author ,"UTF-8")
                        + "&client="
                        + URLEncoder.encode(client, "UTF-8")
                        + "&text="
                        + URLEncoder.encode(editTextFieldForMessage.getText().toString().trim(),
                        "UTF-8");

                Log.i("chat",
                        "+ ChatActivity - отправляем на сервер новое сообщение: "
                                + editTextFieldForMessage.getText().toString().trim() + "  " + post_url);

                URL url = new URL(post_url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000); // ждем 10сек
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.connect();

                res = conn.getResponseCode();
                Log.i("chat", "+ ChatActivity - ответ сервера (200 - все ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("chat",
                        "+ ChatActivity - ошибка соединения: " + e.getMessage());

            } finally {
                // закроем соединение
                conn.disconnect();
            }
            return res;
        }

        protected void onPostExecute(Integer result) {

            try {
                if (result == 200) {
                    Log.i("chat", "+ ChatActivity - сообщение успешно ушло.");
                    // сбросим набранный текст
                    editTextFieldForMessage.setText("");
                }
            } catch (Exception e) {
                Log.i("chat", "+ ChatActivity - ошибка передачи сообщения:\n"
                        + e.getMessage());
                Toast.makeText(getApplicationContext(),
                        "ошибка передачи сообщения", Toast.LENGTH_SHORT).show();
            } finally {
                // активируем кнопку
                buttonSubmitSend.setEnabled(true);
            }
        }
    }
    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("chat",
                    "+ ChatActivity - ресивер получил сообщение - обновим ListView");
            setListViewChat();
        }
        public void onBackPressed() {
            Log.i("chat", "+ ChatActivity - закрыт");
            unregisterReceiver(upd_res);
            finish();
        }
    }

    // выходим из чата


}



