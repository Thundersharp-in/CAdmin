package com.thundersharp.cadmin.core.globalAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.thundersharp.cadmin.R;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {

    Context context;
    List<String> pdfuri;

    public PdfAdapter(Context context, List<String> pdfuri) {
        this.context = context;
        this.pdfuri = pdfuri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_pdf,parent,false); //item_org_images
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = pdfuri.get(position);

    }

    @Override
    public int getItemCount() {
        if (pdfuri != null){
            return pdfuri.size();
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pdfView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           pdfView = itemView.findViewById(R.id.pdf_viewer);
        }
    }
}
