package com.thundersharp.cadmin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.core.calculators.ConvertingUnits;

import org.w3c.dom.Text;

public class WeightConverter extends AppCompatActivity {

    private TextView e1,e2;
    private Spinner s1,s2;
    private int count1=0;
    private ConvertingUnits.Weight ca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_converter);


        e1=findViewById(R.id.item1);
        e2=findViewById(R.id.item2);
        s1=(Spinner)findViewById(R.id.spinner1);
        s2=(Spinner)findViewById(R.id.spinner2);

        ca=new ConvertingUnits.Weight();
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
                    temp=ca.MilliToKilo(value);
                    break;
                case 1:
                    temp=ca.CentiToKilo(value);
                    break;
                case 2:
                    temp=ca.DeciToKilo(value);
                    break;
                case 3:
                    temp=ca.GramToKilo(value);
                    break;
                case 4:
                    temp=value;
                    break;
                case 5:
                    temp=ca.MetricTonnesToKilo(value);
                    break;
                case 6:
                    temp=ca.PoundsToKilo(value);
                    break;
                case 7:
                    temp=ca.OuncesToKilo(value);
                    break;
            }

            switch (item2)
            {
                case 0:
                    temp=ca.KiloToMilli(temp);
                    break;
                case 1:
                    temp=ca.KiloToCenti(temp);
                    break;
                case 2:
                    temp=ca.KiloToDeci(temp);
                    break;
                case 3:
                    temp=ca.KiloToGram(temp);
                    break;
                case 5:
                    temp=ca.KiloToMetricTonnes(temp);
                    break;
                case 6:
                    temp=ca.KiloToPounds(temp);
                    break;
                case 7:
                    temp=ca.KiloToOunces(temp);
                    break;
            }
            return temp;
        }
    }
}
