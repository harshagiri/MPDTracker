package edu.umo.mpdtracker.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBDelete {
    private SQLiteDbHelper dbHelper;
    private SQLiteDatabase db;

    public DBDelete(Context context) {
        dbHelper = new SQLiteDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void deleteEntry(String selectedDate, String medicineName){
        db.delete(DBReaderContract.DBEntry.TABLE_NAME, DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE + " <=? AND "
                + DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE + " >= ? AND "
                + DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME +" =? ", new String[] {selectedDate, selectedDate, medicineName});
        db.close();
    }
}
