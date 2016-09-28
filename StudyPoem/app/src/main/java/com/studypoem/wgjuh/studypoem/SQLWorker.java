package com.studypoem.wgjuh.studypoem;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
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
import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class SQLWorker extends SQLiteOpenHelper {
    public static final String DB_NAME = "PoemsTable.sqlite";
    public static final String DB_LOCATION = "/data/data/"+BuildConfig.APPLICATION_ID+"/databases/";
    Context context;
    SQLiteDatabase database;
    public SQLWorker(Context context) {
        super(context,DB_NAME,null,1);
        this.context=context;
        System.out.println(MainActivity.TAG + "Context: " + context);
        boolean dbexist = checkdatabase();
        if (dbexist) {
            System.out.println(MainActivity.TAG + "Database exists");
            opendatabase();
        } else {
            System.out.println(MainActivity.TAG + "Database doesn't exist");
            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(dbexist) {
            System.out.println(MainActivity.TAG + " Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_LOCATION + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println(MainActivity.TAG + "Database doesn't exist");
        }
        return checkdb;
    }

    /**
     * TODO error with assets file
     * @throws IOException
     */
    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = context.getAssets().open(DB_NAME);
        System.out.println(MainActivity.TAG + " assets: " + myinput.available());
        // Path to the just created empty db
        String outfilename = DB_LOCATION + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[myinput.available()];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_LOCATION + DB_NAME;
        database = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(database != null) {
            database.close();
        }
        super.close();
    }

   /*     super(context, DB_NAME, null, 1);
        this.context = context;
        checkDatabse();
    }

    private void checkDatabse(){
        File databse = new File(DB_LOCATION+DB_NAME);
        if(!databse.exists()){
            if(!copyDatabse()){
                System.out.println(MainActivity.TAG + MainActivity.TAG + " FAILED COPY DB");
            }
        }
    }
    private boolean copyDatabse(){
        try{
            InputStream inputStream = context.getAssets().open(DB_NAME);
            String outFilename = DB_LOCATION + DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFilename);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer))>0) {
                outputStream.write(buffer,0,length);
            }            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void openDatabse(){
        String myPath =DB_LOCATION + DB_NAME; //DB_LOCATION + DB_NAME;
        if(database != null && database.isOpen()){
            return;
        }else
            database = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDatabase(){
        if(database != null)
            database.close();
    }*/
    public Values getStringsFromDB(String regex) {
        opendatabase();
        String column = "author_name";
        Cursor cursor;
        Integer currentLvl = ListPoems.lvl;
        if (currentLvl != 0 && regex != null) {
            switch (currentLvl) {
                case 0:
                    column = "author_name";
                    break;
                case 1:
                    column = "title";
                    break;
                case 2:
                    column = "poem";
                    break;
                default:
                    break;
            }
            cursor = database.query(null, new String[]{column}, "(author_name LIKE ? OR title LIKE ?)", new String[]{"%" + regex + "%", "%" + regex + "%"}, column, null, column);
        } else {
            cursor = database.query(null, new String[]{column,"author_portrait_id"}, null, null, column, null, column);
        }
        System.out.println(MainActivity.TAG + MainActivity.TAG + " " + cursor.getCount() + " names " + Arrays.toString(cursor.getColumnNames()));
        ArrayList<String> adapterStrings = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(0);
                System.out.println(MainActivity.TAG + "STRING: " + temp);
                adapterStrings.add(temp);
                if(currentLvl == 0)
                    ids.add(cursor.getInt(1));
            } while (cursor.moveToNext());
        } else System.out.println(MainActivity.TAG + MainActivity.TAG + " 0 elements");
        System.out.println(MainActivity.TAG + MainActivity.TAG + adapterStrings.get(0).toString());
        close();
        return new Values(adapterStrings,ids);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(MainActivity.TAG + MainActivity.TAG + " CREATE db");
/*        db.execSQL("create table "+ DB_NAME+" ("
                +   "id_author integer primary key,"
                +   "author_name text,"
                +   "author_portrait_id integer,"
                +   "title text,"
                +   "poem text"
                +   ");");
        String[] authors = context.getResources().getStringArray(R.array.poets);
        TypedArray authorsPortraitIds= context.getResources().obtainTypedArray(R.array.poets_portraits);
        String[] titles = context.getResources().getStringArray(R.array.titles);
        String[] poems = context.getResources().getStringArray(R.array.poems);
        System.out.println(MainActivity.TAG + MainActivity.TAG + " poems: " + Arrays.toString(poems)+ " names: " + Arrays.toString(authors) + " Titles: " + Arrays.toString(titles));
        ContentValues contentValues = new ContentValues();
        for(int i = 0 ; i < authors.length; i++){
            contentValues.clear();
            contentValues.put("author_name",authors[i]);
            contentValues.put("author_portrait_id", authorsPortraitIds.getResourceId(i,-1));
            contentValues.put("title",titles[i]);
            contentValues.put("poem",poems[i]);
            System.out.println(MainActivity.TAG + MainActivity.TAG+ " put into: " +  db.insert(DB_NAME,null,contentValues));
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}