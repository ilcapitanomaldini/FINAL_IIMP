package com.liveproject.ycce.iimp.polling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.liveproject.ycce.iimp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 02-02-2017.
 */
public class Activity_PollResults extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("Poll Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        String[] result = getIntent().getStringArrayExtra("Result");
        float[] rvalue = getIntent().getFloatArrayExtra("ResultValue");
        //ArrayList<PollMapping> pollMappingArray = getIntent().getSerializableExtra("PollMapping");
        String Title = getIntent().getStringExtra("Title");
        pieChart = (PieChart) findViewById(R.id.pie_poll);

        List<PieEntry> entries = new ArrayList<>();


        //  entries.add(new PieEntry(18.5f, "Green"));
        //    entries.add(new PieEntry(26.7f, "Yellow"));
        //      entries.add(new PieEntry(24.0f, "Red"));
//        entries.add(new PieEntry(30.8f, "Blue"));
        int i = 0;
        for (String element : result) {
            entries.add(new PieEntry(rvalue[i], element));
            i++;
        }


        PieDataSet set = new PieDataSet(entries, Title);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueTextSize(16);
        PieData data = new PieData(set);
        pieChart.setData(data);


        pieChart.setCenterText(Title);
        pieChart.setCenterTextSize(25);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHighlightPerTapEnabled(true);


        pieChart.getDescription().setEnabled(false);


        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setTextSize(16);
        // pieChart.invalidate(); // refresh

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
