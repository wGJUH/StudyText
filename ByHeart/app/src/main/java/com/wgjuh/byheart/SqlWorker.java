package com.wgjuh.byheart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

import com.wgjuh.byheart.myapplication.R;

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

    public SqlWorker(Context context) {
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
        System.out.println(" createDB method finished: " + (System.currentTimeMillis() - start));
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

    public Values getPoemsAuthorsFromDB() {
        opendatabase();
        Cursor cursor;
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<Integer> id = new ArrayList<Integer>();
        ArrayList<String> portrait_ids = new ArrayList<String>();
        cursor = database.query(DB_NAME, new String[]{COLUMN_AUTHOR_NAME, COLUMN_ID, COLUMN_PORTRAIT_ID}, COLUMN_AUTHOR_NAME + " IS NOT NULL", null, COLUMN_AUTHOR_NAME, null, COLUMN_AUTHOR_NAME);
        if (cursor.moveToFirst()) {
            do {
                strings.add(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)));
                id.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                portrait_ids.add(cursor.getString(cursor.getColumnIndex(COLUMN_PORTRAIT_ID)));

            } while (cursor.moveToNext());
        }
        close();
        return new Values(strings, portrait_ids);
    }

    public Values getPoemsTitlesFromDB(String author) {
        opendatabase();
        Cursor cursor;
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<Boolean> starrs = new ArrayList<Boolean>();
        cursor = database.query(DB_NAME, new String[]{COLUMN_ID, COLUMN_POEM_TITLE, COLUMN_FAVORITE}, COLUMN_AUTHOR_NAME + " = ? AND " + COLUMN_POEM_TITLE + " IS NOT NULL", new String[]{author}, null, null, COLUMN_POEM_TITLE);
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                titles.add(cursor.getString(cursor.getColumnIndex(COLUMN_POEM_TITLE)));
                starrs.add(cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE)) != 0);
                ids.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return new Values(titles, starrs, ids);
    }

    public Values getStarred() {
        opendatabase();
        Cursor cursor = database.query(DB_NAME, new String[]{COLUMN_ID, COLUMN_POEM_TITLE}, COLUMN_FAVORITE + " = ?", new String[]{"" + 1}, null, null, COLUMN_POEM_TITLE);
        ArrayList<String> adapterStrings = new ArrayList<>();
        ArrayList<Boolean> starrs = new ArrayList<Boolean>();
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(cursor.getColumnIndex(COLUMN_POEM_TITLE));
                if (temp != null) {
                    //System.out.println(MainActivity.TAG + "STRING: " + temp);
                    adapterStrings.add(temp);
                    starrs.add(true);
                    ids.add(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else System.out.println(" 0 elements");
        close();
        System.out.println("Add " + adapterStrings.size() + " rows");
        return new Values(adapterStrings, starrs, ids);
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_LOCATION + DB_NAME;
        database = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public int getStarredSize() {
        return getStarred().getStrings().size();
    }

    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    private boolean isDatabaseExists() {
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

    public boolean setStar(int id) {
        int updated;
        opendatabase();
        Cursor cursor = database.query(DB_NAME, new String[]{COLUMN_ID, COLUMN_AUTHOR_NAME, COLUMN_FAVORITE}, COLUMN_ID + " =?", new String[]{"" + id}, null, null, null);
        if (cursor.moveToFirst()) {
            if(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)) == null){
                System.out.println("DELETED");
                updated = database.delete(DB_NAME,COLUMN_ID + " =? ", new String[]{""+id});
            }else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_FAVORITE, (cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE)) ^ 1));
                updated = database.update(DB_NAME, contentValues, COLUMN_ID + " =? ", new String[]{"" + id});
            }
            System.out.println("Udpated: " + updated);
            cursor.close();
        } else {
            cursor.close();
            close();
            return false;
        }
        close();
        return true;
    }

    public void addNewAuthor(String authorName, String authorPhoto) {
        opendatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AUTHOR_NAME, authorName);
        contentValues.put(COLUMN_PORTRAIT_ID, authorPhoto);
        database.insert(DB_NAME, null, contentValues);
        close();
    }
    public void addNewText(String authorName, String title, String poem){
        opendatabase();
        ContentValues contentValues = new ContentValues();


        if(!authorName.equals(context.getString(R.string.app_name))){
            System.out.println("authorName: " + authorName + " poem Title: " + context.getString(R.string.app_name));
            contentValues.put(COLUMN_AUTHOR_NAME, authorName);
        }
        else{
            contentValues.put(COLUMN_FAVORITE,1);
        }
        contentValues.put(COLUMN_POEM_TITLE,title);
        contentValues.put(COLUMN_POEM,poem);
        database.insert(DB_NAME,null,contentValues);
        close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int getRowNumber(String s) {
        System.out.println(" take rowNumber");
        opendatabase();
        Cursor cursor = database.query(DB_NAME, null, null, null, COLUMN_AUTHOR_NAME, null, COLUMN_AUTHOR_NAME);
        int i = -1;
        String author, title;
        if (cursor.moveToFirst())
            do {
                i++;
                author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME));
                title = cursor.getString(cursor.getColumnIndex(COLUMN_POEM_TITLE));
                if (author.equals(s) || title.equals(s)) {
                    System.out.println(" break");
                    break;
                }
            } while (cursor.moveToNext());
        cursor.close();
        close();
        System.out.println(" i : " + i);
        return i;
    }

    public int getRowPoemNumber(String author, String title) {
        System.out.println(" take rowNumber");
        opendatabase();
        Cursor cursor;
        String tempTitle;
        if(author != null)
        cursor= database.query(DB_NAME, null, COLUMN_AUTHOR_NAME + " =?", new String[]{author}, null, null, COLUMN_POEM_TITLE);
        else cursor= database.query(DB_NAME, null, COLUMN_FAVORITE + " =? ", new String[]{""+1}, null, null, COLUMN_POEM_TITLE);
        int i = -1;
        if (cursor.moveToFirst())
            do {
                i++;
                tempTitle = cursor.getString(cursor.getColumnIndex(COLUMN_POEM_TITLE));
                if (tempTitle.equals(title)) {
                    System.out.println(" break");
                    break;
                }
            } while (cursor.moveToNext());
        cursor.close();
        close();
        System.out.println(" i : " + i);
        /**
         * todo не уверен в правильности этого i-1
         */
        return i-1;
    }
}