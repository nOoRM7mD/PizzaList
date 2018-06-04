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
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends BaseActivity {

    public static final String BASE_URL = "https://api.androidhive.info/";
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
        updatedTime = (TextView) findViewById(R.id.textView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(com.example.lenovo.pizzalist.view.MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new ProductAdapter(new ArrayList<Product>(), getApplicationContext());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)
                findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if (isNetworkAvailable(getApplicationContext())) {
                    iControllerListener.getDataFromURL(getApplicationContext(), "");
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    currentDateTimeString = sdf.format(d);
                    updatedTime.setText("Updated " + currentDateTimeString);

                    SharedPreferences prefs = getSharedPreferences("TIME", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("time", currentDateTimeString);
                    editor.commit();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    SharedPreferences prefs1 = getSharedPreferences("TIME", MODE_PRIVATE);
                    String defaultValue = "defaultName";
                    String time = prefs1.getString("time", defaultValue);
                    updatedTime.setText("Last Updated " + time);
                    showSnackbar("Oh, network unavailable!", findViewById(R.id.container));

                }
            }
        });
        iControllerListener.getDataFromURL(getApplicationContext(),  "");

        if (!isNetworkAvailable(getApplicationContext())) {
            showSnackbar("Oh, network unavailable!", findViewById(R.id.container));
        }

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
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
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
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        View view = snackbar.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
