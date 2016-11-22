package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.ItemImageBinding;

/**
 * Created by Quang Quang on 11/22/2016.
 */

public class HorizontalItemImageAdapter extends
        RecyclerView.Adapter<HorizontalItemImageAdapter.ImageHolder>{

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        private ItemImageBinding mBinding;
        public ImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.findBinding(itemView);
        }
    }
}
