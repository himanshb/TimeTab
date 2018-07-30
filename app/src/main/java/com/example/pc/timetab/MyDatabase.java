package com.example.pc.timetab;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabase extends SQLiteOpenHelper {

    String dbName;
    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dbName=name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytable(id integer primary key autoincrement,day varchar,subject varchar,teacher varchar,room integer,time_from varchar,time_to varchar,color integer,unique(day,time_from))");
    }
    protected boolean enterInDb(String day, String subject, String teacher, int room, String from, String to,int color){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("day",day);
        cv.put("subject",subject);
        cv.put("teacher",teacher);
        cv.put("room",room);
        cv.put("time_from",from);
        cv.put("time_to",to);
        cv.put("color",color);
        long check=db.insert("mytable",null,cv);
        return check>0;
    }

    protected String timeChecker(String from,String day){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select time_from from mytable where time_from=? and day=?",new String[]{from,day});
        if (cursor.getCount()>0){
            cursor.close();
            return "MATCHED";
        }
        else
            return "NULL";
    }

    protected Cursor getCursor(String day){
        SQLiteDatabase db=this.getReadableDatabase();
        return db.rawQuery("select * from mytable where day=? order by time_from",new String[]{day});
    }
    protected Cursor getCursorByID(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        return db.rawQuery("select * from mytable where id=?",new String[]{Integer.toString(id)});
    }
    protected void removeFromTable(String[] strings){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("mytable","id=?",strings);
        db.close();
    }

    protected  boolean updateTable(String day,String subject,String teacher,int room,String fromTime,String toTime,int id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("day",day);
        cv.put("subject",subject);
        cv.put("teacher",teacher);
        cv.put("room",room);
        cv.put("time_from",fromTime);
        cv.put("time_to",toTime);
        int check =db.update("mytable",cv,"id=?",new String[]{Integer.toString(id)});
        return check>0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
