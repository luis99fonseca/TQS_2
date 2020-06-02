package com.example.home_searcher.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.home_searcher.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;
import java.text.SimpleDateFormat;

import androidx.fragment.app.FragmentActivity;


public class Pop extends FragmentActivity {

    CalendarPickerView datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarpopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*0.9), (int)(height*0.9));

        initializeCalendar();

    }

    public void initializeCalendar(){
        Date today = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        this.datePicker = findViewById(R.id.calendarpicker);
        datePicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {


            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar calSelected=Calendar.getInstance();
                calSelected.setTime(date);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        List<Date> rentdates= this.datePicker.getSelectedDates();
        Intent resultItent = new Intent();
        Calendar calSelected = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

        if (rentdates.size() > 1) {
            calSelected.setTime(rentdates.get(0));
            String firstDate = format1.format(calSelected.getTime());
            resultItent.putExtra("firstDay", firstDate);

            calSelected = Calendar.getInstance();
            calSelected.setTime(rentdates.get(rentdates.size() - 1));
            String lastDate = format1.format(calSelected.getTime());
            resultItent.putExtra("lastDay", lastDate);

            setResult(RESULT_OK, resultItent);
        }
        else if (rentdates.size() == 1){
            calSelected.setTime(rentdates.get(0));
            String date = format1.format(calSelected.getTime());
            resultItent.putExtra("firstDay", date);
            resultItent.putExtra("lastDay", date);
            setResult(RESULT_OK, resultItent);
        }
        super.onBackPressed();
    }

}
