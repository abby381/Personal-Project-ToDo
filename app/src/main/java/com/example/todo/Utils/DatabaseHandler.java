package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public Context Ctx;
    private static  final int VERSION = 1;
    private static final String NAME = "todoListDatabase";
    private static final String TODO_TABLE = "TODO_TABLE"; //"todo"; // ano sabi? di nagssave pag naginput ako hahaha
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE TODO_TABLE(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, status TEXT)"; // okay na to hindi na to error
            //"CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
              //                              + TASK + "TEXT, " + STATUS + "INTEGER)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context){

        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // saan code mo sa insert pala? may button ba na iciclick?
            AlertDialog.Builder ab = new AlertDialog.Builder(this.Ctx);
            try
            {
                db.execSQL(CREATE_TODO_TABLE);
                ab.setMessage("Success");
                ab.show();
            }
            catch (SQLiteConstraintException sqx)
            {
                ab.setMessage("Database error: " + sqx.getMessage());
                ab.show();
            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //drop the older table
        db.execSQL("DROP TABLE IF EXISTS TODO_TABLE ");
        //CREATE TABLE AGAIN
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){ // never used, means dimo pa natawag , ito mali ba?
        ContentValues cv = new ContentValues();
        cv.put("task", task.getTask());
        cv.put("status", 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel>getAllTasks(){
        List<ToDoModel> tasklist = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
            try{
                cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
                if(cursor !=null){
                    if(cursor.moveToFirst()){
                        do{
                            ToDoModel task = new ToDoModel();
                            task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                            task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                            task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                            tasklist.add(task);
                        }while(cursor.moveToNext());
                    }
                }
            }
            finally {
                db.endTransaction();
                cursor.close();
            }
            return tasklist;
    }

    public void updateStatus(int id, int status){
        ContentValues cv =  new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "+?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "+?", new String[] {String.valueOf(id)});
    }

}
