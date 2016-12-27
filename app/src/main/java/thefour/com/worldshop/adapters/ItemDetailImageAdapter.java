package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.DetailItemImageBinding;
import thefour.com.worldshop.databinding.ItemImageBinding;
import thefour.com.worldshop.models.Item;

/**
 * Created by Quang Quang on 11/22/2016.
 */

public class ItemDetailImageAdapter extends
        RecyclerView.Adapter<ItemDetailImageAdapter.ImageHolder> {
    private static final String TAG = ItemDetailImageAdapter.class.getSimpleName();


    private ArrayList<String> mDataset;

    public ItemDetailImageAdapter(Item item) {
        mDataset = item.getImages();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.mBinding.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri imagePath = Uri.parse(mDataset.get(position));
        Glide.with(holder.mBinding.image.getContext())
                .load(imagePath).into(holder.mBinding.image);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public ArrayList<String> getData() {
        return mDataset;
    }


    class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DetailItemImageBinding mBinding;

        public ImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item_imageButton) {
                //to-do Do nothing
            }
        }
    }
}
