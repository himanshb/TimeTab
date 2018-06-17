package com.example.pc.timetab;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;

public class MonActivity extends AppCompatActivity implements View.OnClickListener {

    MyDatabase myDatabase;
    String day;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<ListItem> listItems;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon);
        //get intent"Day"
        day=this.getIntent().getExtras().getString("day");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(day);

        myDatabase=new MyDatabase(this,"Time_Table_DB",null,1);
        fab=(FloatingActionButton)this.findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void createCards(){
        cursor=myDatabase.getCursor(day);
        listItems=new ArrayList<>();
        recyclerView=(RecyclerView)this.findViewById(R.id.recyclerViewMon);
        while (cursor.moveToNext()){
            String subject=cursor.getString(cursor.getColumnIndex("subject"));
            String teacher=cursor.getString(cursor.getColumnIndex("teacher"));
            int room=cursor.getInt(cursor.getColumnIndex("room"));
            int color=cursor.getInt(cursor.getColumnIndex("color"));
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String from=cursor.getString(cursor.getColumnIndex("time_from"));
            String to=cursor.getString(cursor.getColumnIndex("time_to"));
            ListItem listItem=new ListItem(subject,teacher,room,from,to);
            listItem.setDay(day);
            listItem.setColor(color);
            listItem.setId(id);
            listItems.add(listItem);
        }
        adapter=new MyAdapter(this,listItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,FormActivity.class);
        intent.putExtra("day",day);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCards();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabase.close();
    }
}
