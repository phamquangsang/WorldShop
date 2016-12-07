package thefour.com.worldshop.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.databinding.ItemCityBinding;
import thefour.com.worldshop.models.City;

/**
 * Created by phatnguyen on 12/2/16.
 */

public final class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {
    private final Activity mActivity;
    private final List<City> mCities;
    private static final int mLayoutName;

    private static final CitySelectedEvent onCitySelectedEvent;

    static {
        mLayoutName = R.layout.item_city;
        onCitySelectedEvent = new CitySelectedEvent();
    }

    public CityAdapter(Activity mActivity, ArrayList<City> mCities) {
        this.mActivity = mActivity;
        this.mCities = mCities;

    }

    public List<City> getCities() {
        return mCities;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(mLayoutName, parent, false);
        return new CityHolder(v);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        City city = mCities.get(position);
        if (city != null) {
            //text
            holder.mBinding.setCity(mCities.get(position));
            holder.mBinding.textViewItemCity.setTypeface(TypefaceCache.get(mActivity, TypefaceCache.HARMONIA_BOLD));
            holder.mBinding.textViewItemCityCountry.setTypeface(TypefaceCache.get(mActivity, TypefaceCache.HARMONIA_REGULAR));
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

    public static final class CitySelectedEvent {
        private City mCity;

        public City getCity() {
            return mCity;
        }

        public void setCity(City mCity) {
            this.mCity = mCity;
        }
    }

    class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemCityBinding mBinding;

        CityHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.imageViewItemCity.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageView_item_city:
                    onCitySelectedEvent.setCity(mCities.get(getAdapterPosition()));
                    EventBus.getDefault().post(onCitySelectedEvent);
                    break;
            }
        }
    }


}
