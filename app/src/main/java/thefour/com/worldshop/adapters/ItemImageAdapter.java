package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.ItemImageBinding;

/**
 * Created by Quang Quang on 11/22/2016.
 */

public class ItemImageAdapter extends
        RecyclerView.Adapter<ItemImageAdapter.ImageHolder>{
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
        switch (getItemViewType(position)){
            case TYPE_ADD_IMAGE:

                break;
            case TYPE_IMAGE:
                Glide.with(holder.mBinding.itemImageButton.getContext())
                        .load(R.drawable.example_image_item).into(holder.mBinding.itemImageButton);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mDataset.size()?TYPE_ADD_IMAGE:TYPE_IMAGE;
    }

    class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemImageBinding mBinding;
        public ImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.itemImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.item_imageButton){
                if(getAdapterPosition() == mDataset.size()){
                    EventBus.getDefault().post(new UserClickAddImageButtonEvent());
                }
            }
        }
    }

    public void addImage(String fileUrl){
        mDataset.add(fileUrl);
        notifyItemInserted(mDataset.size()-1);
    }

    public static class UserClickAddImageButtonEvent{   }
}
