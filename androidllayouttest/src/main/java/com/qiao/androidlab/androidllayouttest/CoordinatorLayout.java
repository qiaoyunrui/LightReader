package com.qiao.androidlab.androidllayouttest;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Administrator on 2015/11/30.
 */
public class CoordinatorLayout extends AppCompatActivity {

    FloatingActionButton fab_btn;
    android.support.design.widget.CoordinatorLayout root;
    android.support.v7.widget.Toolbar toolBar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);
        fab_btn = (FloatingActionButton) findViewById(R.id.fab_btn);
        root = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.root);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarlayout);
        collapsingToolbarLayout.setTitle("HELLO_WORLD");
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(root, "HELLO", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
