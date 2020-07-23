package com.thundersharp.cadmin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.calculators.ConvertingUnits;

public class LengthConverter extends AppCompatActivity {

    private EditText e1,e2;
    private Spinner s1,s2;
    private int count1=0;
    private ConvertingUnits.Length ca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_converter);

        e1=(EditText)findViewById(R.id.item1);
        e2=(EditText)findViewById(R.id.item2);
        s1=(Spinner)findViewById(R.id.spinner1);
        s2=(Spinner)findViewById(R.id.spinner2);

        ca=new ConvertingUnits.Length();
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.num0:
                e1.setText(e1.getText()+"0");
                break;

            case R.id.num1:
                e1.setText(e1.getText()+"1");
                break;

            case R.id.num2:
                e1.setText(e1.getText()+"2");
                break;

            case R.id.num3:
                e1.setText(e1.getText()+"3");
                break;

            case R.id.num4:
                e1.setText(e1.getText()+"4");
                break;

            case R.id.num5:
                e1.setText(e1.getText()+"5");
                break;

            case R.id.num6:
                e1.setText(e1.getText()+"6");
                break;

            case R.id.num7:
                e1.setText(e1.getText()+"7");
                break;

            case R.id.num8:
                e1.setText(e1.getText()+"8");
                break;

            case R.id.num9:
                e1.setText(e1.getText()+"9");
                break;

            case R.id.dot:
                if (count1==0)
                {
                    e1.setText(e1.getText()+".");
                    count1++;
                }
                break;

            case R.id.clear:
                e1.setText("");
                e2.setText("");
                count1=0;
                break;

            case R.id.backSpace:
                if(e1.length()!=0)
                {
                    String text=e1.getText().toString();
                    if(text.endsWith("."))
                        count1=0;
                    String newText=text.substring(0,text.length()-1);
                    e1.setText(newText);
                }
                break;

            case R.id.equal:
                int item1=s1.getSelectedItemPosition();
                int item2=s2.getSelectedItemPosition();
                double value1=Double.parseDouble(e1.getText().toString());
                double result=evaluate(item1,item2,value1);
                e2.setText(result+"");
                break;
        }
    }

    public double evaluate(int item1,int item2,double value)
    {
        double temp=0.0;
        if(item1==item2)
            return value;
        else
        {
            switch (item1)
            {
                case 0:
                    temp=ca.NanoToMeter(value);
                    break;
                case 1:
                    temp=ca.MilliToMeter(value);
                    break;
                case 2:
                    temp=ca.CentiToMeter(value);
                    break;
                case 3:
                    temp=value;
                    break;
                case 4:
                    temp=ca.KiloToMeter(value);
                    break;
                case 5:
                    temp=ca.InchToMeter(value);
                    break;
                case 6:
                    temp=ca.FootToMeter(value);
                    break;
                case 7:
                    temp=ca.YardToMeter(value);
                    break;
                case 8:
                    temp=ca.MileToMeter(value);
                    break;
            }

            switch (item2)
            {
                case 0:
                    temp=ca.MeterToNano(temp);
                    break;
                case 1:
                    temp=ca.MeterToMilli(temp);
                    break;
                case 2:
                    temp=ca.MeterToCenti(temp);
                    break;
                case 4:
                    temp=ca.MeterToKilo(temp);
                    break;
                case 5:
                    temp=ca.MeterToInch(temp);
                    break;
                case 6:
                    temp=ca.MeterToFoot(temp);
                    break;
                case 7:
                    temp=ca.MeterToYard(temp);
                    break;
                case 8:
                    temp=ca.MeterToMile(temp);
                    break;
            }
            return temp;
        }
    }
}
