package com.qiao.androidlab.lightreader.Parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiao.androidlab.lightreader.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<LightPic> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public MyAdapter(Context context, List<LightPic> datas) {
        this.mContext = context;
        this.mDatas = datas;
//        WeakReference<List<LightPic>> weakReference = new WeakReference<>(datas);
        mInflater = LayoutInflater.from(context);
    }

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setmDatas(List<LightPic> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!isNull(mDatas.get(position).getTitle())) {
            holder.title.setText(mDatas.get(position).getTitle());
        }
        if (!isNull(mDatas.get(position).getAuthor())) {
            holder.author.setText(mDatas.get(position).getAuthor());
        }
        if (!isNull(mDatas.get(position).getTime())) {
            holder.time.setText(mDatas.get(position).getTime());
        }
        if (!isNull(mDatas.get(position).getDetail())) {
            holder.detail.setText(mDatas.get(position).getDetail());
        }
        if (!isNull(mDatas.get(position).getBm())) {
            /*
            进行图片压缩
             */
            /*ByteArrayOutputStream out = new ByteArrayOutputStream();
            mDatas.get(position).getBm().compress(Bitmap.CompressFormat.JPEG, 100, out);
            BitmapFactory.Options options = new BitmapFactory.Options();
            int be = 2;
            options.inSampleSize = be;
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, null);
            WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(bitmap);
            holder.show.setImageBitmap(bitmap);*/
            holder.show.setImageBitmap(mDatas.get(position).getBm());
            Glide.with(mContext)
                    .load(mDatas.get(position).getPath())
                    .crossFade()
                    .into(holder.show);
        }

        holder.title.setText(mDatas.get(position).getTitle());
        holder.author.setText(mDatas.get(position).getAuthor());
        holder.time.setText(mDatas.get(position).getTime());
        holder.detail.setText(mDatas.get(position).getDetail());

        Palette.generateAsync(mDatas.get(position).getBm(), new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    holder.card.setCardBackgroundColor(swatch.getRgb());
                    holder.title.setTextColor(swatch.getTitleTextColor());
                    holder.author.setTextColor(swatch.getBodyTextColor());
                    holder.time.setTextColor(swatch.getBodyTextColor());
                    holder.detail.setTextColor(swatch.getBodyTextColor());
                }
            }
        });

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.OnItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.OnItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void AddData(int position, LightPic lightPic) {
        mDatas.add(position, lightPic);
        notifyItemInserted(position);
    }

    public void deleteDate(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public LightPic getLightPic(int position) {
        return mDatas.get(position);
    }

    boolean isNull(Object o) {
        if (o == null) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView author;
    TextView time;
    TextView detail;
    ImageView show;
    android.support.v7.widget.CardView card;

    public MyViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        author = (TextView) itemView.findViewById(R.id.author);
        time = (TextView) itemView.findViewById(R.id.time);
        detail = (TextView) itemView.findViewById(R.id.detial);
        show = (ImageView) itemView.findViewById(R.id.show);
        card = (CardView) itemView.findViewById(R.id.card);
    }
}
