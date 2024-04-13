//package com.example.appgrit.adapters;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;
//
//import com.bumptech.glide.Glide;
//import com.example.appgrit.models.ImagePostModel;
//
//import java.util.List;
//
//public class ImagePagerAdapter extends PagerAdapter {
//    private Context context;
//    private List<ImagePostModel> imageList;
//
//    public ImagePagerAdapter(Context context, List<ImagePostModel> imageList) {
//        this.context = context;
//        this.imageList = imageList;
//    }
//
//    @Override
//    public int getCount() {
//        return imageList.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        ImageView imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        Glide.with(context)
//                .load(imageList.get(position).getImagePath())
//                .into(imageView);
//
//        container.addView(imageView);
//
//        return imageView;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((ImageView) object);
//    }
//}
