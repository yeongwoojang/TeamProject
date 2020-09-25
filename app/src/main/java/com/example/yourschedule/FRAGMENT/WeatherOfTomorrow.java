package com.example.yourschedule.FRAGMENT;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.Formatter.KoreanFormat;
import com.example.yourschedule.Formatter.TempValueFormatter;
import com.example.yourschedule.Formatter.TimeValueFormatter;
import com.example.yourschedule.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherOfTomorrow extends Fragment {

    public WeatherOfTomorrow newInstance() {
        return new WeatherOfTomorrow();
    }

    private LineChart lineChart, lineChart2;
    private TextView weatherText, maxTemp, minTemp, humidityText;
    private ImageButton[] imageButtons;
    private ImageView runningImage;

    private FrameLayout completePage;

    private JsonObject jsonObject;
    double longitude;
    double latitude;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_of_tomorrow, container, false);
        weatherText = rootView.findViewById(R.id.weatherText);
        completePage = rootView.findViewById(R.id.completePage);
        maxTemp = rootView.findViewById(R.id.maxTemperature);
        minTemp = rootView.findViewById(R.id.minTemperature);
        humidityText = rootView.findViewById(R.id.humidity);
        runningImage = rootView.findViewById(R.id.running_image);
        lineChart = rootView.findViewById(R.id.linechart);
        lineChart2 = rootView.findViewById(R.id.linechart2);

        if (getArguments() != null) {
            longitude = getArguments().getDouble("longitude");
            latitude = getArguments().getDouble("latitude");

                jsonObject = new JsonParser().parse(getArguments().getString("jsonObject")).getAsJsonObject();
                Log.d("fsdgsfdg",jsonObject+"");
        }
        String pkg = getActivity().getPackageName();
        imageButtons = new ImageButton[8];
        for (int i = 1; i < imageButtons.length + 1; i++) {
            int id = getResources().getIdentifier("weatherIcon" + i, "id", pkg);
            imageButtons[i - 1] = rootView.findViewById(id);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        float mornTemp;
        float dayTemp;
        float eveTemp;
        float nightTemp;
        float maxTemperature;
        float minTemperature;
        int humidity;
        int weatherId;

        List<String> humidityList = new ArrayList<>();
        List<Integer> hourList = new ArrayList<>();
        List<String> hourTemperatureList = new ArrayList<>();
        int index =0;


        JsonArray jaDaily = jsonObject.getAsJsonArray("daily");
        JsonArray jaHourly = jsonObject.getAsJsonArray("hourly");
        Log.d("dsgsfdgdsfgs", jaHourly + "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar todayCal = Calendar.getInstance();
        Calendar afterTmrCal = Calendar.getInstance();
        afterTmrCal.add(Calendar.DATE,2);
        for (int i = 0; i < jaHourly.size(); i++) {
            JsonElement jsonElementHourly = jaHourly.get(i);
            JsonObject elementObject = jsonElementHourly.getAsJsonObject();
            JsonArray jaWeather = jsonElementHourly.getAsJsonObject().get("weather").getAsJsonArray();
            Date timeInDate = new Date(elementObject.get("dt").getAsInt() * 1000L);
            Date todayDate = new Date(todayCal.getTimeInMillis());
            Date tommorowAfter = new Date(afterTmrCal.getTimeInMillis());
            Date tommorow = null;
            //온도
            float temp = elementObject.get("temp").getAsFloat();

            String date = sdf.format(timeInDate);
            String today = sdf.format(todayDate);
            String sTomorrowAfter = sdf.format(tommorowAfter);
            try {
                tommorow = sdf.parse(date);
                todayDate = sdf.parse(today);
                tommorowAfter = sdf.parse(sTomorrowAfter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("dsgffsdg",dateFormat.format(timeInDate));
            if (tommorow.compareTo(todayDate) ==1
                    && tommorow.compareTo(tommorowAfter)==-1) {
                if(Integer.parseInt(dateFormat.format(timeInDate))%3==0 &&
                        Integer.parseInt(dateFormat.format(timeInDate))<=21){


                    hourList.add(Integer.parseInt(dateFormat.format(timeInDate)));
                    hourTemperatureList.add(Math.round(temp)+"");
                    Log.d("teadasd",temp+"");
                    JsonElement jsonElementWeather = jaWeather.get(0);
                    String icon = jsonElementWeather.getAsJsonObject().get("icon").getAsString();
                    String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";
                    Glide.with(getActivity()).load(iconUrl).into(imageButtons[index]);
                    index++;
                }

            }
        }
        JsonElement jsonElementDaily = jaDaily.get(1);
        Log.d("sdfgdfg",jsonElementDaily+"");
        JsonObject celsius = jsonElementDaily.getAsJsonObject().get("temp").getAsJsonObject();
        JsonObject humidityJson = jsonElementDaily.getAsJsonObject();
        humidity = humidityJson.get("humidity").getAsInt();
        humidityList.add(String.valueOf(humidityJson.get("humidity")));
        Log.d("celsius", celsius.toString());
        mornTemp = celsius.get("morn").getAsFloat();
        dayTemp = celsius.get("day").getAsFloat();
        eveTemp = celsius.get("eve").getAsFloat();
        nightTemp = celsius.get("night").getAsFloat();
        maxTemperature = celsius.get("max").getAsFloat();
        minTemperature = celsius.get("min").getAsFloat();
        JsonArray jaWeather = jsonElementDaily.getAsJsonObject().get("weather").getAsJsonArray();
        Log.d("dsfgsdfgsfdg",jaWeather+"");
        JsonElement jsonElementWeather = jaWeather.get(0);
        weatherId = jsonElementWeather.getAsJsonObject().get("id").getAsInt();

        weatherText.setText(KoreanFormat.EngToKor(weatherId));
        humidityText.setText(humidity + "%");
        maxTemp.setText((int) Math.round(maxTemperature) + "℃");
        minTemp.setText((int) Math.round(minTemperature) + "℃");

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(8, mornTemp));
        entries.add(new Entry(12, dayTemp));
        entries.add(new Entry(16, eveTemp));
        entries.add(new Entry(20, nightTemp));

        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.setLineWidth(1);
        lineDataSet.setCircleRadius(3);
        lineDataSet.setCircleColor(getActivity().getResources().getColor(R.color.gray300));
        lineDataSet.setCircleHoleColor(getActivity().getResources().getColor(R.color.white));
        lineDataSet.setColor(getActivity().getResources().getColor(R.color.gray500));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);


        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(getActivity().getResources().getColor(R.color.black));
        lineData.setValueTextSize(9);
        lineData.setValueFormatter(new TempValueFormatter());
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 8f) {
                    return "오전";
                } else if (value == 12f) {
                    return "점심";
                } else if (value == 16f) {
                    return "오후";
                } else if (value == 20f) {
                    return "밤";
                } else {
                    return "";
                }
            }
        });

        xAxis.enableGridDashedLine(4, 24, 0);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawLabels(false);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        yLAxis.setAxisMaximum(45f);
        yLAxis.setAxisMinimum(-30f);


        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.getLegend().setFormSize(0);
        lineChart.invalidate();

        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int i = 0; i <hourList.size(); i++) {
            entries2.add(new Entry(i, Float.parseFloat(hourTemperatureList.get(i))));
        }
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "");
        lineDataSet2.setLineWidth(1);
        lineDataSet2.setCircleRadius(3);
        lineDataSet2.setCircleColor(getActivity().getResources().getColor(R.color.gray300));
        lineDataSet2.setCircleHoleColor(getActivity().getResources().getColor(R.color.white));
        lineDataSet2.setColor(getActivity().getResources().getColor(R.color.gray500));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(true);

        LineData lineData2 = new LineData();
        lineData2.addDataSet(lineDataSet2);
        lineData2.setValueTextColor(getActivity().getResources().getColor(R.color.black));
        lineData2.setValueTextSize(9);
        lineData2.setValueFormatter(new TempValueFormatter());
        lineChart2.setData(lineData2);

        XAxis xAxis2 = lineChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextColor(Color.BLACK);
        xAxis2.setValueFormatter(new TimeValueFormatter(hourList));
        xAxis2.setDrawAxisLine(false);
        xAxis2.setDrawGridLines(false);

        YAxis yLAxis2 = lineChart2.getAxisLeft();
        yLAxis2.setTextColor(Color.BLACK);
        yLAxis2.setDrawLabels(false);
        yLAxis2.setDrawAxisLine(false);
        yLAxis2.setDrawGridLines(false);
        yLAxis2.setAxisMaximum(45f);
        yLAxis2.setAxisMinimum(-30f);

        YAxis yRAxis2 = lineChart2.getAxisRight();
        yRAxis2.setDrawLabels(false);
        yRAxis2.setDrawAxisLine(false);
        yRAxis2.setDrawGridLines(false);
        Description description2 = new Description();
        description2.setText("");

        lineChart2.setDoubleTapToZoomEnabled(false);
        lineChart2.setDrawGridBackground(false);
        lineChart2.setDescription(description2);
        lineChart2.getLegend().setFormSize(0);
        lineChart2.invalidate();

        completePage.setVisibility(View.VISIBLE);
