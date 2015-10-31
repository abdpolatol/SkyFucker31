package com.example.bahadir.myapplicationn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClassKonusulanKanallar {
    private static final String DATABASENAME= "KonusulanKanallar.db";
    private static final String TABLENAME = "KonusulanKanalTablosu";
    private static final int DATABASEVERSION = 1;

    private static final String ROWID = "_id";
    private static final String KANALADI = "kanaladi";

    Context context;
    private DbHelper dbhelper;
    private static File kayityeri;
    private static SQLiteDatabase sqlitedatabaseobjesi;

    public DatabaseClassKonusulanKanallar(Context context) {
        this.context = context;
        File path = Environment.getExternalStorageDirectory();
        String whappy = "Whappy";
        File f = new File(path , whappy);
        if(!f.exists()){
            f.mkdirs();
        }
        File mesajlar =new File(f,"KonusulanKanallar");
        if(!mesajlar.exists()){
            mesajlar.mkdirs();
        }
        kayityeri = mesajlar ;
    }
    public DatabaseClassKonusulanKanallar open(){
        dbhelper = new DbHelper(context);
        sqlitedatabaseobjesi = dbhelper.getWritableDatabase();
        return this;
    }
    public void close(){
        sqlitedatabaseobjesi.close();
    }
    public long olustur(String kanaladi){
        boolean var = false;
        List<String> a = databasedenkanalcek();
        for(int i = 0 ; i<a.size() ; i++){
            Log.i("tago", "kanaladi =" + kanaladi);
            Log.i("tago" , "databasekanal = " + a.get(i));
            if(kanaladi.equals(a.get(i))){
                var = true;
                Log.i("tago" , String.valueOf(var));
            }
        }
        if(var==false){
            ContentValues cV = new ContentValues();
            cV.put(KANALADI , kanaladi);
            return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
        }
        long b = 23;
        return b;
    }
    public List<String> databasedenkanalcek() {
        String[] kolonlar = new String[]{ROWID , KANALADI};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, null, null, null, null, null);
        List<String> kayitlikanallar = new ArrayList<>();
        int kanaladiindexi = c.getColumnIndex(KANALADI);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitlikanallar.add(c.getString(kanaladiindexi));
        }
        return kayitlikanallar ;
    }


    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, kayityeri + File.separator + DATABASENAME, null, DATABASEVERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(" + ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     KANALADI + " TEXT NOT NULL);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            onCreate(db);
        }
    }
}
