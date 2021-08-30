package edu.umo.mpdtracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.chip.Chip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.umo.mpdtracker.Model.Medicine;
import edu.umo.mpdtracker.persist.DBWriter;

public class AddMedicineActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int RequestPermissionCode = 1;

    TextView medNameTV;
    Spinner medTypeSpinner;
    TextView datePicker;
    TextView medStartDateTV;
    TextView medDaysTV;
    Chip morningChip;
    Chip afternoonChip;
    Chip nighChip;
    ImageView medicinePhoto1;
    Button takePhotoButton;
    Button datePickerButton;
    Button submitButton;
    Bitmap tempBitMap;

    boolean morningIsSelected = false;
    boolean afternoonIsSelected = false;
    boolean nightIsSelected = false;

    Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine);

        medNameTV = findViewById(R.id.medicineNameTV);

        medTypeSpinner = findViewById(R.id.medicineTypeSpinner);
        setDataToMedTypeSpinner(medTypeSpinner);

        datePicker = findViewById(R.id.startDateTV);

        datePickerButton = findViewById(R.id.buttonPickDate);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(getSupportFragmentManager(),
                        "DATE PICK");
            }
        });

        medStartDateTV = findViewById(R.id.startDateTV);
        medDaysTV = findViewById(R.id.numberOfDaysTV);
        morningChip = findViewById(R.id.chipMorning);
        morningChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                morningIsSelected = isChecked;
            }
        });
        afternoonChip = findViewById(R.id.chipAfternoon);
        afternoonChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afternoonIsSelected = isChecked;
            }
        });
        nighChip = findViewById(R.id.chipNight);
        nighChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                nightIsSelected = isChecked;
            }
        });

        medicinePhoto1 = findViewById(R.id.medincinePhoto1);
        takePhotoButton = (Button) findViewById(R.id.cameraButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableRuntimePermission();
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });


        submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    if (collectFormData()){
                        DBWriter writer = new DBWriter(AddMedicineActivity.this);
                        writer.writeDataToDB(medicine);
                        AddMedicineActivity.this.finish();
                    } else {
                        Toast.makeText(AddMedicineActivity.this, "Incomplete form", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private ArrayList<String> buildSpinnerData() {
        ArrayList<String> medTypeStrings = new ArrayList<>();
        medTypeStrings.add("Tablet");
        medTypeStrings.add("Syrup");
        medTypeStrings.add("Powder");
        medTypeStrings.add("IV");
        return medTypeStrings;
    }

    private void setDataToMedTypeSpinner(Spinner spinner) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                buildSpinnerData());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        datePicker.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean collectFormData() throws ParseException {
        String medNameString = null;
        if(medNameTV.getText() != null && !medNameTV.getText().toString().isEmpty()){
            medNameString = medNameTV.getText().toString();
        } else {
            return false;
        }

        String medTypeString = null;
        if(medTypeSpinner.getSelectedItem() != null && !medTypeSpinner.getSelectedItem().toString().isEmpty()){
            medTypeString = medTypeSpinner.getSelectedItem().toString();
        } else {
            return false;
        }

        String medStartDateString = null;
        if(datePicker.getText() != null && !datePicker.getText().toString().isEmpty()){
            medStartDateString = datePicker.getText().toString();
        } else {
            return false;
        }

        String medDaysString = null;
        if(medDaysTV.getText() != null && !medDaysTV.getText().toString().isEmpty()){
            medDaysString = medDaysTV.getText().toString();
        } else {
            return false;
        }

        String medEndDateString = null;
        if(datePicker.getText() != null && !datePicker.getText().toString().isEmpty()){
            medEndDateString = computeEndDate(datePicker.getText().toString(), medDaysString);
        } else {
            return false;
        }

        String morningSwitchString = morningIsSelected ? "1" : "0";
        String afternoonSwitchString = afternoonIsSelected ? "1" : "0";
        String nightSwitchString = nightIsSelected ? "1" : "0";

        Bitmap medicinePhoto = null;
        if (tempBitMap != null) {
            Bitmap emptyMap = Bitmap.createBitmap(tempBitMap.getWidth(), tempBitMap.getHeight(), tempBitMap.getConfig());
            if(emptyMap.sameAs(tempBitMap)){
                return false;
            } else {
                medicinePhoto = tempBitMap;
            }
        } else {
            return false;
        }

        medicine = new Medicine(
                medNameString,
                medTypeString,
                medStartDateString,
                medEndDateString,
                medDaysString,
                morningSwitchString,
                afternoonSwitchString,
                nightSwitchString,
                medicinePhoto);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String computeEndDate(String startDateString, String noOfDays) throws ParseException {
        //End Date = Start Date + days
        Date inputDate = null;
        if (startDateString != null && !startDateString.isEmpty()) {
            try {
                inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(
                new Date(inputDate.getTime() + TimeUnit.DAYS.toMillis(Integer.parseInt(noOfDays) - 1)).getTime()
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            medicinePhoto1.setImageBitmap(bitmap);
            tempBitMap = bitmap;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddMedicineActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddMedicineActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddMedicineActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(AddMedicineActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddMedicineActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
}
