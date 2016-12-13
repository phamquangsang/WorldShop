/*
// Make new offer or edit existing offer
//If Offer passed in parameter has offerId => 'update'
//else 'make new offer'
*/
package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestFutureTarget;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.api.CityApi;
import thefour.com.worldshop.api.OfferApi;
import thefour.com.worldshop.databinding.ActivityMakeOfferBinding;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class MakeOfferActivity extends AppCompatActivity {
    private static final String ARG_USER = "arg_user";
    private static final String ARG_REQUEST = "arg_request";
    private static final String ARG_UPDATED_OFFER = "arg_offer";
    private static final String TAG = MakeOfferActivity.class.getSimpleName();

    private ActivityMakeOfferBinding mBinding;
    private User mUser;
    private Request mRequest;
    private Offer mOffer;
    private ArrayList<City> mCities;



    public static Intent getIntent(Context c, User user, Request request,@Nullable Offer updatedOffer) {
        Intent i = new Intent(c, MakeOfferActivity.class);
        i.putExtra(ARG_USER, user);
        i.putExtra(ARG_REQUEST, request);
        i.putExtra(ARG_UPDATED_OFFER, updatedOffer);
        return i;
    }

    @BindingAdapter("android:imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url != null) {
            Glide.with(imageView.getContext()).load(url).placeholder(R.drawable.placeholder).into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make_offer);
        setSupportActionBar(mBinding.toolbar);

        mOffer = getIntent().getParcelableExtra(ARG_UPDATED_OFFER);
        if(mOffer == null){
            mOffer = new Offer();
        }
        mUser = getIntent().getParcelableExtra(ARG_USER);
        mRequest = getIntent().getParcelableExtra(ARG_REQUEST);
        mCities = new ArrayList<>();

        setupView();

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateOffer()) {
                    finish();
                    sendOffer();
                }
            }
        });
    }


    private boolean validateOffer() {
        boolean result = true;
        if (!Util.validateEmptyInput(this, mBinding.container.editTextFee, mBinding.container.inputFee))
            result = false;
        if (!validateDate()) {
            Snackbar.make(mBinding.getRoot(), "Deliver day should be after today!", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (!Util.validateEmptyInput(this, mBinding.container.editTextDeliverFrom, mBinding.container.inputItemDeliverFrom)) {
            result = false;
        }

        if (!Util.validateCity(mBinding.container.editTextDeliverFrom.getText().toString(),
                mBinding.container.inputItemDeliverFrom, mCities))
            result = false;
        return result;
    }

    private boolean validateDate() {
        int dd, mm, yyyy;
        dd = mBinding.container.datePicker.getDayOfMonth();
        mm = mBinding.container.datePicker.getMonth();
        yyyy = mBinding.container.datePicker.getYear();

        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);


        Date today = c.getTime();
        c.set(Calendar.YEAR, yyyy);
        c.set(Calendar.MONTH, mm);
        c.set(Calendar.DAY_OF_MONTH, dd);

        Date dateSpecified = c.getTime();

        return (dateSpecified.after(today));
    }

    private long getDeliverDateInMilis() {
        int dd, mm, yyyy;
        dd = mBinding.container.datePicker.getDayOfMonth();
        mm = mBinding.container.datePicker.getMonth();
        yyyy = mBinding.container.datePicker.getYear();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, yyyy);
        c.set(Calendar.MONTH, mm);
        c.set(Calendar.DAY_OF_MONTH, dd);

        return c.getTime().getTime();
    }


    private void setupView() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mRequest.getItem().getName());
        mBinding.setRequest(mRequest);
        mBinding.container.setRequest(mRequest);
        mBinding.container.editTextFee.setText
                (String.valueOf(0.05d * mRequest.getItem().getPrice() * mRequest.getQuantity()));

        Glide.with(this)
                .load(mRequest.getItem().getFirstImage())
                .placeholder(R.drawable.placeholder)
                .into(mBinding.container.itemImageView);

        setUpAutoCompleteCities();
    }

    private void setUpAutoCompleteCities() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_expandable_list_item_1);
        CityApi.loadCities(new CityApi.OnLoadCompleted() {
            @Override
            public void onLoadCompleted(ArrayList<City> cities) {
                mCities = cities;
                for (City city :
                        cities) {
                    adapter.add(city.getName());
                }
                adapter.notifyDataSetChanged();
                //Toast.makeText(ShoppingActivity.this, "load cities completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadCancelled(DatabaseError error) {

            }
        });
        mBinding.container.editTextDeliverFrom.setAdapter(adapter);

    }

    private void sendOffer() {
        String cityInput = mBinding.container.editTextDeliverFrom.getText().toString();
        for (City city :
                mCities) {
            if (city.getName().equalsIgnoreCase(cityInput)) {
                mOffer.setDeliverFrom(city);
                break;
            }
        }
        mOffer.setDeliveryDate(getDeliverDateInMilis());
        mOffer.setFromUser(mUser);
        mOffer.setItem(mRequest.getItem());
        mOffer.setFee(Double.valueOf(mBinding.container.editTextFee.getText().toString()));
        mOffer.setStatus(Offer.STATUS_PENDING);
        mOffer.setLastTimeEdited(System.currentTimeMillis());
        mOffer.setTime(System.currentTimeMillis());
        mOffer.setNote(mBinding.container.editTextNote.getText().toString());

        DatabaseReference.CompletionListener completeListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e(TAG, "onComplete: make offer failed", databaseError.toException());
                } else {
                    Toast.makeText(getApplicationContext(), "Make offer succeed!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        if(mOffer.getOfferId()==null){//make new offer
            OfferApi.makeOffer(mRequest, mOffer, completeListener);
        }else{//update offer
            OfferApi.updateOffer(mRequest, mOffer, completeListener);
        }

    }
}
