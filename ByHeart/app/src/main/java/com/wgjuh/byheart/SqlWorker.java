package com.wgjuh.byheart;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by wGJUH on 19.10.2016.
 */

public class SqlWorker extends SQLiteOpenHelper implements Data {
    private Context context;
    SQLiteDatabase database;

    public  SqlWorker(Context context){
        super(context, DB_NAME, null, 1);
        this.context = context;
        if (isDatabaseExists()) {
            System.out.println("Database exists");
            //opendatabase();
        } else {
            System.out.println("Database doesn't exist");
            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createdatabase() throws IOException {
        long start = System.currentTimeMillis();
        if (isDatabaseExists()) {
            System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
        System.out.println(" createDB method finished: " + (System.currentTimeMillis()-start));
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        System.out.println(" START COPYING");
        InputStream myinput = null;
        try {
            myinput = context.getAssets().open(DB_NAME);
        } catch (IOException e) {
            System.out.println(" get from assets failed ");
            e.printStackTrace();
        }
        System.out.println(" assets: " + myinput.available());
        // Path to the just created empty db
        String outfilename = DB_LOCATION + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[myinput.available()];
        System.out.println(" BUFFER: " + buffer.length);
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }
    public Values getPoemsAuthorsFromDB(){
        opendatabase();
        Cursor cursor;
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<Integer> id = new ArrayList<Integer>();
        ArrayList<String> portrait_ids = new ArrayList<String>();
        cursor = database.query(DB_NAME, new String[]{COLUMN_AUTHOR_NAME,COLUMN_ID,COLUMN_PORTRAIT_ID}, COLUMN_AUTHOR_NAME + " IS NOT NULL", null, COLUMN_AUTHOR_NAME, null, COLUMN_AUTHOR_NAME);
        if(cursor.moveToFirst()){
            do{
                strings.add(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)));
                id.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                portrait_ids.add(cursor.getString(cursor.getColumnIndex(COLUMN_PORTRAIT_ID)));

            }while (cursor.moveToNext());
        }
        close();
        return new Values(strings,id,portrait_ids);
    }
    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_LOCATION + DB_NAME;
        database = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    private boolean isDatabaseExists(){
        boolean checkdb = false;
        try {
            String myPath = DB_LOCATION + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
