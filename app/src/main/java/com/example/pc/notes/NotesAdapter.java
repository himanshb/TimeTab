package com.example.pc.notes;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.pc.timetab.R;
import com.example.pc.timetab.UpdateActivity;

import java.io.File;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private List<NoteBuilder> noteBuilders;
    private NotesDatabase notesDatabase;

    public NotesAdapter(Context context, List<NoteBuilder> noteBuilders) {
        this.context = context;
        this.noteBuilders = noteBuilders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_layout_notes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NoteBuilder noteBuilder=noteBuilders.get(position);
        holder.cardLayout.setBackgroundColor(noteBuilder.getColor());
        holder.imageButton.setBackgroundColor(noteBuilder.getColor());
        holder.textView1.setText(noteBuilder.getTitle());
        holder.textView2.setText(noteBuilder.getContent());
        holder.textView3.setText(noteBuilder.getFileDate());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(context,v);
                MenuInflater inflater= popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();
                        if (id==R.id.one){
                            Intent intent=new Intent(context, UpdateNoteActivity.class);
                            intent.putExtra("title",noteBuilder.getTitle());
                            intent.putExtra("date",noteBuilder.getFileDate());
                            intent.putExtra("timeStamp",noteBuilder.getFileName());
                            intent.putExtra("content",noteBuilder.getContent());
                            context.startActivity(intent);
                            return true;
                        }
                        if (id==R.id.two){
                            removeItem(noteBuilder);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }
    public void removeItem(NoteBuilder infoNoteBuilder){
        //delete entry from DB
        notesDatabase=new NotesDatabase(context,"NOTES_DB",null,1);
        notesDatabase.deleteNote(infoNoteBuilder.getFileName());
        notesDatabase.close();

        //delete the file
        File file=context.getFileStreamPath(infoNoteBuilder.getFileName()+".txt");
        boolean check=file.delete();
        if (check){
            Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        //delete the card
        int pos=noteBuilders.indexOf(infoNoteBuilder);
        noteBuilders.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        return noteBuilders.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2,textView3;
        ImageButton imageButton;
        ConstraintLayout cardLayout;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
            imageButton=(ImageButton)itemView.findViewById(R.id.imageButtonDelete);
            textView1=(TextView)itemView.findViewById(R.id.textViewTitle);
            textView2=(TextView)itemView.findViewById(R.id.textViewContent);
            textView3=(TextView)itemView.findViewById(R.id.textViewDate);
            cardLayout=(ConstraintLayout)itemView.findViewById(R.id.cardLayout);
        }
    }
}
