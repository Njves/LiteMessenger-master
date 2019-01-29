package com.example.egor.litemessenger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.egor.litemessenger.Models.Client;
import com.example.egor.litemessenger.Models.NetworkService;
import com.example.egor.litemessenger.Requests.GetClients;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
	String server_name = "http://litemessenger.ru";


	Spinner spinner_author, spinner_client;
	String author, client;
	Button open_chat_btn, open_chat_reverce_btn;
	GetClients getClients;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i("chat", "+ MainActivity - запуск приложения");

		open_chat_btn = findViewById(R.id.open_chat_btn);
		open_chat_reverce_btn = findViewById(R.id.open_chat_reverce_btn);


		// запустим FoneService
		this.startService(new Intent(this, FoneService.class));

		// заполним 2 выпадающих меню для выбора автора и получателя сообщения
		// 5 мужских и 5 женских имен
		// установим слушателей
		spinner_author = findViewById(R.id.spinner_author);
		spinner_client = findViewById(R.id.spinner_client);
        getClients = NetworkService.getInstance().getRetrofit().create(GetClients.class);
        Call<List<Client>> call = getClients.getClients();
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                Log.i(TAG + "test", response.body().toString());
                List<Client> list = response.body();
                Log.d(TAG, list.toString());

            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.d(TAG, "Request is "+t.getMessage());
                Log.d(TAG, t.toString());
            }
        });
		spinner_author.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "Петя",
				"Вася", "Коля", "Андрей", "Сергей", "Оля", "Лена",
				"Света", "Марина", "Наташа" }));
		spinner_client.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "Петя",
				"Вася", "Коля", "Андрей", "Сергей", "Оля", "Лена",
				"Света", "Марина", "Наташа" }));
		spinner_client.setSelection(5);

		open_chat_btn.setText("Открыть чат: "
				+ spinner_author.getSelectedItem().toString() + " > "
				+ spinner_client.getSelectedItem().toString());
		open_chat_reverce_btn.setText("Открыть чат: "
				+ spinner_client.getSelectedItem().toString() + " > "
				+ spinner_author.getSelectedItem().toString());

		spinner_author
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
											   View itemSelected, int selectedItemPosition,
											   long selectedId) {

						author = spinner_author.getSelectedItem().toString();

						open_chat_btn.setText("Открыть чат: "
								+ spinner_author.getSelectedItem().toString()
								+ " > "
								+ spinner_client.getSelectedItem().toString());
						open_chat_reverce_btn.setText("Открыть чат: "
								+ spinner_client.getSelectedItem().toString()
								+ " > "
								+ spinner_author.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		spinner_client
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
											   View itemSelected, int selectedItemPosition,
											   long selectedId) {

						client = spinner_client.getSelectedItem().toString();

						open_chat_btn.setText("Открыть чат: "
								+ spinner_author.getSelectedItem().toString()
								+ " > "
								+ spinner_client.getSelectedItem().toString());
						open_chat_reverce_btn.setText("Открыть чат: "
								+ spinner_client.getSelectedItem().toString()
								+ " > "
								+ spinner_author.getSelectedItem().toString());
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
	}

	// откроем чат с выбранным автором и получателем
	public void open_chat(View v) {
		// быстрая проверка
		if (author.equals(client)) {
			// если автор и получатель одинаковы
			// чат не открываем
			Toast.makeText(this, "author = client !", Toast.LENGTH_SHORT)
					.show();
		} else {
			// откроем нужный чат author > client
			Intent intent = new Intent(MainActivity.this, ChatActivity.class);
			intent.putExtra("author", author);
			intent.putExtra("client", client);
			startActivity(intent);
		}
	}

	// откроем чат с выбранным автором и получателем, только наоборот
	public void open_chat_reverce(View v) {
		// быстрая проверка
		if (author.equals(client)) {
			// если автор и получатель одинаковы
			// чат не открываем
			Toast.makeText(this, "author = client !", Toast.LENGTH_SHORT)
					.show();
		} else {
			// откроем нужный чат client > author
			Intent intent = new Intent(MainActivity.this, ChatActivity.class);
			intent.putExtra("author", client);
			intent.putExtra("client", author);
			startActivity(intent);
		}
	}


	public void onBackPressed() {

	}
}