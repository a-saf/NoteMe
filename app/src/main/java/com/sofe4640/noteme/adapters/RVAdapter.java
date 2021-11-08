package com.sofe4640.noteme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofe4640.noteme.R;
import com.sofe4640.noteme.models.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteViewHolder>{

    private final ArrayList<Note> notes;
    private NoteViewHolder.OnNoteListener mOnNoteListener;

    public RVAdapter(ArrayList<Note> notes, NoteViewHolder.OnNoteListener onNoteListener) {
        this.notes = notes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.activity_rv_list_item,
                        parent,
                        false
                ), mOnNoteListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
       return position;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitle, noteSubtitle, noteDate;
        int noteColor;
        OnNoteListener onNoteListener;

        NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteSubtitle = itemView.findViewById(R.id.noteSubtitle);
            noteDate = itemView.findViewById(R.id.noteDate);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        void setNote(Note note) {
            noteTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                noteSubtitle.setVisibility(View.GONE);
            } else {
                noteSubtitle.setText(note.getSubtitle());
            }

            noteDate.setText(note.getDate());
            noteColor = Integer.parseInt(note.getNoteColor());
            LinearLayout backgroundLayout = itemView.findViewById(R.id.noteLayout);
            backgroundLayout.setBackgroundColor(noteColor);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        public interface OnNoteListener{
            void onNoteClick(int position);
        }
    }


}
