package com.example.rishabh.toimto.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rishabh.toimto.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishabh on 12/9/16.
 */
public class MyRecyclerView {
    public static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{

        private LayoutInflater inflater;
        private List<ListItem> list;

        public void setItemClickCallback(ItemClickCallback itemClickCallback) {
            this.itemClickCallback = itemClickCallback;
        }

        private ItemClickCallback itemClickCallback;

        public void setListData(List<ListItem> listData) {
            this.list = listData;
        }

        public interface ItemClickCallback{
            void onItemClick(int p);
        }

        public ListAdapter(List<ListItem> list, Context context){
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_item, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position){
            //Log.e("item", item.getText()+" : "+item.getImage()+" : "+position);
            ListItem item = list.get(position);
            holder.text.setText(item.getText());
            holder.image.setImageResource(R.drawable.test_poster);
            Glide.with(inflater.getContext())
                    .load(item.getImage())
                    .into(holder.image);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView text;
            private ImageView image;
            private View container;

            public ListHolder(View itemView) {
                super(itemView);

                text = (TextView) itemView.findViewById(R.id.card_text);
                image = (ImageView) itemView.findViewById(R.id.card_image);

                container = itemView.findViewById(R.id.card_root);
                container.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }

    }

    public static class ListData{

        public static String[] text = {"Suicide Squad", "Jason Bourne", "Batman: The Killing Joke"};
        public static String[] image = {"R.drawable.test_poster",
                "R.drawable.test_poster2",
                "R.drawable.test_poster3"};

        public static List<ListItem> getListData(){
            List<ListItem> data = new ArrayList<ListItem>();

            for(int y=0; y<text.length; y++){
                data.add(new ListItem(text[y], image[y]));
            }

            return data;

        }

        public static void setListData(String[] text, String[] image){
            ListData.text = text;
            ListData.image = image;
        }
    }

    public static class ListItem{

        private String text;
        private String image;

        public ListItem(String text, String image){
            setText(text);
            setImage(image);
        }

        public ListItem(){}

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String toString() {return getText()+" : "+getImage();}
    }
}
