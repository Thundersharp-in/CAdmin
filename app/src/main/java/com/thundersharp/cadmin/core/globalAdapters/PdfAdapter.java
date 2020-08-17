package com.thundersharp.cadmin.core.globalAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.PdfLoader;

import java.io.File;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView pdfView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           pdfView = itemView.findViewById(R.id.pdf_viewer);
           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, PdfLoader.class).putExtra("url",pdfuri.get(getAdapterPosition())));
           /*
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view1 = LayoutInflater.from(context).inflate(R.layout.full_pdf_viewer,null);
            WebView webView = view1.findViewById(R.id.pdf_files);
            final ProgressBar progressBar = view1.findViewById(R.id.progresspdf);
            progressBar.setVisibility(View.VISIBLE);

            File file=new File(pdfuri.get(getAdapterPosition()));
            Intent target=new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent=Intent.createChooser(target,"Open File");
            context.startActivity(intent);

            builder.setCancelable(true);
            builder.setView(view1);
            builder.show();

            */
        }
    }
}
