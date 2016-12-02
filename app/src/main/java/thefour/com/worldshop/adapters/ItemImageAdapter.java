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
import thefour.com.worldshop.databinding.ItemImageBinding;

/**
 * Created by Quang Quang on 11/22/2016.
 */

public class ItemImageAdapter extends
        RecyclerView.Adapter<ItemImageAdapter.ImageHolder> {
    private static final String TAG = ItemImageAdapter.class.getSimpleName();
    private final int TYPE_IMAGE = 1;
    private final int TYPE_ADD_IMAGE = 2;

    private ArrayList<String> mDataset;

    public ItemImageAdapter() {
        mDataset = new ArrayList<>();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ADD_IMAGE:
                holder.mBinding.itemImageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case TYPE_IMAGE:
                holder.mBinding.itemImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Uri imagePath = Uri.parse(mDataset.get(position));
                Glide.with(holder.mBinding.itemImageButton.getContext())
                        .load(imagePath).into(holder.mBinding.itemImageButton);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mDataset.size() ? TYPE_ADD_IMAGE : TYPE_IMAGE;
    }

    public void addImage(String fileUrl) {
        mDataset.add(fileUrl);
        notifyItemInserted(mDataset.size() - 1);
    }

    public ArrayList<String> getData() {
        return mDataset;
    }

    public static class UserClickAddImageButtonEvent {
    }

    class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemImageBinding mBinding;

        public ImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.itemImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item_imageButton) {
                if (getAdapterPosition() == mDataset.size()) {
                    EventBus.getDefault().post(new UserClickAddImageButtonEvent());
                }
            }
        }
    }
}
