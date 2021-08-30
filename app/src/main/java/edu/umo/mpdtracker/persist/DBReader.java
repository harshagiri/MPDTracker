package edu.umo.mpdtracker.persist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import edu.umo.mpdtracker.Model.Medicine;

public class DBReader {

    private SQLiteDbHelper dbHelper;
    private SQLiteDatabase db;

    // Define a projection that specifies which columns from the database
// you will actually use after this query.
    private String[] projection = {
            BaseColumns._ID,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_DAYS,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_MORNING,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_NIGHT,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_TYPE,
            DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_PHOTO
    };
    private Cursor cursor;


    public DBReader(Context context) {
        dbHelper = new SQLiteDbHelper(context);
        db = dbHelper.getReadableDatabase();

    }

    public List<Medicine> readFormDB(String selectedDate) {
        String rawQuery = "SELECT * FROM Medicine WHERE "
                + DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE + " <=? AND "
                + DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE+" >= ?";

        cursor = db.rawQuery(rawQuery, new String [] {selectedDate,selectedDate});

        /*cursor = db.query(
                DBReaderContract.DBEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE + "='" + selectedDate + "'",              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );*/

        List meds = readResultSet();
        if (!meds.isEmpty() && meds.size() > 0) {
            return meds;
        }

        return null;
    }

    public List<Medicine> readFormDB() {

        cursor = db.query(
                DBReaderContract.DBEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List meds = readResultSet();
        if (!meds.isEmpty() && meds.size() > 0) {
            return meds;
        }

        return null;
    }

    private List readResultSet() {
        List meds = new ArrayList<Medicine>();
        while (cursor.moveToNext()) {
            String medName = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME));
            String medType = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_TYPE));
            String medStartDate = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE));
            String medEndDate = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE));
            String medDosageDays = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_DAYS));
            String medDosageMorning = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_MORNING));
            String medDosageAfternoon = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON));
            String medDosageNight = cursor.getString(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_NIGHT));
            Bitmap medicineImage = getImage(cursor.getBlob(cursor.getColumnIndexOrThrow(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_PHOTO)));

            Medicine medicine = new Medicine(medName,
                    medType,
                    medStartDate,
                    medEndDate,
                    medDosageDays,
                    medDosageMorning,
                    medDosageAfternoon,
                    medDosageNight,
                    medicineImage);
            meds.add(medicine);
        }
        cursor.close();
        return meds;
    }

    public Bitmap getImage(byte[] medicineImage) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(medicineImage, 0, medicineImage.length);
        return bitmap;
    }
}
