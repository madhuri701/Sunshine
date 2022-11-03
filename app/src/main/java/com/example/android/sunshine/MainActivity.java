package com.example.android.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.w3c.dom.Text;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler {
   private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
private ForecastAdapter mForecastAdapter;

 private TextView mErrorMessageDisplay;
 //add a progress bar
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);


        mRecyclerView =(RecyclerView)findViewById(R.id.recyclerview_forecast);

        mErrorMessageDisplay = (TextView)findViewById(R.id.tv_error_message_display);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(LayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);

        //setting the adapter attaches it to the RecyclerView in our layout

        mRecyclerView.setAdapter(mForecastAdapter);

        mLoadingIndicator=(ProgressBar)findViewById(R.id.pd_loading_indicator);

        loadWeatherData();
    }
private void loadWeatherData()
{
    //call showWeatherDataView before executing the Asyntask
    showWeatherDataView();
    String location = SunshinePreferences.getPreferredWeatherLocation(this);
    new fetchWeatherTask().execute(location);



}

public void onClick(String weatherForDay)
{
    Context context = this;

    Class destinationActivity = DetailActivity.class;

    Intent intentToStartDetailActivity = new Intent(context, destinationActivity);
     intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT,weatherForDay);

    startActivity(intentToStartDetailActivity);
}

    /**
     * This method will make the view for the weather data visible and
     * hide the error message.
     * <p>
     *     Since it is okay to redundantly set the visibility of a view,we don't
     *     need to check whether each view is currently visible or invisible.
     */
     private void showWeatherDataView()
     {
       // first, make sure the error s invisible
         mErrorMessageDisplay.setVisibility(View.INVISIBLE);
         //then make sure the weather data is visible
         mRecyclerView.setVisibility(View.VISIBLE);
     }

    /**
     * This method will make the error message visible and hide the weather
     * view.
     * <p>
     *     Since it is okay to redundantly set the visibility of a view, we don't
     *     need to check whether each view is currently visible or invisible
     *
     */

    private void showErrorMessage()
    {
       mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }
    public class fetchWeatherTask extends AsyncTask<String,Void,String[]>
{

 @Override
 protected void onPreExecute()
    {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    @Override
    protected String[] doInBackground(String... params) {
        //if there is zip code there is nothing to find
        if(params.length==0)
        {
            return null;
        }
        String location = params[0];
        URL weatherRequestUrl = NetworkUtils.buildUrl(location);
        try
            {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpleJsonWeatherData= OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,jsonWeatherResponse);
                return simpleJsonWeatherData;

            }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    @Override
    protected void onPostExecute(String[] weatherData)
    {
        //As soon as the data is finsihed, hide loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
       if(weatherData!=null)
       {
           showWeatherDataView();
           mForecastAdapter.setmWeatherData(weatherData);
       }
       else
       {
           // if weather data is was null, show error message
           showErrorMessage();
       }
    }
}
private void openLocationMap()
{
    String addressString = "1680 Amphitheatre parkway,CA";

    Uri geoLocation = Uri.parse("geo:0,0?qs"+addressString);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(geoLocation);

    if(intent.resolveActivity(getPackageManager())!=null)
    {
        startActivity(intent);
    }
    else
    {
        Log.d(TAG,"Could not call"+geoLocation.toString()+"no recieving app");
    }
}
    public boolean onCreateOptionsMenu(Menu menu) {
        //Use AppCompactActivity's method getMenuInflater to get a handle on the menu inflater
        getMenuInflater().inflate(R.menu.main,menu);
//use the inflater's inflate method to inflate our menu layout to this menu
        // return true so that menu is displayed in the toolbar
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
       if(id==R.id.action_refresh) {
           mForecastAdapter.setmWeatherData(null);
           loadWeatherData();
           return true;
       }
           if(id==R.id.action_map)
           {
               openLocationMap();
               return true;
           }

return super.onOptionsItemSelected(item);
    }


}