package ca.nait.cstanhope2.notelingual;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBManager extends SQLiteOpenHelper{

    private static final int DB_Version = 2;
    static final String DB_NAME = "NoteLingual.db";

    static final String TABLE_NAME = "note";
    static final String COL_ID = BaseColumns._ID;
    static final String COL_TITLE = "title";
    static final String COL_DATE_CREATED = "created";
    static final String COL_DATE_LAST_EDIT = "lastEdit";
    static final String COL_CONTENT = "content";

    public DBManager(Context context){
        super(context, DB_NAME, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = String.format(
                "create table %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s text)",
                TABLE_NAME,
                COL_ID, COL_TITLE, COL_DATE_CREATED, COL_DATE_LAST_EDIT, COL_CONTENT
        );

        db.execSQL(create);
    }

    @Override
    // triggered when DB_VERSION changes
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(db);
    }
}