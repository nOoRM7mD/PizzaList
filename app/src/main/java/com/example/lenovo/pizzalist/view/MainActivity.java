package com.example.lenovo.pizzalist.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.pizzalist.MyApp;
import com.example.lenovo.pizzalist.R;
import com.example.lenovo.pizzalist.adapter.ProductAdapter;
import com.example.lenovo.pizzalist.models.Product;
import com.example.lenovo.pizzalist.presenter.BaseControllerListener;
import com.example.lenovo.pizzalist.presenter.BasePresenter;
import com.example.lenovo.pizzalist.presenter.Presenter;
import com.example.lenovo.pizzalist.view.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.lenovo.pizzalist.utilties.Network.isNetworkAvailable;

public class MainActivity extends BaseActivity implements InternetConnectionListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private TextView updatedTime;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String currentDateTimeString;
    private BaseControllerListener iControllerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iControllerListener = new Presenter(this);
        updatedTime = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(com.example.lenovo.pizzalist.view.MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new ProductAdapter(new ArrayList<Product>(), getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout =
                findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (isNetworkAvailable(getApplicationContext())) {
                iControllerListener.getDataFromURL(getApplicationContext());
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.time_pattern));
                currentDateTimeString = sdf.format(d);
                updatedTime.setText(getString(R.string.time_update) + currentDateTimeString);

                SharedPreferences prefs = getSharedPreferences("TIME", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("time", currentDateTimeString);
                editor.commit();
            } else {
                internetUnavailable();
            }
        });
        iControllerListener.getDataFromURL(getApplicationContext());
        ((MyApp) getApplication()).setConnectionListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MyApp) getApplication()).removeConnectionListener();
    }

    @Override
    protected BasePresenter setupPresenter() {
        return new Presenter(this);
    }


    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.pleasewait));
            progressDialog.setCanceledOnTouchOutside(false);
            //OnCancelListener
            progressDialog.setOnCancelListener(dialogInterface -> {
                finish();
            });
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(String message, List<Product> list) {
        mAdapter = new ProductAdapter((ArrayList<Product>) list, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message) {
        Log.d("Status", message);
    }

    protected void showSnackbar(String message, @NonNull View parentView) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                });
        View view = snackbar.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void internetUnavailable() {
        mSwipeRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();
        SharedPreferences prefs1 = getSharedPreferences("TIME", MODE_PRIVATE);
        String defaultValue = "0:00";
        String time = prefs1.getString("time", defaultValue);
        updatedTime.setText(getString(R.string.last_updated_time) + time);
        showSnackbar("Oh, network unavailable!", findViewById(R.id.container));

    }

    @Override
    public void cacheUnavailable() {
        showSnackbar("No Content Available", findViewById(R.id.container));
        progressDialog.dismiss();
    }
}
