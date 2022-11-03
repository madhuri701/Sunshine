package com.example.android.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{


    private String[] mWeatherData;

    private final ForecastAdapterOnClickHandler mclickHandler;

    public interface ForecastAdapterOnClickHandler{

        void onClick(String weatherForDay);

    }


public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler)
{
    mclickHandler = clickHandler;

}
    //cache of the children views for a forecast list item
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
       public final TextView mWeatherTextView;
       public ForecastAdapterViewHolder(View view)
       {
           super(view);
           mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
          view.setOnClickListener(this);

       }
       @Override
       public void onClick(View v)
       {
           int adapterPosition = getAdapterPosition();
           String weatherForDay = mWeatherData[adapterPosition];
           mclickHandler.onClick(weatherForDay);
       }
    }
    //override onCreateViewHolder
    //Within onCreateViewHolder, inflate the list item xml into a view
    // within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a
@Override
public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType)
{
    Context context = viewGroup.getContext();
    int layoutIdForListItem = R.layout.forecast_list_item;
    LayoutInflater inflater = LayoutInflater.from(context);
    boolean shouldAttachToParentImmediately = false;
    View view = inflater.inflate(layoutIdForListItem, viewGroup,shouldAttachToParentImmediately);
    return new ForecastAdapterViewHolder(view);
}

@Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position)
{
     String weatherForThisDay = mWeatherData[position];
     forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);

}

@Override
    public int getItemCount()
{

    if(null==mWeatherData)
    {
        return 0;
    }
    return mWeatherData.length;

}


public void setmWeatherData(String[]weatherData)
{
    mWeatherData = weatherData;
    notifyDataSetChanged();
}
}
