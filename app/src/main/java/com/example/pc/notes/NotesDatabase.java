package com.example.pc.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class NotesDatabase extends SQLiteOpenHelper {
    public NotesDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes(note_id integer identity(1,1),note_name varchar,note_file_name varchar primary key,date varchar,color integer)");
    }

    protected boolean addNote(String noteName,String noteFileName,String date,int color){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("note_name",noteName);
        cv.put("note_file_name",noteFileName);
        cv.put("date",date);
        cv.put("color",color);
        long check=db.insert("notes",null,cv);
        return check>0;
    }

    protected Cursor getCursor(){
        SQLiteDatabase db=this.getReadableDatabase();
        return db.rawQuery("select note_name,note_file_name,date,color from notes order by date",null);
    }
    protected boolean deleteNote(String noteFileName){
        SQLiteDatabase db=this.getWritableDatabase();
        int check=db.delete("notes","note_file_name=?",new String[]{noteFileName});
        return check>0;
    }

    protected boolean updateNote(String noteName,String noteFileName,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("note_name",noteName);
        cv.put("date",date);
        int check=db.update("notes",cv,"note_file_name=?",new String[]{noteFileName});
        return check>0;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists notes");
        onCreate(db);
    }

}
