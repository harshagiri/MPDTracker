package edu.umo.mpdtracker.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import edu.umo.mpdtracker.Model.Medicine;

public class DBWriter {

    private SQLiteDbHelper dbHelper;
    private SQLiteDatabase db;

    public DBWriter(Context context) {
        dbHelper = new SQLiteDbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public void writeDataToDB(String medNameString, String medStartDateString, String medEndDateString, String medDaysString, String morningSwitchString, String afternoonSwitchString, String nightSwitchString, String medTypeString, Bitmap medicineImage) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME, medNameString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_TYPE, medTypeString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE, medStartDateString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE, medEndDateString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_DAYS, medDaysString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_MORNING, morningSwitchString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON, afternoonSwitchString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_NIGHT, nightSwitchString);
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_PHOTO, getBitmapAsByteArray(medicineImage));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBReaderContract.DBEntry.TABLE_NAME, null, values);
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void writeDataToDB(Medicine medicine) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_NAME, medicine.getName());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_TYPE, medicine.getType());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_START_DATE, medicine.getStartDate());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_END_DATE, medicine.getEndDate());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_DAYS, medicine.getDays());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_MORNING, medicine.getMorningSwitch());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON, medicine.getAfternoonSwitch());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_DOSAGE_NIGHT, medicine.getNightSwitch());
        values.put(DBReaderContract.DBEntry.COLUMN_NAME_MEDICINE_PHOTO, getBitmapAsByteArray(medicine.getMedicinePhoto()));

        // Insert the new row, returning the primary key value of the new row
        try {
            long newRowId = db.insert(DBReaderContract.DBEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("INSERTED", medicine.toString());
    }
}
