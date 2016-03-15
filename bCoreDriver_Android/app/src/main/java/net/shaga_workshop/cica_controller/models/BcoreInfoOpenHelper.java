package net.shaga_workshop.cica_controller.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shaga on 2015/11/21.
 */
public class BcoreInfoOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bCoreInfo.db";
    private static final int DB_VERSION = 1;


    public BcoreInfoOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BcoreInfoCatHands.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
