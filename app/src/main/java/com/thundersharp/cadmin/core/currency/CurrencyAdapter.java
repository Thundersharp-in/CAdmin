package com.thundersharp.cadmin.core.currency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.currency.utils.Globals;
import com.thundersharp.cadmin.core.currency.utils.Prefs;
import com.thundersharp.cadmin.ui.activity.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;


public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    List<String> countryCode = new ArrayList<>();
    List<String> countriesCurrencies = new ArrayList<>();

    List<String> copycountryCode = new ArrayList<>();
     List<String> copycountriesCurrencies = new ArrayList<>();

    Context context;


    public static final String TAG = "CurrencyAdapter";


    public CurrencyAdapter(Context context,List<String> countryCode, List<String> countriesCurrencies) {
        this.context=context;
        this.countriesCurrencies = countriesCurrencies;
        this.countryCode = countryCode;
        this.copycountriesCurrencies = countriesCurrencies;
        this.copycountryCode = countryCode;
    }


    public void filter(String s){
        copycountriesCurrencies=new ArrayList<>();
        copycountryCode=new ArrayList<>();
        if(s!=null && s.length()>1){
            for(int i=0;i<countriesCurrencies.size();i++){
                if(countriesCurrencies.get(i).toLowerCase().contains(s.toLowerCase())){
                    copycountryCode.add(countryCode.get(i));
                    copycountriesCurrencies.add(countriesCurrencies.get(i));
                }
            }

        }else {
            copycountriesCurrencies=countriesCurrencies;
            copycountryCode=countryCode;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(this.copycountryCode.get(position)!=null && this.copycountriesCurrencies.get(position)!=null) {
                if (Globals.getCountryFlag(this.copycountryCode.get(position)) != null) {
                    if (Globals.getCountryFlag(this.copycountryCode.get(position)).length() > 1) {
                        holder.flag.setText(Globals.getCountryFlag(this.copycountryCode.get(position)));
                        holder.currency.setText(this.copycountriesCurrencies.get(position));
                    }else {
                        holder.flag.setVisibility(View.GONE);
                        holder.currency.setVisibility(View.GONE);
                    }
                }
            }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p=0;
                if(CurrencyConverter.flag==1){
                    if(holder.currency.getText().toString().toLowerCase().equals(CurrencyConverter.countryTo.getText().toString().toLowerCase())){
                        p=1;
                    }else {
                        CurrencyConverter.countryFrom.setText(holder.currency.getText().toString());
                    }
                }else if(CurrencyConverter.flag==2){
                    if(holder.currency.getText().toString().toLowerCase().equals(CurrencyConverter.countryFrom.getText().toString().toLowerCase())){
                        p=1;
                    }else {
                        CurrencyConverter.countryTo.setText(holder.currency.getText().toString());
                    }
                }
                if(p==0){
                    CurrencyConverter.behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    CurrencyConverter.behavior.setPeekHeight(0);
                    CurrencyConverter.currency_to.setText("");
                    CurrencyConverter.currency_from.setText("");
                    CurrencyConverter.search.setText("");
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.linear.getWindowToken(), 0);
                    Prefs.setPrefs("country_from",CurrencyConverter.countryFrom.getText().toString(),context);
                    Prefs.setPrefs("country_to",CurrencyConverter.countryTo.getText().toString(),context);
                }else {
                    Toast.makeText(context,"Select same currencies on both sides",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return copycountryCode.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView flag, currency;
        public LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (TextView) itemView.findViewById(R.id.flag);
            currency = (TextView) itemView.findViewById(R.id.currency);
            linear=(LinearLayout)itemView.findViewById(R.id.currencylinear);
        }

    }


}