//        getWeekWeather(String.valueOf(latitude), String.valueOf(longitude), "minutely,current", "metric", "650b8470989fceb2f4a95b3241a76d65");

    }

    public void getWeekWeather(String latitude, String longitude, String exclude, String units, String OPEN_WEATHER_MAP_KEY) {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();

        final AnimationDrawable drawable =
                (AnimationDrawable) runningImage.getBackground();
        drawable.start();

        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getWeekWeather(latitude, longitude, exclude, units, OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                float mornTemp;
                float dayTemp;
                float eveTemp;
                float nightTemp;
                float maxTemperature;
                float minTemperature;
                int humidity;
                int weatherId;

                List<String> humidityList = new ArrayList<>();
                List<Integer> hourList = new ArrayList<>();
                List<String> hourTemperatureList = new ArrayList<>();
                int index =0;

                JsonObject jsonObject = response.body();
                JsonArray jaDaily = jsonObject.getAsJsonArray("daily");
                JsonArray jaHourly = jsonObject.getAsJsonArray("hourly");
                Log.d("dsgsfdgdsfgs", jaHourly + "");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Calendar todayCal = Calendar.getInstance();
                Calendar afterTmrCal = Calendar.getInstance();
                afterTmrCal.add(Calendar.DATE,2);
                for (int i = 0; i < jaHourly.size(); i++) {
                    JsonElement jsonElementHourly = jaHourly.get(i);
                    JsonObject elementObject = jsonElementHourly.getAsJsonObject();
                    JsonArray jaWeather = jsonElementHourly.getAsJsonObject().get("weather").getAsJsonArray();
                    Date timeInDate = new Date(elementObject.get("dt").getAsInt() * 1000L);
                    Date todayDate = new Date(todayCal.getTimeInMillis());
                    Date tommorowAfter = new Date(afterTmrCal.getTimeInMillis());
                    Date tommorow = null;
                    //온도
                    float temp = elementObject.get("temp").getAsFloat();

                    String date = sdf.format(timeInDate);
                    String today = sdf.format(todayDate);
                    String sTomorrowAfter = sdf.format(tommorowAfter);
                    try {
                        tommorow = sdf.parse(date);
                        todayDate = sdf.parse(today);
                        tommorowAfter = sdf.parse(sTomorrowAfter);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (tommorow.compareTo(todayDate) ==1
                            && tommorow.compareTo(tommorowAfter)==-1) {
                        if(Integer.parseInt(dateFormat.format(timeInDate))%3==0 &&
                                Integer.parseInt(dateFormat.format(timeInDate))<=21){


                            hourList.add(Integer.parseInt(dateFormat.format(timeInDate)));
                            hourTemperatureList.add(Math.round(temp)+"");
                            JsonElement jsonElementWeather = jaWeather.get(0);
                            String icon = jsonElementWeather.getAsJsonObject().get("icon").getAsString();
                            String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";
                            Glide.with(getActivity()).load(iconUrl).into(imageButtons[index]);
                            index++;
                        }

                    }
                }
                JsonElement jsonElementDaily = jaDaily.get(1);
                Log.d("sdfgdfg",jsonElementDaily+"");
                JsonObject celsius = jsonElementDaily.getAsJsonObject().get("temp").getAsJsonObject();
                JsonObject humidityJson = jsonElementDaily.getAsJsonObject();
                humidity = humidityJson.get("humidity").getAsInt();
                humidityList.add(String.valueOf(humidityJson.get("humidity")));
                Log.d("celsius", celsius.toString());
                mornTemp = celsius.get("morn").getAsFloat();
                dayTemp = celsius.get("day").getAsFloat();
                eveTemp = celsius.get("eve").getAsFloat();
                nightTemp = celsius.get("night").getAsFloat();
                maxTemperature = celsius.get("max").getAsFloat();
                minTemperature = celsius.get("min").getAsFloat();
                JsonArray jaWeather = jsonElementDaily.getAsJsonObject().get("weather").getAsJsonArray();
                Log.d("dsfgsdfgsfdg",jaWeather+"");
                JsonElement jsonElementWeather = jaWeather.get(0);
                weatherId = jsonElementWeather.getAsJsonObject().get("id").getAsInt();

                weatherText.setText(KoreanFormat.EngToKor(weatherId));
                humidityText.setText(humidity + "%");
                maxTemp.setText((int) Math.round(maxTemperature) + "℃");
                minTemp.setText((int) Math.round(minTemperature) + "℃");

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(8, mornTemp));
                entries.add(new Entry(12, dayTemp));
                entries.add(new Entry(16, eveTemp));
                entries.add(new Entry(20, nightTemp));

                LineDataSet lineDataSet = new LineDataSet(entries, "");
                lineDataSet.setLineWidth(1);
                lineDataSet.setCircleRadius(3);
                lineDataSet.setCircleColor(getActivity().getResources().getColor(R.color.gray300));
                lineDataSet.setCircleHoleColor(getActivity().getResources().getColor(R.color.white));
                lineDataSet.setColor(getActivity().getResources().getColor(R.color.gray500));
                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setDrawHorizontalHighlightIndicator(false);
                lineDataSet.setDrawHighlightIndicators(false);
                lineDataSet.setDrawValues(true);


                LineData lineData = new LineData(lineDataSet);
                lineData.setValueTextColor(getActivity().getResources().getColor(R.color.black));
                lineData.setValueTextSize(9);
                lineData.setValueFormatter(new TempValueFormatter());
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        if (value == 8f) {
                            return "오전";
                        } else if (value == 12f) {
                            return "점심";
                        } else if (value == 16f) {
                            return "오후";
                        } else if (value == 20f) {
                            return "밤";
                        } else {
                            return "";
                        }
                    }
                });

                xAxis.enableGridDashedLine(4, 24, 0);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);

                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);
                yLAxis.setDrawLabels(false);
                yLAxis.setDrawAxisLine(false);
                yLAxis.setDrawGridLines(false);
                yLAxis.setAxisMaximum(45f);
                yLAxis.setAxisMinimum(-30f);


                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                Description description = new Description();
                description.setText("");

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setDescription(description);
                lineChart.getLegend().setFormSize(0);
                lineChart.invalidate();

                ArrayList<Entry> entries2 = new ArrayList<>();
                for (int i = 0; i <hourList.size(); i++) {
                    entries2.add(new Entry(i, Float.parseFloat(hourTemperatureList.get(i))));
                }
                LineDataSet lineDataSet2 = new LineDataSet(entries2, "");
                lineDataSet2.setLineWidth(1);
                lineDataSet2.setCircleRadius(3);
                lineDataSet2.setCircleColor(getActivity().getResources().getColor(R.color.gray300));
                lineDataSet2.setCircleHoleColor(getActivity().getResources().getColor(R.color.white));
                lineDataSet2.setColor(getActivity().getResources().getColor(R.color.gray500));
                lineDataSet2.setDrawCircleHole(true);
                lineDataSet2.setDrawCircles(true);
                lineDataSet2.setDrawHorizontalHighlightIndicator(false);
                lineDataSet2.setDrawHighlightIndicators(false);
                lineDataSet2.setDrawValues(true);

                LineData lineData2 = new LineData();
                lineData2.addDataSet(lineDataSet2);
                lineData2.setValueTextColor(getActivity().getResources().getColor(R.color.black));
                lineData2.setValueTextSize(9);
                lineData2.setValueFormatter(new TempValueFormatter());
                lineChart2.setData(lineData2);

                XAxis xAxis2 = lineChart2.getXAxis();
                xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis2.setTextColor(Color.BLACK);
                xAxis2.setValueFormatter(new TimeValueFormatter(hourList));
                xAxis2.setDrawAxisLine(false);
                xAxis2.setDrawGridLines(false);

                YAxis yLAxis2 = lineChart2.getAxisLeft();
                yLAxis2.setTextColor(Color.BLACK);
                yLAxis2.setDrawLabels(false);
                yLAxis2.setDrawAxisLine(false);
                yLAxis2.setDrawGridLines(false);
                yLAxis2.setAxisMaximum(45f);
                yLAxis2.setAxisMinimum(-30f);

                YAxis yRAxis2 = lineChart2.getAxisRight();
                yRAxis2.setDrawLabels(false);
                yRAxis2.setDrawAxisLine(false);
                yRAxis2.setDrawGridLines(false);
                Description description2 = new Description();
                description2.setText("");

                lineChart2.setDoubleTapToZoomEnabled(false);
                lineChart2.setDrawGridBackground(false);
                lineChart2.setDescription(description2);
                lineChart2.getLegend().setFormSize(0);
                lineChart2.invalidate();

                drawable.stop();
                completePage.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
