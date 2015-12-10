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

public class DatabaseClassKiminleKonustun {
    private static final String DATABASENAME= "KiminleKonustun.db";
    private static final String TABLENAME = "KonustuklarinTablosu";
    private static final int DATABASEVERSION = 1;

    private static final String ROWID = "_id";
    private static final String KARSIID = "karsiid";
    private static final String KARSIISIM = "karsiisim";
    private static final String KARSIRESIMURL = "karsiresimurl";

    Context context;
    private DbHelper dbhelper;
    private static File kayityeri;
    private static SQLiteDatabase sqlitedatabaseobjesi;

    public DatabaseClassKiminleKonustun(Context context) {
        this.context = context;
        File path = Environment.getExternalStorageDirectory();
        String whappy = "Whappy";
        File f = new File(path , whappy);
        if(!f.exists()){
            f.mkdirs();
        }
        File mesajlar =new File(f,"KiminleKonustun");
        if(!mesajlar.exists()){
            mesajlar.mkdirs();
        }
        kayityeri = mesajlar ;
    }

    public DatabaseClassKiminleKonustun open(){
        dbhelper = new DbHelper(context);
        sqlitedatabaseobjesi = dbhelper.getWritableDatabase();
        return this;
    }
    public void close(){
        sqlitedatabaseobjesi.close();
    }
    public long olustur(String karsiid , String karsiisim , String karsiresimurl){
        boolean var = false;
        List<String> a = databasedenidcek();
        for(int i = 0 ; i<a.size() ; i++){
            Log.i("tago" , "karsi id=" + karsiid);
            Log.i("tago" , "databaseid = " + a.get(i));
            if(karsiid.equals(a.get(i))){
                var = true;
                Log.i("tago" , String.valueOf(var));
            }
        }
        if(var==false){
            ContentValues cV = new ContentValues();
            cV.put(KARSIID , karsiid);
            cV.put(KARSIISIM , karsiisim);
            cV.put(KARSIRESIMURL, karsiresimurl);
            return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
        }if(var==true){

        }
        long b = 23;
        return b;
    }
    public List<String> databasedencek(String karsidakiid) {
        String[] kolonlar = new String[]{ROWID , KARSIID , KARSIISIM, KARSIRESIMURL};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, KARSIID + "=" + karsidakiid, null, null, null, null);
        List<String> kayitlikonustuklarin = new ArrayList<>();
        int karsiidindexi = c.getColumnIndex(KARSIID);
        int karsiisimindexi = c.getColumnIndex(KARSIISIM);
        int karsiresimurlindexi = c.getColumnIndex(KARSIRESIMURL);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitlikonustuklarin.add(c.getString(karsiidindexi) + c.getString(karsiisimindexi)
            + c.getString(karsiresimurlindexi));
        }
        return kayitlikonustuklarin ;
    }
    public List<String> databasedenidcek() {
        String[] kolonlar = new String[]{ROWID , KARSIID , KARSIISIM, KARSIRESIMURL};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, null, null, null, null, null);
        List<String> kayitliidler = new ArrayList<>();
        int karsiidindexi = c.getColumnIndex(KARSIID);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitliidler.add(c.getString(karsiidindexi));
        }
        return kayitliidler ;
    }
    public List<String> databasedenisimcek() {
        String[] kolonlar = new String[]{ROWID , KARSIID , KARSIISIM, KARSIRESIMURL};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, null, null, null, null, null);
        List<String> kayitliisimler = new ArrayList<>();
        int karsiisimindexi = c.getColumnIndex(KARSIISIM);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitliisimler.add(c.getString(karsiisimindexi));
        }
        return kayitliisimler ;
    }
    public List<String> databasedenresimurlcek() {
        String[] kolonlar = new String[]{ROWID , KARSIID , KARSIISIM, KARSIRESIMURL};
        Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, null, null, null, null, null);
        List<String> kayitliresimurller = new ArrayList<>();
        int karsiresimurlindexi = c.getColumnIndex(KARSIRESIMURL);
        for(c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()){
            kayitliresimurller.add(c.getString(karsiresimurlindexi));
        }
        return kayitliresimurller ;
    }

    public void delete(String table_name, String o, String o1) {
        sqlitedatabaseobjesi.delete(table_name , null, null);
    }


    private static class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, kayityeri + File.separator + DATABASENAME, null, DATABASEVERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "(" + ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KARSIID +" TEXT NOT NULL, "+ KARSIISIM + " TEXT NOT NULL, " + KARSIRESIMURL + " TEXT NOT NULL);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }
}
}