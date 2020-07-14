package com.thundersharp.cadmin.ui.fragment;

import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thundersharp.cadmin.MainActivity;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.notes.adapters.NotesAdapter;
import com.thundersharp.cadmin.notes.callbacks.MainActionModeCallback;
import com.thundersharp.cadmin.notes.callbacks.NoteEventListener;
import com.thundersharp.cadmin.notes.db.NotesDB;
import com.thundersharp.cadmin.notes.db.NotesDao;
import com.thundersharp.cadmin.notes.model.Note;
import com.thundersharp.cadmin.notes.utils.NoteUtils;
import com.thundersharp.cadmin.ui.EditNoteActivity;

import java.util.ArrayList;
import java.util.List;

import static com.thundersharp.cadmin.MainActivity.floatingActionButton;
import static com.thundersharp.cadmin.ui.EditNoteActivity.NOTE_EXTRA_Key;

public class HomeFragment extends Fragment  {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_outline_24,getActivity().getTheme()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Chat comming soon",Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }


}