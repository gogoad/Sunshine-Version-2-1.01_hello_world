package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jxa on 2016/9/7.
 */
public class ForecastFragment extends Fragment {

    ArrayAdapter<String> adapter;
    ListView mListForecoast;
    HttpURLConnection conn = null;
    BufferedReader reader = null;
    String forecastJsonStr = null;
    String line;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //表示fragment可以处理菜单事件
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("东营");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastArray = {
                "星期一--晴--88/64",
                "星期二--雾--88/64",
                "星期三--多云--88/64",
                "星期四--雷电--88/64",
                "星期五--小雨--88/64",
                "星期六--中雨--88/64",
                "星期天--大雾--88/64"
        };

        /**
         * (1) 该方法对于基本数据类型的数组支持并不好,当数组是基本数据类型时不建议使用
         * (2) 当使用asList()方法时，数组就和列表链接在一起了.
         *     当更新其中之一时，另一个将自动获得更新。
         *     注意:仅仅针对对象数组类型,基本数据类型数组不具备该特性
         * (3) asList得到的数组是的没有add和remove方法的
         */
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, weekForecast);


        mListForecoast = (ListView) rootView.findViewById(R.id.listview_forecast);

        mListForecoast.setAdapter(adapter);

        return rootView;
    }

    /**
     * 使用异步加载
     */
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        int mode = 2;
        String key = "f4b315b4dcc97fb44e2a52edf23da44d";


        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            try {
                // URL url = new URL("http://v.juhe.cn/weather/index?format=2&cityname=%E9%95%BF%E6%B2%99&key=f4b315b4dcc97fb44e2a52edf23da44d");
                String FORECAST_BASE_URL = "http://v.juhe.cn/weather/index?";
                String FORMAT_PARAM = "format";
                String QUERY_PARAM = "cityname";
                String APPID_PARAM = "key";
                Uri buildUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(FORMAT_PARAM, Integer.toString(mode))
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(APPID_PARAM, key)
                        .build();
                URL url = new URL(buildUri.toString());
                Log.e("我的uri", buildUri.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                //得到一个input流
                InputStream inputStream = conn.getInputStream();
                //将input流转换成字符串
                StringBuffer buffer = new StringBuffer();
                //没有数据返回
                if (inputStream == null) {
                    forecastJsonStr = null;
                }
                //读取数据
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.e("返回的数据", forecastJsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                return getWeatherDataFromJson(forecastJsonStr,7);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                adapter.clear();
                for (String forecastResult: result) {
                    adapter.add(forecastResult);

                }
            }
        }
    }


    private String[] getWeatherDataFromJson(String forecastJsonStr,int numDays) throws JSONException{
        String[] resultStr = new String[numDays];

            JSONObject jsonObeject = new JSONObject(forecastJsonStr);
            String resultcode = jsonObeject.getString("resultcode");
            /*if (resultcode.equals("200")) {
                JSONObject result = jsonObeject.getJSONObject("result");
                JSONObject today = result.getJSONObject("today");
                String date_y = today.getString("date_y");
                String weather = today.getString("weather");
                String temperature = today.getString("temperature");
                String week = today.getString("week");
                String dressing_advice = today.getString("dressing_advice");
                Log.e("解析出来的数据",date_y+week+temperature+weather+dressing_advice);
            }*/

            if (resultcode.equals("200")) {
                JSONObject result = jsonObeject.getJSONObject("result");
                JSONArray future = result.getJSONArray("future");
                for (int i = 0; i < future.length(); i++) {
                    JSONObject jsonObject = future.getJSONObject(i);
                    String date = jsonObject.getString("date");
                    String temperature = jsonObject.getString("temperature");
                    String weather = jsonObject.getString("weather");
                    String week = jsonObject.getString("week");
                    String wind = jsonObject.getString("wind");
                    // Log.e("解析出来的数据",date+"-"+week+"-"+temperature+"-"+weather+"-"+wind);
                    resultStr[i] = date + "-" + week + "-" + temperature + "-" + weather + "-" + wind;
                }
            }

        return resultStr;
    }

}
