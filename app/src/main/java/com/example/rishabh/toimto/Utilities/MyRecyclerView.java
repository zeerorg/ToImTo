package com.example.rishabh.toimto.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            holder.image.setImageResource(item.getImage());
        }

        @Override
        public int getItemCount() {
            return ListData.text.length * 5;
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
                //TODO
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }

    }

    public static class ListData{

        public final static String[] text = {"Suicide Squad", "Jason Bourne", "Batman: The Killing Joke"};
        public final static int[] image = {R.drawable.test_poster,
                R.drawable.test_poster2,
                R.drawable.test_poster3};

        public static List<ListItem> getListData(){
            List<ListItem> data = new ArrayList<ListItem>();

            for(int x=0; x<5; x++){
                for(int y=0; y<text.length; y++){
                    data.add(new ListItem(text[y], image[y]));
                }
            }

            return data;

        }
    }

    public static class ListItem{

        private String text;
        private int image;

        public ListItem(String text, int image){
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

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String toString() {return getText()+" : "+getImage();}
    }
}
