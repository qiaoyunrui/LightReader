package com.qiao.androidlab.lightreader.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiao.androidlab.lightreader.Parts.MyAdapter;
import com.qiao.androidlab.lightreader.Parts.SerializableLightPic;
import com.qiao.androidlab.lightreader.R;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * RecycleAdapterEx
 * <p/>
 * RecycleView 的 适配器【新】
 *
 * @author: 乔云瑞
 * @time: 2016/3/12 18:02
 */
public class RecycleAdapterEx extends RecyclerView.Adapter<RecycleAdapterEx.ViewHolderEx> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SerializableLightPic> datas;
    private OnItemClickListener mOnItemClickListener;
    private Bitmap mBitmap;

    public RecycleAdapterEx(Context context, List<SerializableLightPic> datas) {
        this.mContext = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setDatas(List<SerializableLightPic> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolderEx onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_ex, parent, false);
        ViewHolderEx viewHolderEx = new ViewHolderEx(view);
        return viewHolderEx;
    }

    @Override
    public void onBindViewHolder(final ViewHolderEx holder, final int position) {
        holder.title.setText(datas.get(position).getTitle());
        holder.author.setText(datas.get(position).getAuthor());
        holder.time.setText(datas.get(position).getTime());
//        mBitmap = BitmapFactory.decodeFile(datas.get(position).getPath());
        /*
        图片加载框架
         */
        Glide.with(mContext)
                .load(datas.get(position).getPath())
                        //.placeholder(R.mipmap.test_img)   //设置占位图
                .crossFade()
                .into(holder.show);
        WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(mBitmap);
        /*if (mBitmap == null) {

        } else {
            holder.show.setImageBitmap(mBitmap);
        }*/
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.OnItemClick(holder.itemView, position);
                }
            });
        }
    }


    public void AddData(int position, SerializableLightPic lightPic) {
        datas.add(position, lightPic);
        notifyItemInserted(position);
    }

    public void deleteDate(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public SerializableLightPic getSerializableLightPic(int position) {
        return datas.get(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    class ViewHolderEx extends RecyclerView.ViewHolder {

        ImageView show;
        TextView title;
        TextView author;
        TextView time;
        CardView card;

        public ViewHolderEx(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_ex);
            show = (ImageView) itemView.findViewById(R.id.card_ex_image);
            author = (TextView) itemView.findViewById(R.id.card_ex_author);
            time = (TextView) itemView.findViewById(R.id.card_ex_time);
            title = (TextView) itemView.findViewById(R.id.card_ex_title);
        }
    }

}


