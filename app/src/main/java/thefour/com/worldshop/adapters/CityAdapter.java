package thefour.com.worldshop.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.databinding.ItemCityBinding;
import thefour.com.worldshop.models.City;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by phatnguyen on 12/2/16.
 */

public final class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {
    private final Activity mActivity;
    private final List<City> mCities;
    private static final int mLayoutName;

    static
    {
        mLayoutName = R.layout.item_city;
    }

    public CityAdapter(Activity mActivity, ArrayList<City> mCities) {
        this.mActivity = mActivity;
        this.mCities = mCities;
    }


    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(mLayoutName, parent, false);
        return new CityHolder(v);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        City city = mCities.get(position);
        if (city != null)
        {
            //text
            holder.mBinding.setCity(mCities.get(position));
            holder.mBinding.textViewItemCity.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.OPENSANS_REGULAR));
            holder.mBinding.textViewItemCityCountry.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.OPENSANS_LIGHT));
            //image
            String url = (city.getImage() == null ? City.getDefaulUrl() : city.getImage());
            Glide.with(mActivity)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.mBinding.imageViewItemCity);
        }


    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    static class CityHolder extends RecyclerView.ViewHolder {
        private final ItemCityBinding mBinding;
        public CityHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }


}
