package com.example.user.litemessenger;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.litemessenger.activity.ChatActivity;
import com.example.user.litemessenger.service.FoneService;

import java.net.HttpURLConnection;
import java.net.URL;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Поменять имя переменным
    Spinner mSpinnerAuthor, mSpinnerClient;
    String author, client;
    Button btnOpenChat, btnOpenChatReverse, btnDeleteServerChat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnOpenChat =  findViewById(R.id.open_chat_btn);
        btnOpenChatReverse =  findViewById(R.id.open_chat_reverce_btn);
        btnDeleteServerChat =  findViewById(R.id.delete_server_chat);

        //Запус сервера
        this.startService(new Intent(this, FoneService.class));


        mSpinnerAuthor = (Spinner) findViewById(R.id.spinner_author);
        mSpinnerClient = (Spinner) findViewById(R.id.spinner_client);

        mSpinnerAuthor.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Оля", "Даниил", "Петя", "Никита", "Влад", "Артём", "Егор",}));
        mSpinnerClient.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Оля", "Даня", "Петя", "Никита", "Влад", "Артём", "Егор",}));
        mSpinnerClient.setSelection(5);

        btnOpenChat.setText("Открыть чат: " + mSpinnerAuthor.getSelectedItem().toString() + " > " + mSpinnerClient.getSelectedItem().toString());
        btnOpenChatReverse.setText("Открыть чат: " + mSpinnerClient.getSelectedItem().toString() + " > " + mSpinnerAuthor.getSelectedItem().toString());
        mSpinnerAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

                author = mSpinnerAuthor.getSelectedItem().toString();

                btnOpenChat.setText("Открыть чат: " + mSpinnerAuthor.getSelectedItem().toString() + " > " + mSpinnerClient.getSelectedItem().toString());
                btnOpenChatReverse.setText("Открыть чат: " + mSpinnerClient.getSelectedItem().toString() + " > " + mSpinnerAuthor.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

                client = mSpinnerClient.getSelectedItem().toString();

                btnOpenChat.setText("Открыть чат: " + mSpinnerAuthor.getSelectedItem().toString() + " > " + mSpinnerClient.getSelectedItem().toString());
                btnOpenChatReverse.setText("Открыть чат: " + mSpinnerClient.getSelectedItem().toString() + " > " + mSpinnerAuthor.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
btnOpenChat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        if (author.equals(client)) {
            Toast.makeText(getApplicationContext(), "Нельзя отправить сообщение себе!", Toast.LENGTH_SHORT).show();
        } else {

  Log.i("Njves", author + " " + client);
Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
intent.putExtra("author", author);
intent.putExtra("client", client);
startActivity(intent);
        }
    }
});
btnOpenChatReverse.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (author.equals(client)) {

            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
        } else {

            Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
            intent.putExtra("author", client);
            intent.putExtra("client", author);
            startActivity(intent);
        }
    }
});
btnDeleteServerChat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Log.i("chat", "+ Menu Activity - Все чаты с сервера были удалены");

        btnDeleteServerChat.setEnabled(false);
        btnDeleteServerChat.setText(R.string.chatsIsdelete);

        DeleteFromChat deleteFromChat = new DeleteFromChat();
        deleteFromChat.execute();
    }
});
    }






    public void deleteLocalChat(View v) {

        Log.i("chat", "+ MainActivity - все чаты с локального устройства были удалены");

        SQLiteDatabase chatDBlocal;
        chatDBlocal = openOrCreateDatabase("chatDBlocal.db", Context.MODE_PRIVATE, null);
        chatDBlocal.execSQL("drop table chat");
        chatDBlocal.execSQL("CREATE TABLE IF NOT EXISTS chat (_id integer primary key autoincrement, author, client, data, text)");

        Toast.makeText(getApplicationContext(), R.string.chatsIsMobileDelete, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_camera) {


        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class DeleteFromChat extends AsyncTask<Void, Void, Integer> {

        Integer res;
        HttpURLConnection conn;

        protected Integer doInBackground(Void... params) {

            try {
                URL url = new URL(AppConfig.SERVER_NAME + "/chat.php?action=delete");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000); //
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.connect();
                res = conn.getResponseCode();
                Log.i("chat", "+ MainActivity - MainActivity - ответ сервера (200 = ОК): " + res.toString());

            } catch (Exception e) {
                Log.i("chat", "+ MainActivity - MainActivity - ответ сервера ОШИБКА: " + e.getMessage());
            } finally {
                conn.disconnect();
            }

            return res;
        }

        protected void onPostExecute(Integer result) {

            try {
                if (result == 200) {
                    Toast.makeText(getApplicationContext(), "Чат на сервере удалён", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка выполнения запроса.", Toast.LENGTH_SHORT).show();
            } finally {

                btnDeleteServerChat.setEnabled(true);
                btnDeleteServerChat.setText("Удалить	все чаты на сервере");
            }
        }
    }
}


