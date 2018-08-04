package tech.threekilogram.androidmodellib.beauty;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import tech.threekilogram.androidmodellib.GankCategoryBean.ResultsBean;
import tech.threekilogram.androidmodellib.R;
import tech.threekilogram.androidmodellib.beauty.BeautyRecyclerAdapter.ImageHolder;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-31
 * @time: 18:16
 */
public class BeautyRecyclerAdapter extends Adapter<ImageHolder> {

      private LayoutInflater mInflater;

      private List<ResultsBean> mBeans = new ArrayList<>();

      @NonNull
      @Override
      public ImageHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {

            if(mInflater == null) {
                  mInflater = LayoutInflater.from(parent.getContext());
            }

            View view = mInflater
                .inflate(R.layout.recycler_image_next_image_item, parent, false);

            return new ImageHolder(view);
      }

      @Override
      public void onBindViewHolder (
          @NonNull ImageHolder holder, int position) {

            ResultsBean bean = mBeans.get(position);
            holder.bind(position, bean);
      }

      @Override
      public int getItemCount () {

            return mBeans.size();
      }

      public static class ImageHolder extends ViewHolder {

            private ImageView mImage;

            public ImageHolder (View itemView) {

                  super(itemView);
                  initView(itemView);
            }

            private void initView (@NonNull final View itemView) {

                  mImage = itemView.findViewById(R.id.image);
            }

            void bind (int position, ResultsBean bean) {

            }
      }
}
