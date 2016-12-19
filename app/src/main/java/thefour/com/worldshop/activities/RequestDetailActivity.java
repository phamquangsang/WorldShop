package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.NetworkUtil;
import thefour.com.worldshop.R;
import thefour.com.worldshop.api.OfferApi;
import thefour.com.worldshop.databinding.ActivityRequestDetailBinding;
import thefour.com.worldshop.fragments.OfferListFragment;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class RequestDetailActivity extends AppCompatActivity
        implements View.OnClickListener, OfferListFragment.OnListOfferInteractListener{
    private static final String ARG_LOGGED_USER = "arg_logged_user";
    private static final String ARG_REQUEST = "arg_request";
    private static final String TAG = RequestDetailActivity.class.getSimpleName();
    private static final String ARG_REQUEST_ID = "arg-request-id";

    private User mLoggedUser;
    private String mRequestId;
    private Request mRequest;
    private ActivityRequestDetailBinding mBinding;
    private OfferListFragment mFragment;
    private ChildEventListener mEventOffersListener;
    private ValueEventListener mRequestListener;


    //TODO|| to see request in detail, All item images, All offer (pending, accepted),
    //TODO|| User who make offer could edit their offer,
    //TODO|| User who make request can accept other user's offer,

    //TODO|| Action Make offer button.
    //TODO|| Action Chat.
    //TODO|| Action accept offer.

    //TODO|| UI-UX example https://grabr.io/en/grabs/52007


//    public static Intent getIntent(Context c, Request request, User loggedUser){
//        Intent i = new Intent(c, RequestDetailActivity.class);
//        i.putExtra(ARG_REQUEST, request);
//        i.putExtra(ARG_LOGGED_USER, loggedUser);
//        return i;
//    }

    public static Intent getIntent(Context c, String requestId, User loggedUser){
        Intent i = new Intent(c, RequestDetailActivity.class);
        i.putExtra(ARG_REQUEST_ID, requestId);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mRequestId = getIntent().getStringExtra(ARG_REQUEST_ID);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupView();
        loadRequest();
//        Toast.makeText(this, "request:status = "+mRequestId.getStatus(), Toast.LENGTH_SHORT).show();
    }

    private void setupView() {
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mFragment = OfferListFragment.newInstance(1, mLoggedUser, mRequestId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();

    }

    private void loadRequest(){
        mRequestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRequest = new Request();
                mRequest.setRequest(dataSnapshot.getValue(Request.class));
                mBinding.content.setRequest(mRequest);
                if(isLoggedUser(mRequest.getFromUser())){
                    mBinding.content.btnMakeOffer.setVisibility(View.GONE);
                    mBinding.content.btnMessage.setVisibility(View.GONE);
                }else{
                    mBinding.content.btnMessage.setOnClickListener(RequestDetailActivity.this);
                    mBinding.content.btnMakeOffer.setOnClickListener(RequestDetailActivity.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Toast.makeText(this, ""+mRequestId, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference()
                .child(Contracts.REQUESTS_LOCATION).child(mRequestId)
                .addValueEventListener(mRequestListener);
    }

    private boolean isLoggedUser(User user){
        return mLoggedUser.getUserId().equals(user.getUserId());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(mRequest!=null){
            Toast.makeText(this, R.string.no_internet_warning, Toast.LENGTH_SHORT).show();
            return;
        }
        if(id == mBinding.content.btnMakeOffer.getId()){
            startActivity(MakeOfferActivity.getIntent(this, mLoggedUser, mRequest, null));
        }else if(id == mBinding.content.btnMessage.getId()){
            ChatActivity.getIntent(this, mLoggedUser, mRequest.getFromUser());
        }
    }

    @Override
    public void onItemClick(Offer item) {
        //do nothing
    }

    @Override
    public void onAcceptClick(Offer item) {
        if(!NetworkUtil.isNetworkAvailable(this)){
            Snackbar.make(mBinding.getRoot(),R.string.no_internet_warning, Snackbar.LENGTH_LONG).show();
            return;
        }
        OfferApi.acceptOffer(mRequest, item, mLoggedUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.i(TAG, "onComplete: accept offer succeed");

            }
        });
    }

    @Override
    public void onUpdateClick(Offer item) {
        Intent intent = MakeOfferActivity.getIntent(this, mLoggedUser, mRequest, item);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if(mEventOffersListener !=null)
            FirebaseDatabase.getInstance().getReference()
                .child(Contracts.REQUEST_OFFERS_LOCATION)
                .child(mRequestId)
                .removeEventListener(mEventOffersListener);
        if(mRequestListener!=null){
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.REQUESTS_LOCATION).child(mRequestId)
                    .removeEventListener(mRequestListener);
        }
        super.onDestroy();
    }

    @Override
    public void onDeleteClick(Offer item) {
        if(!NetworkUtil.isNetworkAvailable(this)){
            Snackbar.make(mBinding.getRoot(),R.string.no_internet_warning, Snackbar.LENGTH_LONG).show();
            return;
        }
        OfferApi.deleteOffer(mRequest, item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.i(TAG, "onComplete: delete offer succeed");
            }
        });
    }

    @Override
    public void onCancelOffer(Offer item) {
        if(!NetworkUtil.isNetworkAvailable(this)){
            Snackbar.make(mBinding.getRoot(),R.string.no_internet_warning, Snackbar.LENGTH_LONG).show();
            return;
        }
        OfferApi.undoAccepOffer(mRequest, item, mLoggedUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.i(TAG, "onComplete: cancel offer completed!");
            }
        });
    }

    @Override
    public void onCompleteOffer(Offer item) {
        if(!NetworkUtil.isNetworkAvailable(this)){
            Snackbar.make(mBinding.getRoot(),R.string.no_internet_warning, Snackbar.LENGTH_LONG).show();
            return;
        }
        OfferApi.offerCompleted(mRequest, item, mLoggedUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(RequestDetailActivity.this, "Request Succeed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFragmentCreated() {
        mEventOffersListener = OfferApi.loadRequestOffers(mRequestId, mFragment);
    }
}
