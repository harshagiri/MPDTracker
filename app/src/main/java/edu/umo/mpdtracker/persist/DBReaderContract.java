package edu.umo.mpdtracker.persist;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

public class DBReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBReaderContract() {}

    /* Inner class that defines the table contents */
    public static class DBEntry implements BaseColumns {
        public static final String TABLE_NAME = "Medicine";
        public static final String COLUMN_NAME_MEDICINE_NAME = "Name";
        public static final String COLUMN_NAME_MEDICINE_START_DATE = "StartDate";
        public static final String COLUMN_NAME_MEDICINE_END_DATE = "EndDate";
        public static final String COLUMN_NAME_MEDICINE_DOSAGE_DAYS = "DosageDays";
        public static final String COLUMN_NAME_MEDICINE_DOSAGE_MORNING = "DosageMorning";
        public static final String COLUMN_NAME_MEDICINE_DOSAGE_AFTERNOON = "DosageAfternoon";
        public static final String COLUMN_NAME_MEDICINE_DOSAGE_NIGHT = "DosageNight";
        public static final String COLUMN_NAME_MEDICINE_TYPE = "Type";
        public static final String COLUMN_NAME_MEDICINE_PHOTO = "MedicinePhoto";
    }
    
}
