package com.example.yourschedule.FRAGMENT;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.ForRetrofit.RetrofitService;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WeatherOfWeek extends Fragment {


    private LineChart lineChart, lineChart2;
    private TextView weatherText, maxTemp, minTemp, humidityText;
    private ImageButton[] imageButtons;
    private ImageView runningImage;

    private RelativeLayout loadingPage;
    private LinearLayout completeLoadDataPage;


    double longitude;
    double latitude;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};




    public WeatherOfWeek newInstance() {
        return new WeatherOfWeek();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("json", "WeatherOfWeek onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("json", "WeatherOfWeek onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_weather_of_week, container, false);
        runningImage = rootView.findViewById(R.id.running_image);
        loadingPage = rootView.findViewById(R.id.weatherLoading);
        completeLoadDataPage = rootView.findViewById(R.id.completeWeatherPage);
        weatherText = rootView.findViewById(R.id.weatherText);
        maxTemp = rootView.findViewById(R.id.maxTemperature);
        minTemp = rootView.findViewById(R.id.minTemperature);
        humidityText = rootView.findViewById(R.id.humidity);
        lineChart = rootView.findViewById(R.id.linechart);
        lineChart2 = rootView.findViewById(R.id.linechart2);
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
        final LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("sfdgsdfg",longitude+" : "+ latitude);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }




        getWeekWeather( String.valueOf(latitude), String.valueOf(longitude), "minutely,current", "metric", "650b8470989fceb2f4a95b3241a76d65");
    }

    public void getWeekWeather(String latitude, String longitude, String exclude, String units, String OPEN_WEATHER_MAP_KEY) {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();

        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getWeekWeather(latitude, longitude, exclude, units, OPEN_WEATHER_MAP_KEY);
        final AnimationDrawable drawable =
                (AnimationDrawable) runningImage.getBackground();
        drawable.start();
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
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

                    JsonObject jsonObject = response.body();
                    JsonArray jaDaily = jsonObject.getAsJsonArray("daily");
                    JsonArray jaHourly = jsonObject.getAsJsonArray("hourly");

                    Log.d("JaDaily", jaDaily + "");
                    Log.i("jaHourly", jaHourly + "");
                    SimpleDateFormat sdf = new SimpleDateFormat("ddHH");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH");

                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
                    for (int i = 0; i <8; i++) {
                        JsonElement jsonElementHourly = jaHourly.get(i);
                        Log.d("element", jsonElementHourly + "");
                        JsonObject elementObject = jsonElementHourly.getAsJsonObject();
                        JsonArray jaWeather = jsonElementHourly.getAsJsonObject().get("weather").getAsJsonArray();
                        Date timeInDate = new Date(elementObject.get("dt").getAsInt() * 1000L);
                        float temp = elementObject.get("temp").getAsFloat();
                        String main = String.valueOf(elementObject.get("main"));

//                      String timeInFormat = sdf.format(timeInDate);
                        String date = dateFormat.format(timeInDate);
                        hourList.add(Integer.parseInt(date));
                        hourTemperatureList.add(Math.round(temp)+"");
                        JsonElement jsonElementWeather = jaWeather.get(0);
                        String icon = jsonElementWeather.getAsJsonObject().get("icon").getAsString();
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + ".png";
                        Glide.with(getActivity()).load(iconUrl).into(imageButtons[i]);
                    }
                    Log.d("hourSize",hourList.size()+"");
                    for(int i=0;i<hourList.size();i++){
                        Log.d("myHourList",hourList.get(i)+"");
                    }
                    JsonElement jsonElementDaily = jaDaily.get(0);
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
                    JsonElement jsonElementWeather = jaWeather.get(0);
                    weatherId = jsonElementWeather.getAsJsonObject().get("id").getAsInt();

                    weatherText.setText(EngToKor(weatherId));
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
                    //여기까지

                    //lineChar2

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
                    lineChart2.setDescription(description);
                    lineChart2.getLegend().setFormSize(0);
                    lineChart2.invalidate();
                    drawable.stop();
                    completeLoadDataPage.setVisibility(View.VISIBLE);
                    loadingPage.setVisibility(View.INVISIBLE);
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("SFgfdgdfg","실패");
            }
        });
    }


    String EngToKor(int id) {
        int index[] = {201, 200, 202, 210, 211, 212, 221, 230, 231, 232,
                300, 301, 302, 310, 311, 312, 313, 314, 321, 500,
                501, 502, 503, 504, 511, 520, 521, 522, 531, 600,
                601, 602, 611, 612, 615, 616, 620, 621, 622, 701,
                711, 721, 731, 741, 751, 761, 762, 771, 781, 800,
                801, 802, 803, 804, 900, 901, 902, 903, 904, 905,
                906, 951, 952, 953, 954, 955, 956, 957, 958, 959,
                960, 961, 962};

        String values[] = {"가벼운 비를 동반한 천둥구름", "비를 동반한 천둥구름", "폭우를 동반한 천둥구름", "약한 천둥구름",
                "천둥구름", "강한 천둥구름", "불규칙적 천둥구름", "약한 연무를 동반한 천둥구름", "연무를 동반한 천둥구름",
                "강한 안개비를 동반한 천둥구름", "가벼운 안개비", "안개비", "강한 안개비", "가벼운 적은비", "적은비",
                "강한 적은비", "소나기와 안개비", "강한 소나기와 안개비", "소나기", "악한 비", "중간 비", "강한 비",
                "매우 강한 비", "극심한 비", "우박", "약한 소나기 비", "소나기 비", "강한 소나기 비", "불규칙적 소나기 비",
                "가벼운 눈", "눈", "강한 눈", "진눈깨비", "소나기 진눈깨비", "약한 비와 눈", "비와 눈", "약한 소나기 눈",
                "소나기 눈", "강한 소나기 눈", "박무", "연기", "연무", "모래 먼지", "안개", "모래", "먼지", "화산재", "돌풍",
                "토네이도", "구름 한 점 없는 맑은 하늘", "약간의 구름이 낀 하늘", "드문드문 구름이 낀 하늘", "구름이 거의 없는 하늘",
                "구름으로 뒤덮인 흐린 하늘", "토네이도", "태풍", "허리케인", "한랭", "고온", "바람부는", "우박", "바람이 거의 없는",
                "약한 바람", "부드러운 바람", "중간 세기 바람", "신선한 바람", "센 바람", "돌풍에 가까운 센 바람", "돌풍",
                "심각한 돌풍", "폭풍", "강한 폭풍", "허리케인"};

        for (int i = 0; i < index.length; i++) {
            if (index[i] == id) {
                return values[i];
            }
        }
        return "none";
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };



}
