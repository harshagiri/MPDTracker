package edu.umo.mpdtracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.umo.mpdtracker.R;
import edu.umo.mpdtracker.Model.Medicine;

public class MedicineLVAdapter extends ArrayAdapter<Medicine> {

    public MedicineLVAdapter(@NonNull Context context, ArrayList<Medicine> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listView = convertView;
        if (listView == null)
            listView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);

        /*
         after inflating an item of listview item
         we are getting data from array list inside
         our modal class.
        */
        Log.d("POSITION", position+"");
        Medicine medicineDataModel = getItem(position);

        // initializing our UI components of list view item.
        ImageView imageView = listView.findViewById(R.id.idProductImage);
        TextView nameTV = listView.findViewById(R.id.idMedicineName);
        TextView dosage = listView.findViewById(R.id.idDosage);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        imageView.setImageBitmap(medicineDataModel.getMedicinePhoto());
        nameTV.setText(medicineDataModel.getName());
        dosage.setText("(" + medicineDataModel.getMorningSwitch() + "-" + medicineDataModel.getAfternoonSwitch() + "-" + medicineDataModel.getNightSwitch() + ")");
        // below line is use to add item click listener
        // for our item of list view.
        listView.setClickable(true);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Toast.makeText(getContext(), "Item clicked is : " + medicineDataModel.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return listView;
    }

}