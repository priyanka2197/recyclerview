package com.example.testapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.adapter.UserRecyclerViewAdapter;
import com.example.testapp.model.ResponseDataItem;
import com.example.testapp.network.APIClient;
import com.example.testapp.network.APIMethod;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ActionBar actionBar;
    UserRecyclerViewAdapter adapter;
    List<ResponseDataItem> userdatalist = new ArrayList<>();
    TextView netConn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netConn = findViewById(R.id.netconnection);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);

        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00BDFF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            netConn.setVisibility(View.GONE);

        }else {
            netConn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        callUserInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });
        return true;
    }


    private void filter(String text) {
        List<ResponseDataItem> filteredlist = new ArrayList<>();
        for (ResponseDataItem item : userdatalist) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }


    private void callUserInfo() {

        Call<List<ResponseDataItem>> call = APIClient.getClient().create(APIMethod.class).getUserData();
        call.enqueue(new Callback<List<ResponseDataItem>>() {
            @Override
            public void onResponse(Call<List<ResponseDataItem>> call, Response<List<ResponseDataItem>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, String.format("onResponse: success" + new Gson().toJson(response.body())));

                    progressBar.setVisibility(View.GONE);
                    userdatalist = response.body();
                    prepareRecyclerView();

                }
            }

            @Override
            public void onFailure(Call<List<ResponseDataItem>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void prepareRecyclerView() {
        if (userdatalist != null) {
            adapter = new UserRecyclerViewAdapter(getApplicationContext(), userdatalist);
            adapter.setOnItemClickListener(new UserRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(MainActivity.this, "Item clicked at position " + position, Toast.LENGTH_SHORT).show();
                }
            });
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(MainActivity.this, "Data Not Loaded. Try again later!!!..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
