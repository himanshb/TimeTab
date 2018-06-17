package com.example.pc.timetab;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.notes.CreateNoteActivity;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<ListItem> listItems;
    private Context context;
    MyDatabase myDatabase;
    public MyAdapter(Context context,ArrayList<ListItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ListItem listItem=listItems.get(position);

        holder.cardLayout.setBackgroundColor(listItem.getColor());
        holder.imageButton.setBackgroundColor(listItem.getColor());
        holder.t1.setText(listItem.getSubject());
        holder.t2.setText(listItem.getTeacher());
        holder.t3.setText("Room :"+listItem.getRoom());
        holder.t4.setText(listItem.getFrom());

        //On click Three Dots Icon
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(context,v);
                MenuInflater inflater= popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popup_menu_cards,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();
                        if (id==R.id.edit){
                            Intent intent=new Intent(context,UpdateActivity.class);
                            intent.putExtra("day",listItem.getDay());
                            intent.putExtra("subject",listItem.getSubject());
                            intent.putExtra("from",listItem.getFrom());
                            intent.putExtra("to",listItem.getTo());
                            intent.putExtra("id",listItem.getId());
                            context.startActivity(intent);
                            return true;
                        }
                        if (id==R.id.delete){
                            removeItem(listItem);
                            return true;
                        }
                        if (id==R.id.add_note){
                            Intent intent=new Intent(context,CreateNoteActivity.class);
                            context.startActivity(intent);
                        }
                        if (id==R.id.reminder){
                            Intent intent=new Intent(context,ReminderActivity.class);
                            intent.putExtra("time",listItem.getFrom());
                            intent.putExtra("id",listItem.getId());
                            context.startActivity(intent);
                        }
                        return false;
                    }
                });
            }
        });
    }
    public void removeItem(ListItem infoListItem){
        myDatabase=new MyDatabase(context,"Time_Table_DB",null,1);
        String id=Integer.toString(infoListItem.getId());
        myDatabase.removeFromTable(new String[]{id});
        int pos=listItems.indexOf(infoListItem);
        listItems.remove(pos);
        notifyItemRemoved(pos);
        myDatabase.close();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4;
        ImageButton imageButton;
        ConstraintLayout cardLayout;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.textView1);
            t2=(TextView)itemView.findViewById(R.id.textView2);
            t3=(TextView)itemView.findViewById(R.id.textView3);
            t4=(TextView)itemView.findViewById(R.id.textView8);
            imageButton=(ImageButton)itemView.findViewById(R.id.imageButtonPopup);
            cardView=(CardView)itemView.findViewById(R.id.card_view);
            cardLayout=(ConstraintLayout)itemView.findViewById(R.id.card_layout_main);
        }

    }
}
