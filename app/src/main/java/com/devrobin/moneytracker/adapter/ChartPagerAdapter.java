package com.devrobin.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.MainViewModel.ChartViewModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ChartPagerItemBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartPagerAdapter extends RecyclerView.Adapter<ChartPagerAdapter.ChartPagerViewHolder> {

    private final ChartViewModel chartViewModel;

    public ChartPagerAdapter(ChartViewModel chartViewModel) {
        this.chartViewModel = chartViewModel;
    }

    @NonNull
    @Override
    public ChartPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChartPagerItemBinding pagerItemBinding = ChartPagerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChartPagerViewHolder(pagerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartPagerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Hide all Chart first
        holder.pagerItemBinding.pieChart.setVisibility(View.GONE);
        holder.pagerItemBinding.barChart.setVisibility(View.GONE);
        holder.pagerItemBinding.lineChart.setVisibility(View.GONE);


        holder.pagerItemBinding.getRoot().post(new Runnable() {
            @Override
            public void run() {

                try{
                    //Ensure View has the dimension (Get Height & Width)
                    if (holder.pagerItemBinding.getRoot().getWidth() > 0 && holder.pagerItemBinding.getRoot().getHeight() > 0){
                        renderChart(holder.pagerItemBinding, position);
                    }
                    else {
                        holder.pagerItemBinding.getRoot().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                renderChart(holder.pagerItemBinding, position);
                            }
                        }, 300);
                    }
                } catch (Exception e) {
                    Log.e("ChartPagerAdapter", "Error in onBindViewHolder: " + e.getMessage());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class ChartPagerViewHolder extends RecyclerView.ViewHolder{

        ChartPagerItemBinding pagerItemBinding;

        public ChartPagerViewHolder(@NonNull ChartPagerItemBinding pagerItemBinding) {
            super(pagerItemBinding.getRoot());

            this.pagerItemBinding = pagerItemBinding;
        }
    }



    //render chart
    private void renderChart(ChartPagerItemBinding pagerItemBinding, int position) {

        try {
            switch (position){
                case 0:
                    pagerItemBinding.pieChart.setVisibility(View.VISIBLE);
                    renderPieChart(pagerItemBinding.pieChart);
                    break;
                case 1:
                    pagerItemBinding.barChart.setVisibility(View.VISIBLE);
                    renderBarChart(pagerItemBinding.barChart);
                    break;
                case 2:
                    pagerItemBinding.lineChart.setVisibility(View.VISIBLE);
                    renderLineChart(pagerItemBinding.lineChart);
                    break;
            }
        } catch (Exception e) {
            Log.e("ChartPagerAdapter", "Error renderChart onBindViewHolder" + e.getMessage());
        }

    }

    private void renderPieChart(PieChart pieChart) {

        try{
            List<PieEntry> pieEntries = new ArrayList<>();
            String dataType = chartViewModel.getDataType().getValue();
            List<CategoryChartData> categoryData = chartViewModel.getCategoryChartData().getValue();

            if (categoryData != null && !categoryData.isEmpty()){

                for (CategoryChartData item : categoryData){

                    float value = 0;

                    if ("Income".equals(dataType)){
                        value = (float) item.getIncome();
                    }
                    else if ("Expense".equals(dataType)){
                        value = (float) item.getExpense();
                    }

                    if (value > 0){
                        pieEntries.add(new PieEntry(value, item.getCategory()));
                    }
                }

            }

            if (!pieEntries.isEmpty()){

                PieDataSet dataSet = new PieDataSet(pieEntries, dataType + "Distribution");

                if ("Income".equals(dataType)){

                    dataSet.setColors(Color.parseColor("#4CAF50"), Color.parseColor("#8BC34A"),
                            Color.parseColor("#CDDC39"), Color.parseColor("#FFEB3B"),
                            Color.parseColor("#FF9800"));

                }
                else {
                    dataSet.setColors(Color.parseColor("#F44336"), Color.parseColor("#E91E63"),
                            Color.parseColor("#9C27B0"), Color.parseColor("#673AB7"),
                            Color.parseColor("#3F51B5"));
                }



                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format("$%.0f", value);
                    }
                });


                PieData pieData = new PieData(dataSet);
                pieChart.setData(pieData);
                pieChart.setCenterText(dataType + "Distribution");
                pieChart.setCenterTextSize(16f);
                pieChart.setCenterTextColor(Color.BLACK);
                pieChart.setHoleRadius(35f);
                pieChart.setTransparentCircleRadius(40f);
                pieChart.setDescription(null);

                Legend legend = pieChart.getLegend();
                legend.setEnabled(true);
                legend.setTextColor(Color.BLACK);
                legend.setTextSize(12f);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                pieChart.invalidate();
            }
            else {

                //Show "No Records" for empty data
                pieChart.setCenterText("No Records");
                pieChart.setCenterTextSize(18f);
                pieChart.setCenterTextColor(Color.RED);
                pieChart.setData(null);
                pieChart.getLegend().setEnabled(false);
                pieChart.invalidate();

            }
        } catch (Exception e) {
            Log.e("ChartPagerAdapter", "Error Rendering pie chart: " + e.getMessage());

            try {
                pieChart.setCenterText("No Records");
                pieChart.setData(null);
                pieChart.invalidate();
            } catch (Exception fallbackException) {
                Log.e("ChartPagerAdapter: ", "Error creating Fall bak pie chart" + fallbackException.getMessage());
            }
        }

    }


    //Bar Chart
    private void renderBarChart(BarChart barChart) {

        try{

            List<BarEntry> barEntries = new ArrayList<>();
            String dataType = chartViewModel.getDataType().getValue();
            List<utils.MonthlyChartData> monthData = chartViewModel.getMonthlyChartData().getValue();


            if (monthData != null && !monthData.isEmpty()){

                for (int i = 0; i < monthData.size(); i++){

                    utils.MonthlyChartData mData = monthData.get(i);
                    float value = 0;

                    if ("Income".equals(dataType)){
                        value = (float) mData.getIncome();
                    } else if ("Expense".equals(dataType)) {

                        value = (float) mData.getExpense();

                    }

                    if (value >0){
                        barEntries.add(new BarEntry(i, value));
                    }

                }

            }

            if (!barEntries.isEmpty()){

                BarDataSet dataSet = new BarDataSet(barEntries, dataType);

                if ("Income".equals(dataType)){
                    dataSet.setColors(Color.parseColor("#4CAF50"));
                }
                else {
                    dataSet.setColors(Color.parseColor("#F44336"));
                }

                dataSet.setValueTextSize(15f);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format("$%.0f", value);
                    }
                });

                BarData barData = new BarData(dataSet);
                barChart.setData(barData);
                barChart.setDescription(null);



                //Configure X-axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setTextSize(12f);
                xAxis.setDrawGridLines(false);


                //Set custom labels for months
                if (monthData != null && !monthData.isEmpty()){
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index >= 0 && index < monthData.size()){
                                return monthData.get(index).getMonth();
                            }

                            return "";
                        }
                    });
                }

                //Configure Y-axis
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setTextColor(Color.BLACK);
                leftAxis.setTextSize(10f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGridColor(Color.parseColor("#333333"));

                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setEnabled(false);

                //Configure Legend
                Legend legend  = barChart.getLegend();
                legend.setTextColor(Color.BLACK);
                legend.setTextSize(12f);

                barChart.invalidate();
            }
            else {
                //Show "No Records" for empty datta
                barChart.setData(null);
                barChart.setNoDataText("No Records");
                barChart.setNoDataTextColor(Color.BLACK);

                barChart.invalidate();
            }

        } catch (Exception e) {
            Log.e("ChartPagerAdapter", "Error rendering bar chart" + e.getMessage());

            try{
                barChart.setData(null);
                barChart.setNoDataText("No Records");
                barChart.setNoDataTextColor(Color.BLACK);

                barChart.invalidate();
            } catch (Exception fallbackException) {
                Log.e("ChartPagerAdapter", "Error creating fallback chart: " + fallbackException.getMessage());
            }
        }


    }


    private void renderLineChart(LineChart lineChart) {


        try {
            List<Entry> entries = new ArrayList<>();
            String dataType = chartViewModel.getDataType().getValue();
            List<utils.MonthlyChartData> monthData = chartViewModel.getMonthlyChartData().getValue();

            if (monthData != null && !monthData.isEmpty()) {
                for (int i = 0; i < monthData.size(); i++) {
                    utils.MonthlyChartData m = monthData.get(i);
                    float value = 0;
                    if ("Income".equals(dataType)) {
                        value = (float) m.getIncome();
                    } else if ("Expense".equals(dataType)) {
                        value = (float) m.getExpense();
                    }

                    if (value > 0) {
                        entries.add(new Entry(i, value));
                    }
                }
            }

            if (!entries.isEmpty()) {
                LineDataSet dataSet = new LineDataSet(entries, dataType);

                if ("Income".equals(dataType)) {
                    dataSet.setColor(Color.parseColor("#4CAF50"));
                } else {
                    dataSet.setColor(Color.parseColor("#F44336"));
                }

                dataSet.setLineWidth(3f);
                dataSet.setCircleRadius(5f);
                dataSet.setCircleColor(dataSet.getColor());
                dataSet.setCircleHoleColor(Color.BLUE);
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format("$%.0f", value);
                    }
                });
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(dataSet.getColor());
                dataSet.setFillAlpha(50);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.setDescription(null);

                // Configure X-axis
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setTextSize(10f);
                xAxis.setDrawGridLines(false);

                // Set custom labels for months
                if (monthData != null && !monthData.isEmpty()) {
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index >= 0 && index < monthData.size()) {
                                return monthData.get(index).getMonth();
                            }
                            return "";
                        }
                    });
                }

                // Configure Y-axis
                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(Color.BLACK);
                leftAxis.setTextSize(10f);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGridColor(Color.parseColor("#333333"));

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false);

                // Configure legend
                Legend legend = lineChart.getLegend();
                legend.setTextColor(Color.BLACK);
                legend.setTextSize(12f);

                lineChart.invalidate();
            } else {
                // Show "No Records" for empty data
                lineChart.setData(null);
                lineChart.setNoDataText("No Records");
                lineChart.setNoDataTextColor(Color.BLACK);
                lineChart.invalidate();
            }
        } catch (Exception e) {
            Log.e("ChartPagerAdapter", "Error rendering line chart: " + e.getMessage());
            try {
                lineChart.setData(null);
                lineChart.setNoDataText("No Records");
                lineChart.setNoDataTextColor(Color.BLACK);
                lineChart.invalidate();
            } catch (Exception fallbackException) {
                Log.e("ChartPagerAdapter", "Error creating fallback line chart: " + fallbackException.getMessage());
            }
        }
    }
}
