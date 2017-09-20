package com.wael.alameen.santaland;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private Context context;
    private List<Menu> menuList;
    private AdapterView.OnItemClickListener onClickListener;

    ItemsAdapter(Context context, List<Menu> menuList, AdapterView.OnItemClickListener onClickListener) {
        LayoutInflater.from(context);
        this.context = context;
        this.menuList = menuList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_menu_layout, parent, false);
            return new ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemName.setText(menuList.get(position).getName());
        Picasso.with(context).load(menuList.get(position).getImage()).resize(400, 300).onlyScaleDown().into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView itemImage;
        TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "comic.ttf");
            itemName.setTypeface(typeface);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(null, v, getLayoutPosition(), getItemId());
        }
    }
}
