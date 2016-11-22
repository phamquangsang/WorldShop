package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.HorizontalItemImageAdapter;
import thefour.com.worldshop.databinding.ActivityShoppingBinding;
import thefour.com.worldshop.databinding.ContentShoppingBinding;

public class ShoppingActivity extends AppCompatActivity {
    private ContentShoppingBinding mContentBinding;
    private ActivityShoppingBinding mBinding;

    public static Intent getIntent(Context c) {
        Intent i = new Intent(c, ShoppingActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shopping);
        mContentBinding = mBinding.contentShoppingContainer;

        setSupportActionBar(mBinding.toolbar);
        setUpLayout();

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setUpLayout(){
        mContentBinding.recyclerViewItemImages.setHasFixedSize(true);
        mContentBinding.recyclerViewItemImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mContentBinding.recyclerViewItemImages.setAdapter(new HorizontalItemImageAdapter());
    }
}
