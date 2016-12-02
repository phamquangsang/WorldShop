package thefour.com.worldshop.api;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.City;

/**
 * Created by Quang Quang on 11/23/2016.
 */

public class CityApi {
    public interface OnLoadCompleted{
        void onLoadCompleted(ArrayList<City> cities);
        void onLoadCancelled(DatabaseError error);
    }
    public static void loadCities(final OnLoadCompleted onLoadCompleted){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.CITIES_LOCATION);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<City> cities = new ArrayList<City>();
                for (DataSnapshot city :
                        dataSnapshot.getChildren()) {
                    City cityObject = city.getValue(City.class);
                    cities.add(cityObject);
                }
                onLoadCompleted.onLoadCompleted(cities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onLoadCompleted.onLoadCancelled(databaseError);
            }
        });
    }
}
