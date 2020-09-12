package com.example.yourschedule.FRAGMENT;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.yourschedule.ForRetrofit.RetrofitService;
import com.example.yourschedule.Formatter.TempValueFormatter;
import com.example.yourschedule.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherOfWeek extends Fragment {

    private Retrofit mRetrofit;
    private RetrofitService mRetrofitService;
    private Call<JsonObject> mCallWeekWeather;


    private LineChart lineChart;
    JsonObject jsonObject;
    private TextView weatherText;
//    ImageButton[] imageButtons;
    View rootView;
    LinearLayout daily;
    String[] morn;
    String[] day;
    String[] eve;
    String[] night;

    //private String jsonObject;

    public WeatherOfWeek newInstance() {
        return new WeatherOfWeek();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetrofitInit();
        callWeekWeather();
        Log.d("json", "WeatherOfWeek onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("json", "WeatherOfWeek onCreateView");
        rootView = inflater.inflate(R.layout.fragment_weather_of_week, container, false);
        morn = new String[10];
        day = new String[10];
        eve = new String[10];
        night = new String[10];
        weatherText = rootView.findViewById(R.id.weatherText);
        lineChart = rootView.findViewById(R.id.linechart);

        String pkg = getActivity().getPackageName();
//        imageButtons = new ImageButton[8];
//        for(int i=1;i<imageButtons.length;i++) {
//            int id = getResources().getIdentifier("weatherIcon" + i, "id", pkg);
//            Log.d("sdafasdfasdfsdaf",id+"");
//            imageButtons[i-1] = rootView.findViewById(id);
//        }

//        String pkg2 = getActivity().getPackageName();
//        int id2 = getResources().getIdentifier("linechart","id2",pkg2);



        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("json", "WeatherOfWeek onViewCreated");
    }

    private void setRetrofitInit() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitService = mRetrofit.create(RetrofitService.class);
    }

    private void callWeekWeather() {
        mCallWeekWeather = mRetrofitService.getWeekWeather(
                "37.56826",
                "126.977829",
                "minutely,current",
                "metric",
                "650b8470989fceb2f4a95b3241a76d65"
        );
        mCallWeekWeather.enqueue(mRetrofitCallback);


    }

    private Callback<JsonObject> mRetrofitCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



            jsonObject = response.body();

            String lat = jsonObject.get("lat").getAsString();
            String lon = jsonObject.get("lon").getAsString();
            String timeZone = jsonObject.get("timezone").getAsString();
            List<Integer> weatherIdList = new ArrayList<>();

            JsonArray jaDaily = jsonObject.getAsJsonArray("daily");
            JsonArray jaHourly = jsonObject.getAsJsonArray("hourly");

            Log.d("JaDaily",jaDaily+"");
            Log.i("jaHourly", jaHourly+"");
            SimpleDateFormat sdf = new SimpleDateFormat("dd일 HH시");

            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
            for(int i=0;i<jaHourly.size();i++){
                JsonElement jsonElementDaily = jaHourly.get(i);
                Log.d("element",jsonElementDaily+"");
                JsonObject jsonObject = jsonElementDaily.getAsJsonObject();
                Date timeInDate = new Date(Integer.parseInt(String.valueOf(jsonObject.get("dt"))) *1000L);
                Double temp = Double.parseDouble(String.valueOf(jsonObject.get("temp")));
                String main = String.valueOf(jsonObject.get("main"));
                String timeInFormat = sdf.format(timeInDate);
                Log.d("temp",timeInFormat);
                Log.d("temp",temp+"");
                Log.d("temp",main);
            }

            for (int i = 0; i < jaDaily.size()-1; i++){
                JsonElement jsonElementDaily = jaDaily.get(i);
                JsonObject celsius = jsonElementDaily.getAsJsonObject().get("temp").getAsJsonObject();
                Log.d("celsius",celsius.toString());
                morn[i] = celsius.get("morn").getAsString();
                day[i] = celsius.get("day").getAsString();
                eve[i] = celsius.get("eve").getAsString();
                night[i] = celsius.get("night").getAsString();

                JsonArray jaWeather = jsonElementDaily.getAsJsonObject().get("weather").getAsJsonArray();

                JsonElement jsonElementWeather = jaWeather.get(0);
                Log.d("Sdfgsdfgsfg",jsonElementWeather+"");
                weatherIdList.add(jsonElementWeather.getAsJsonObject().get("id").getAsInt());
//                String main = jsonElementWeather.getAsJsonObject().get("main").getAsString();
                String icon = jsonElementWeather.getAsJsonObject().get("icon").getAsString();
                Log.d("test", icon + "");

                String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";
                Log.d("test", iconUrl + "");

//                Glide.with(getActivity()).load(iconUrl).into(imageButtons[i]);

//                Log.d("JsonTest", main + "");
            }
                weatherText.setText(EngToKor(weatherIdList.get(0)));

            float mornTemp = 0;
            float dayTemp = 0;
            float eveTemp = 0;
            float nightTemp = 0;
            try {
                mornTemp= Float.parseFloat(morn[0]);
                dayTemp= Float.parseFloat(day[0]);
                eveTemp= Float.parseFloat(eve[0]);
                nightTemp = Float.parseFloat(night[0]);

                Log.d("atest", morn[0]+"");
                Log.d("atest", day[0]+"");
                Log.d("atest", eve[0]+"");
                Log.d("atest", night[0]+"");
            }
            catch (Exception e){
                Log.d("atest", e+"");
            }
            List<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            entries.add(new Entry(8,mornTemp));
            entries.add(new Entry(12, dayTemp));
            entries.add(new Entry(16, eveTemp));
            entries.add(new Entry(20, nightTemp));

//        entries.add(new Entry(5, 3));

            LineDataSet lineDataSet = new LineDataSet(entries, "일간 날씨");
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
            xAxis.setValueFormatter(new IndexAxisValueFormatter(){
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
                    }else{
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
            yLAxis.setAxisMaximum(100f);

            YAxis yRAxis = lineChart.getAxisRight();
            yRAxis.setDrawLabels(false);
            yRAxis.setDrawAxisLine(false);
            yRAxis.setDrawGridLines(false);

            Description description = new Description();
            description.setText("");

            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription(description);
            lineChart.invalidate();
            //여기까지


        }

        @Override
        public void onFailure (Call < JsonObject > call, Throwable t){
            Log.d("JSON W", "실패");
        }
    };

    String EngToKor(int id){
        int index[] = {201,200,202,210,211,212,221,230,231,232,
                300,301,302,310,311,312,313,314,321,500,
                501,502,503,504,511,520,521,522,531,600,
                601,602,611,612,615,616,620,621,622,701,
                711,721,731,741,751,761,762,771,781,800,
                801,802,803,804,900,901,902,903,904,905,
                906,951,952,953,954,955,956,957,958,959,
                960,961,962};

        String values[] = {"가벼운 비를 동반한 천둥구름","비를 동반한 천둥구름","폭우를 동반한 천둥구름","약한 천둥구름",
                "천둥구름","강한 천둥구름","불규칙적 천둥구름","약한 연무를 동반한 천둥구름","연무를 동반한 천둥구름",
                "강한 안개비를 동반한 천둥구름","가벼운 안개비","안개비","강한 안개비","가벼운 적은비","적은비",
                "강한 적은비","소나기와 안개비","강한 소나기와 안개비","소나기","악한 비","중간 비","강한 비",
                "매우 강한 비","극심한 비","우박","약한 소나기 비","소나기 비","강한 소나기 비","불규칙적 소나기 비",
                "가벼운 눈","눈","강한 눈","진눈깨비","소나기 진눈깨비","약한 비와 눈","비와 눈","약한 소나기 눈",
                "소나기 눈","강한 소나기 눈","박무","연기","연무","모래 먼지","안개","모래","먼지","화산재","돌풍",
                "토네이도","구름 한 점 없는 맑은 하늘","약간의 구름이 낀 하늘","드문드문 구름이 낀 하늘","구름이 거의 없는 하늘",
                "구름으로 뒤덮인 흐린 하늘","토네이도","태풍","허리케인","한랭","고온","바람부는","우박","바람이 거의 없는",
                "약한 바람","부드러운 바람","중간 세기 바람","신선한 바람","센 바람","돌풍에 가까운 센 바람","돌풍",
                "심각한 돌풍","폭풍","강한 폭풍","허리케인"};

        for(int i=0; i<index.length; i++) {
            if(index[i]==id) {
                return values[i];
            }
        }
        return "none";
    }


}
