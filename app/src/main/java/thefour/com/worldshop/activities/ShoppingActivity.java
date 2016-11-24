package thefour.com.worldshop.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.ItemImageAdapter;
import thefour.com.worldshop.api.CityApi;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.api.StorageApi;
import thefour.com.worldshop.databinding.ActivityShoppingBinding;
import thefour.com.worldshop.databinding.ContentShoppingBinding;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Item;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class ShoppingActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_CODE_PICK_PHOTO = 10;
    private static final String ARG_LOGGED_USER = "arg_logged_user";
    private static final String TAG = ShoppingActivity.class.getSimpleName();

    private ContentShoppingBinding mContentBinding;
    private ActivityShoppingBinding mBinding;
    private ItemImageAdapter mAdapter;
    private ArrayList<City> mCities;

    private User mLoggedUser;

    public static Intent getIntent(Context c, User loggedUser) {
        Intent i = new Intent(c, ShoppingActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shopping);
        mContentBinding = mBinding.contentShoppingContainer;

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        if (mLoggedUser == null) {
            Toast.makeText(this, "mLoggedUserNull", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(mBinding.toolbar);
        setUpLayout();
    }

    public void setUpLayout() {
        mContentBinding.recyclerViewItemImages.setHasFixedSize(true);
        mContentBinding.recyclerViewItemImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ItemImageAdapter();
        mContentBinding.recyclerViewItemImages.setAdapter(mAdapter);

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO validate input form
                if (validateForm()) {
                    onPreparingRequest();
                }
            }
        });

        setUpAutoCompleteCities();
    }

    private void setUpAutoCompleteCities() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        CityApi.loadCities(new CityApi.OnLoadCompleted() {
            @Override
            public void onLoadCompleted(ArrayList<City> cities) {
                mCities = cities;
                for (City city :
                        cities) {
                    adapter.add(city.getName());
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(ShoppingActivity.this, "load cities completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadCancelled(DatabaseError error) {

            }
        });
        mContentBinding.editTextDeliverTo.setAdapter(adapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ItemImageAdapter.UserClickAddImageButtonEvent event) {
        Toast.makeText(this, "on user click add image button", Toast.LENGTH_SHORT).show();
        int permission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != permission) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //TODO start image picker
            startImagePicker();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startImagePicker();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Snackbar.make(mBinding.getRoot(),
                            R.string.explain_read_storage_permission,
                            Snackbar.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void startImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getData().toString();
                    Log.i(TAG, "onActivityResult: user selected image path: " + imagePath);
                    mAdapter.addImage(imagePath);
                }
                break;
            }
        }
    }

    private boolean validateForm() {
        boolean result = true;
        ContentShoppingBinding binding = mContentBinding;
        result = validateUrl(binding.editTextItemUrl.getText().toString(), binding.inputItemUrl) && result;
        result = validateEmptyInput(binding.editTextItemDescription, binding.inputItemDescription) && result;
        result = validateEmptyInput(binding.editTextItemName, binding.inputItemName) && result;
        result = validateEmptyInput(binding.editTextItemPrice, binding.inputItemPrice) && result;
        result = validateEmptyInput(binding.editTextItemQuantity, binding.inputItemQuantity) && result;
        result = validateEmptyInput(binding.editTextReward, binding.inputItemReward) && result;
        result = validateItemImage() && result;
        result = validateCity(binding.editTextDeliverTo.getText().toString(), binding.inputItemDeliverTo) && result;
        return result;
    }

    private boolean validateItemImage() {
        if (mAdapter.getItemCount() <= 1) {
            Snackbar.make(mBinding.getRoot(), R.string.upload_picture_image_warning, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateUrl(String url, @Nullable TextInputLayout inputLayout) {
        boolean result;
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            if (inputLayout != null)
                inputLayout.setError(getString(R.string.invalid_item_url_warning));
            result = false;
        } else {
            result = true;
            if (inputLayout != null)
                inputLayout.setError(null);
        }
        return result;
    }

    private boolean validateEmptyInput(EditText text, @Nullable TextInputLayout inputLayout) {
        if (text.getText().toString().isEmpty()) {
            if (inputLayout != null)
                inputLayout.setError(getString(R.string.empty_input_warning));
            return false;
        }
        if (inputLayout != null)
            inputLayout.setError(null);
        return true;
    }

    private boolean validateCity(String text, @Nullable TextInputLayout inputLayout) {
        for (int i = 0; i < mCities.size(); i++) {
            if (text.trim().equalsIgnoreCase(mCities.get(i).getName())) {
                if (inputLayout != null)
                    inputLayout.setError(null);
                return true;
            }
        }
        if (inputLayout != null) {
            inputLayout.setError("Unsupported City!!");
        }
        return false;
    }

    private void onPreparingRequest() {
        //Todo sent request
        finish();
        ContentShoppingBinding binding = mContentBinding;
        final Request request = new Request();
        request.setFromUser(mLoggedUser);

        for (int i = 0; i < mCities.size(); ++i) {
            String selectedCity = binding.editTextDeliverTo.getText().toString();
            if (mCities.get(i).getName()
                    .equalsIgnoreCase(selectedCity)) {
                request.setDeliverTo(mCities.get(i));
            }
        }
        request.setQuantity(Integer.parseInt(binding.editTextItemQuantity.getText().toString()));
        request.setReward(Double.parseDouble(binding.editTextReward.getText().toString()));
        request.setTime(System.currentTimeMillis());
        request.setStatus(Request.STATUS_PENDING);

        final Item item = new Item();
        item.setName(binding.editTextItemName.getText().toString());
        item.setDescription(binding.editTextItemDescription.getText().toString());
        item.setItemUrl(binding.editTextItemUrl.getText().toString());
        item.setPrice(Double.parseDouble(binding.editTextItemPrice.getText().toString()));
        item.setFirstImage(mAdapter.getData().get(0));
        item.setImages(mAdapter.getData());

        new StorageApi.OnUploadingImage(item.getImages()) {
            @Override
            public void onCompleted(List<String> result) {
                item.setFirstImage(result.get(0));
                item.setImages((ArrayList<String>) result);
                request.setItem(item);
                RequestApi.createRequest(request, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(ShoppingActivity.this, "request Created!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }


}
