package com.example.notuygulamam.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notuygulamam.Models.Upload;
import com.example.notuygulamam.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;

    public ImageAdapter(Context context,List<Upload>uploads){
        mContext=context;
        mUploads=uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewKonu.setText(uploadCurrent.getKonu());

        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewKonu;
        public RatingBar RatingBar;
        public Button oyVer;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName=itemView.findViewById(R.id.text_view_name);
            imageView=itemView.findViewById(R.id.image_view_upload);
            textViewKonu=itemView.findViewById(R.id.text_view_konu);
            RatingBar=itemView.findViewById(R.id.ratingbar);
            oyVer=itemView.findViewById(R.id.oyVer);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            oyVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float GelenOylama=RatingBar.getRating();
                    Log.i("Gelen Oylama",String.valueOf(GelenOylama));
                }
            });

        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Fotoğraf Silinsin mi?");
            MenuItem delete=menu.add(Menu.NONE,1,1,"Fotoğrafı Sil");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onDeleteClick(position);
                            Log.i("deleteimg","");
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);


    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}

