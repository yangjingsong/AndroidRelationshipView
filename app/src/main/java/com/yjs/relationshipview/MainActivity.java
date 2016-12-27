package com.yjs.relationshipview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final RelationshipView circlesView = (RelationshipView) findViewById(R.id.circlesView);
        List<DataBean> list = new ArrayList<>();

        for (int i=0;i<3;i++){
            list.add(new DataBean());
        }
        circlesView.setStories(list,new DataBean());
        circlesView.setOnClickStoryListener(new RelationshipView.OnClickStoryListener() {
            @Override
            public void onClickStory(DataBean story) {
                List<DataBean> list = new ArrayList<>();

                for (int i=0;i<5;i++){
                    list.add(new DataBean());
                }
                circlesView.setStories(list,story);
            }
        });





    }


}
