package com.codiansoft.foodapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReserveTableActivity extends AppCompatActivity implements  com.prolificinteractive.materialcalendarview.OnDateSelectedListener  {

    Date startDate;
    Date endDate;
    MaterialCalendarView calendar;
    TextView startTime;
    boolean isTimeSet = false;
    String strTime = "";
    Spinner no_of_persons_spinner;
    String strPersons = "";
    String strDate="";
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_table);

        Log.e("reserve_table_activity", "start");
        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        startTime = findViewById(R.id.time);
        no_of_persons_spinner = findViewById(R.id.spinner_no_of_persons);
        submit=findViewById(R.id.submit);

        List<String> persons = new ArrayList<>();

        for (int i = 1; i < 100; i++) {
            persons.add(String.valueOf(i));
        }

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, persons);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        no_of_persons_spinner.setAdapter(adapter1);
        no_of_persons_spinner.setSelected(true);
        no_of_persons_spinner.setSelection(0);
        no_of_persons_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strPersons = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        calendar.setOnDateChangedListener(this);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String startDateString = (c.get(Calendar.MONTH) + 1) + "/" + c.getActualMinimum(Calendar.DATE) + "/" + c.get(Calendar.YEAR);

        Date date = new Date();
        c.setTime(date);
        c.add(Calendar.YEAR, 1);
        date = c.getTime();

        String endDateString = (c.get(Calendar.MONTH) + 1) + "/" + c.getActualMaximum(Calendar.DATE) + "/" + c.get(Calendar.YEAR);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        try {
            startDate = df.parse(startDateString);
            endDate = df.parse(endDateString);
            //   String newDateString = df.format(startDate);
            //  System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("date start", startDate.toString());
        Log.e("date end", endDate.toString());

        calendar.state().edit()
                .setMinimumDate(startDate)
                .setMaximumDate(endDate)
                .commit();

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTimeSet = true;
//                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                //  int minute = mcurrentTime.get(Calendar.MINUTE);
                int minute = 0;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ReserveTableActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        isTimeSet = true;

                        if (String.valueOf(selectedMinute).length() == 1) {
                            strTime = selectedHour + ":0" + selectedMinute;
                        } else {
                            strTime = selectedHour + ":" + selectedMinute;
                        }
                        startTime.setText(strTime);
                        Log.e("strTime", strTime);

                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("submit","submit");
                reserveTable();
            }
        });




    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


        if(selected)
        {
            //Log.e("date selected",date.toString());
            Date currentDate=new Date();
            Calendar c1=Calendar.getInstance();
            c1.setTime(currentDate);

            Calendar c2=Calendar.getInstance();
            date.copyTo(c2);
            // CalendarDay calendarDay=c.getTi
            if(c2.get(Calendar.YEAR)==c1.get(Calendar.YEAR) && c2.get(Calendar.MONTH)==c1.get(Calendar.MONTH) && c2.get(Calendar.DATE)<c1.get(Calendar.DATE) )
            {
                Toast.makeText(this,"You cant select previous date",Toast.LENGTH_SHORT).show();
                widget.clearSelection();
            }
            else {

                String _year=String.valueOf(date.getYear());

                String _month=String.valueOf(date.getMonth()+1);

                if(_month.length()==1)
                {
                    _month="0"+_month;
                }

                String _day=String.valueOf(date.getDay());
                if(_day.length()==1)
                {
                    _day="0"+_day;
                }
                strDate=_year+"-"+_month+"-"+_day;

                //Toast.makeText(this, date.getDate().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    boolean validate()
    {
        if(strDate.equals(""))
        {
            Toast.makeText(ReserveTableActivity.this , "please select Date !",Toast.LENGTH_LONG).show();
            return false;
        }
        if(strPersons.equals(""))
        {
            Toast.makeText(ReserveTableActivity.this , "please select no of persons !",Toast.LENGTH_LONG).show();
            return false;
        }
        if(strTime.equals(""))
        {
            Toast.makeText(ReserveTableActivity.this , "please select Time !",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
    private void reserveTable() {
       if (validate())
       {
           RequestQueue queue = Volley.newRequestQueue(this);
           StringRequest postRequest = new StringRequest(Request.Method.POST, GlobalClass.TABLE_RESERVATION,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           // response
                           try {
                               JSONObject Jobject = new JSONObject(response);
                               JSONObject result = Jobject.getJSONObject("result");
                               if (result.getString("status").equals("success")) {

                                   new MaterialDialog.Builder(ReserveTableActivity.this)
                                           .title("Table Reservation")
                                           .content(result.getString("response"))
                                           .positiveText("OK")
                                           .cancelable(false)
                                           .canceledOnTouchOutside(false)
                                           .onPositive(new MaterialDialog.SingleButtonCallback() {
                                               @Override
                                               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                   Intent i = new Intent(ReserveTableActivity.this, MainActivity.class);
                                                   ActivityCompat.finishAffinity(ReserveTableActivity.this);
                                                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                   ReserveTableActivity.this.startActivity(i);
                                               }
                                           })
                                           .show();
//                                Toast.makeText(ReserveTableActivity.this , result.getString("response") , Toast.LENGTH_LONG).show();
                               }
                               else
                               {

                               }
                           } catch (Exception ee) {
                               Toast.makeText(ReserveTableActivity.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   },
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                               Toast.makeText(ReserveTableActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                           } else if (error instanceof AuthFailureError) {

                               //TODO
                           } else if (error instanceof ServerError) {

                               //TODO
                           } else if (error instanceof NetworkError) {

                               //TODO
                           } else if (error instanceof ParseError) {

                               //TODO
                           }
                       }
                   }
           )
           {
               @Override
               protected Map<String, String> getParams() {
                   Map<String, String> params = new HashMap<String, String>();
                   SharedPreferences settings = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                   //  String userID = settings.getString("userID", "defaultValue");
                   String apiSecretKey = settings.getString("apiSecretKey", "defaultValue");

                   params.put("api_secret", apiSecretKey);
                   params.put("restaurant_id", GlobalClass.selectedRestaurantID);
                   params.put("branch_id", GlobalClass.selectedRestaurantBranchID);
                   params.put("date",strDate);
                   params.put("time", strTime);
                   params.put("no_persons",strPersons);
                   params.put("table_id",GlobalClass.table_id);
                   params.put("table_no",GlobalClass.table_number);

                   Log.e("params",params.toString());

                   return params;
               }
           };
           queue.add(postRequest);

       }
           }


}