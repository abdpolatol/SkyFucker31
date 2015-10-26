package com.example.bahadir.myapplicationn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClassGrupChat {
    private static final String DATABASENAME= "GrupMesajlarData.db";
    private static final String TABLENAME = "MesajTablosu";
    private static final int DATABASEVERSION = 1;

    private static final String ROWID = "_id";
    private static final String KENDIMESAJ = "kendimesaj";
    private static final String KARSIMESAJ = "karsimesaj";
    private static final String KANALADI = "kanaladi";
    Context context;
    boolean okunabilir , yazilabilir;
    private DbHelper dbhelper;
    private static File kayityeri;
    private static SQLiteDatabase sqlitedatabaseobjesi;
    public DatabaseClassGrupChat(Context context) {
        this.context = context;
        File path = Environment.getExternalStorageDirectory();
        String whappy = "Whappy";
        File f = new File(path , whappy);
        if(!f.exists()){
            f.mkdirs();
        }
        File mesajlar =new File(f,"GrupMesajlar");
        if(!mesajlar.exists()){
            mesajlar.mkdirs();
        }
        kayityeri = mesajlar ;
    }

    public DatabaseClassGrupChat open(){
        dbhelper = new DbHelper(context);
        checkexternal();
        sqlitedatabaseobjesi = dbhelper.getWritableDatabase();
        Log.i("tago" ,"open database");
        return this;
    }
    public void checkexternal() {
        String durum = Environment.getExternalStorageState();
        if(durum.equals(Environment.MEDIA_MOUNTED)){
            okunabilir = true;
            yazilabilir=true;
            Log.i("tago", "okunabilir ve yazılabilir");
        }else if(durum.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            okunabilir = true;
            yazilabilir = false;
            Log.i("tago" , "okunabilir fakat yazılamaz");
        }else{
            okunabilir = false;
            yazilabilir = false;
            Log.i("tago" , "okunamaz ve yazılamaz");
        }
    }
    public long olustur(String mesaj , String kanaladi){
        ContentValues cV = new ContentValues();
        cV.put(KENDIMESAJ , mesaj);
        cV.put(KARSIMESAJ , "badbadbado");
        Log.i("tago" ,"Kanaladi = " +  kanaladi);
        cV.put(KANALADI, kanaladi);
        Log.i("tago", "olustur database");
        return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
    }
    public void close(){
        sqlitedatabaseobjesi.close();
        Log.i("tago", "close database");
    }
    public List<String> databasedencek (String kanaladi) {
        List<String> kayitlimesajlar = new ArrayList<>();
        try {
            String[] kolonlar = new String[]{ROWID, KENDIMESAJ, KARSIMESAJ, KANALADI};
            Cursor c = sqlitedatabaseobjesi.query(TABLENAME, kolonlar, KANALADI +"='"+kanaladi+"'", null, null, null, null);
                int kendimesajindexi = c.getColumnIndex(KENDIMESAJ);
                int karsimesajindexi = c.getColumnIndex(KARSIMESAJ);
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    kayitlimesajlar.add(c.getString(kendimesajindexi) + "rumbararumbarumbarumruru" + c.getString(karsimesajindexi));
                }
            return kayitlimesajlar;
        }catch(SQLiteException e){
            Log.i("tago" , "kayit yok");
            e.getMessage();
        }
        return kayitlimesajlar;
    }
    public long olusturx(String mesaj,String kanaladi) {
        ContentValues cV = new ContentValues();
        cV.put(KENDIMESAJ , "badbadbado");
        cV.put(KARSIMESAJ , mesaj);
        cV.put(KANALADI , kanaladi);
        return sqlitedatabaseobjesi.insert(TABLENAME , null , cV);
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, kayityeri + File.separator + DATABASENAME, null, DATABASEVERSION);
            Log.i("tago" , "dbhelper create");
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(" + ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KENDIMESAJ +" TEXT NOT NULL, "+ KARSIMESAJ + " TEXT NOT NULL, " + KANALADI + " TEXT NOT NULL);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            onCreate(db);
        }
    }
}
