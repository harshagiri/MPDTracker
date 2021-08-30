package edu.umo.mpdtracker.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBReaderContract.DBEntry.TABLE_NAME + " (" +
                    DBReaderContract.DBEntry._ID + " INTEGER PRIMARY KEY," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_DAYS + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_MORNING + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_NIGHT + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_TYPE + " TEXT," +
                    DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_PHOTO + " BLOB" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBReaderContract.DBEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MedTracker.db";

    public SQLiteDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

}
