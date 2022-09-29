package com.tencent.custom;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/10/22 17:03
 * Description: This is PhotoAlbumItemRecyclerAdapter
 */
public class PhotoAlbumItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int defItem = -1;
    public OnItemClickListener onItemListener;
    List<PhotoAlbumItemEntity> dataBeanList = new ArrayList<>();

    public PhotoAlbumItemRecyclerAdapter(List<PhotoAlbumItemEntity> list) {
        dataBeanList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_album, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        /**
         * 自动向上转型，所以子类的对象holder，能赋给父类RecyclerView.ViewHolder的引用
         * 父类创建的对象的内存肯定是 <= 子类创建的对象的内存，因为子类对象=父类对象+子类扩充的属性和方法
         * 当子类对象自动向上转型赋给父类引用时，子类扩充的属性和方法是被屏蔽的，因为父类的引用没有指向这些
         * 属性的指针，但是当子类重写了父类的方法（只是方法）时，这个父类的引用可以调用这个被重写的方法，
         * 因为他们有了如下指向：父类的引用-->父类的方法-->父类被重写的方法
         * 这就是Java的----多态
         */
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (dataBeanList != null && dataBeanList.size() > 0) {
            Glide.with(TUIChatService.getAppContext()).load(ConfigUrl.getFullImageUrl(dataBeanList.get(position).getSrc()))
                    .error(R.drawable.photo_album_rcv_item_def_img)
                    .apply(bitmapTransform(new MvBlurTransformation(25)))
                    .placeholder(R.drawable.photo_album_rcv_item_def_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.user_img);
        }
    }

    @Override//onCreateViewHolder(ViewGroup parent, int viewType)中的viewType参数由此获得
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        if (dataBeanList == null) {
            return 0;
        }
        return dataBeanList.size();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int pos, PhotoAlbumItemEntity itemEntity);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView user_img;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            user_img = itemView.findViewById(R.id.user_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v, getLayoutPosition(), dataBeanList.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
}
