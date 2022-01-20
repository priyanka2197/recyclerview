package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.model.ResponseDataItem;
import com.example.testapp.model.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.MyViewHolder> {
    Context mContext;
    List<ResponseDataItem> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public UserRecyclerViewAdapter(Context applicationContext, List<ResponseDataItem> userdatalist) {
        this.mContext = applicationContext;
        this.dataList = userdatalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    public void filterList(List<ResponseDataItem> filterllist) {

        dataList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ResponseDataItem userData = dataList.get(position);
        holder.name.setText(userData.getName());
        holder.title.setText(userData.getUsername());
        Glide.with(mContext).load(userData.getAvatar()).into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(int imageData);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, title;
        ImageView imageView;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            linearLayout = itemView.findViewById(R.id.linear);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageview);


        }
    }
}
