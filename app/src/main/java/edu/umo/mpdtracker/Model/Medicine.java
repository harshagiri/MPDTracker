package edu.umo.mpdtracker.Model;

import android.graphics.Bitmap;

public class Medicine {
    private String morningSwitch;
    private String afternoonSwitch;
    private String nightSwitch;
    private String name;
    private String type;
    private String startDate;
    private String endDate;
    private String days;
    private Bitmap medicinePhoto;

    public Medicine() {

    }

    public Medicine(String name, String type, String startDate, String days) {
        this.name = name;
        this.type = type;
        this.startDate = startDate;
        this.days = days;
    }

    public Medicine(String medNameString,
                    String medTypeString,
                    String medStartDateString,
                    String medEndDateString,
                    String medDaysString,
                    String morningSwitchString,
                    String afternoonSwitchString,
                    String nightSwitchString,
                    Bitmap medicinePhoto) {
        this.name = medNameString;
        this.type = medTypeString;
        this.startDate = medStartDateString;
        this.endDate = medEndDateString;
        this.days = medDaysString;
        this.morningSwitch = morningSwitchString;
        this.afternoonSwitch = afternoonSwitchString;
        this.nightSwitch = nightSwitchString;
        this.medicinePhoto = medicinePhoto;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDays() {
        return days;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMorningSwitch() {
        return morningSwitch;
    }

    public String getAfternoonSwitch() {
        return afternoonSwitch;
    }

    public String getNightSwitch() {
        return nightSwitch;
    }

    public Bitmap getMedicinePhoto(){
        return medicinePhoto;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "morningSwitch='" + morningSwitch + '\'' +
                ", afternoonSwitch='" + afternoonSwitch + '\'' +
                ", nightSwitch='" + nightSwitch + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", days='" + days + '\'' +
                '}';
    }
}
