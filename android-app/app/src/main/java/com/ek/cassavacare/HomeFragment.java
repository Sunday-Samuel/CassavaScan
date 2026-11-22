/*
 * Project: CassavaScan
 * File: AboutSubFragment.java
 * Description: Displays application information, model details, and credits
 *
 * Based on the open-source CassavaCare project
 * Original author: Emmanuel Kirui Barkacha (https://github.com/ekbarkacha)
 * License: MIT
 */

package com.ek.cassavacare;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.ek.cassavacare.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private AppDatabase db;
    private ExecutorService executorService;
    private TextView tvRecentScanResult, tvRecentScanDate, tvStatsTotal, tvStatsCommonDisease;
    private BarChart chartScanStats;

    private static final String[] FARMING_TIPS = {
            "Plant cuttings only from healthy plants that show no leaf symptoms.",
            "Remove and destroy plants with mosaic or brown streak symptoms early.",
            "Clean cutting tools with alcohol or fire between plants.",
            "Space plants properly so air moves through the field and leaves dry quickly.",
            "Weed regularly, since weeds can host whiteflies and viruses.",
            "Walk your field weekly and check the underside of leaves for whiteflies.",
            "Avoid planting cassava next to infected fields.",
            "Rotate cassava with legumes such as cowpea or groundnut to break pest cycles.",
            "Add compost or well-rotted manure to improve soil health.",
            "Mulch around plants to keep the soil moist and reduce weeds.",
            "Water early in the day when you can.",
            "Avoid waterlogged soil. Plant on ridges with good drainage.",
            "Never take cuttings from plants that look sick.",
            "Keep simple farm records of planting dates and any disease you see.",
            "Ask an extension officer before using any pesticide, and follow the label exactly.",
            "Wear gloves and wash your hands after handling diseased plants.",
            "Burn or bury infected plant material instead of leaving it in the field.",
            "Plant at the start of the rainy season, when the soil has good moisture.",
            "Harvest mature roots on time, since delayed harvest increases losses from rot.",
            "Store harvested roots in a cool, dry place and use them quickly.",
            "Use resistant cassava varieties recommended for your area.",
            "Check cuttings for pests and damage before you plant them.",
            "Use organic fertilizer such as well-rotted manure to build soil health.",
            "Too much nitrogen fertilizer can encourage leafy growth instead of root growth.",
            "Keep children and animals away from fields after spraying.",
            "Store farm chemicals out of reach of children, in their original containers.",
            "Use a mask and gloves when spraying, and never spray in strong wind.",
            "Keep a few healthy plants as a reference to compare against sick ones.",
            "Inspect your fields after heavy rain, since wet weather can spread disease.",
            "Choose clean, certified planting material whenever it is available."
            };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvRecentScanResult = root.findViewById(R.id.tv_recent_scan_result);
        tvRecentScanDate = root.findViewById(R.id.tv_recent_scan_date);
        tvStatsTotal = root.findViewById(R.id.tv_stats_total);
        tvStatsCommonDisease = root.findViewById(R.id.tv_stats_common_disease);
        chartScanStats = root.findViewById(R.id.chart_scan_stats);

        // Initialize database and executor
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "database-name").build();
        executorService = Executors.newSingleThreadExecutor();

        // Setup buttons
        Button btnScanNow = root.findViewById(R.id.btn_quick_scan);
        Button btnViewHistory = root.findViewById(R.id.btn_view_history);

        btnScanNow.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_scan);
        });

        btnViewHistory.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_to_history);
        });

        // Load recent scan and statistics
        loadRecentScan();
        loadScanStatistics();

        // Basic chart setup
        chartScanStats.getDescription().setEnabled(false);
        chartScanStats.setFitBars(true);
        chartScanStats.getLegend().setEnabled(true);

        // Set random farming tip
        TextView tvTipContent = root.findViewById(R.id.tv_tip_content);
        tvTipContent.setText(FARMING_TIPS[new Random().nextInt(FARMING_TIPS.length)]);

        return root;
    }

    private void loadRecentScan() {
        executorService.execute(() -> {
            List<ScanResult> allScans = db.scanResultDao().getAll();
            requireActivity().runOnUiThread(() -> {
                if (allScans != null && !allScans.isEmpty()) {
                    ScanResult recentScan = allScans.get(0); // First item due to DESC order
                    tvRecentScanResult.setText(recentScan.result);
                    tvRecentScanDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", recentScan.timestamp));
                } else {
                    tvRecentScanResult.setText("No recent scans");
                    tvRecentScanDate.setText("");
                }
            });
        });
    }

    private void loadScanStatistics() {
        executorService.execute(() -> {
            try {
                // Fetch all scan results (ordered by timestamp DESC)
                List<ScanResult> scanResults = db.scanResultDao().getAll();
                if (scanResults.isEmpty()){
                    requireActivity().runOnUiThread(() -> {
                        tvStatsTotal.setText("Total Scans: 0");
                        tvStatsCommonDisease.setText("Most Common Disease: None");
                        chartScanStats.clear();
                        chartScanStats.setVisibility(View.GONE);
                    });
                }else {
                    Map<String, Integer> diseaseCounts = new HashMap<>();
                    String mostCommonDisease = "None";
                    int maxCount = 0;
                    int totalScans = 0;

                    for (ScanResult result : scanResults) {
                        String disease = result.result.split(" - ")[0]; // Extract disease name
                        diseaseCounts.put(disease, diseaseCounts.getOrDefault(disease, 0) + 1);
                        totalScans++;
                        if (diseaseCounts.get(disease) > maxCount) {
                            maxCount = diseaseCounts.get(disease);
                            mostCommonDisease = disease;
                        }
                    }

                    // Prepare chart data with shortened labels and fixed green shades
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();
                    List<Integer> colors = new ArrayList<>();

                    int index = 0;

                    // Fixed shades of green for 5 classes
                    int[] greenShades = {
//                            Color.parseColor("#004D40"), // very dark green
//                            Color.parseColor("#1B5E20"), // dark green
//                            Color.parseColor("#4CAF50"), // medium green
//                            Color.parseColor("#8BC34A"), // bright lime green
//                            Color.parseColor("#A5D6A7")  // pastel mint green
                            Color.parseColor("#1B5E20"),
                            Color.parseColor("#2E7D32"),
                            Color.parseColor("#43A047"),
                            Color.parseColor("#66BB6A"),
                            Color.parseColor("#A5D6A7")
                    };

                    for (Map.Entry<String, Integer> entry : diseaseCounts.entrySet()) {
                        entries.add(new BarEntry(index, entry.getValue()));
                        String shortLabel = shortenDiseaseName(entry.getKey());
                        labels.add(shortLabel);
                        colors.add(greenShades[index % greenShades.length]); // Assign unique green per disease
                        index++;
                    }

                    int textColor = ContextCompat.getColor(requireContext(), R.color.text_primary);
                    BarDataSet dataSet = new BarDataSet(entries, "Scan Count");
                    dataSet.setColors(colors); // Apply custom green shades
                    dataSet.setValueTextColor(textColor);
                    dataSet.setValueTextSize(12f);

                    BarData barData = new BarData(dataSet);
                    barData.setBarWidth(0.9f);

                    int finalTotalScans = totalScans;
                    String finalMostCommonDisease = mostCommonDisease;
                    requireActivity().runOnUiThread(() -> {
                        tvStatsTotal.setText("Total Scans: " + finalTotalScans);
                        tvStatsCommonDisease.setText("Most Common Disease: " + finalMostCommonDisease);
                        chartScanStats.setData(barData);
                        chartScanStats.setFitBars(true);

                        chartScanStats.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                        chartScanStats.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        chartScanStats.getXAxis().setGranularity(1f); // One label per bar
                        chartScanStats.getXAxis().setLabelCount(labels.size()); // Match label count
                        chartScanStats.getAxisLeft().setAxisMinimum(0f);

                        // Optional: Rotate labels if overlapping
                        if (labels.size() > 4) {
                            chartScanStats.getXAxis().setLabelRotationAngle(-45f);
                        }

                        chartScanStats.getAxisRight().setEnabled(false);

                        // Get X-axis and Y-axis
                        XAxis xAxis = chartScanStats.getXAxis();
                        YAxis leftAxis = chartScanStats.getAxisLeft();
                        YAxis rightAxis = chartScanStats.getAxisRight();

                        xAxis.setTextColor(textColor);
                        leftAxis.setTextColor(textColor);

                        // Disable grid lines
                        xAxis.setDrawGridLines(false);
                        leftAxis.setDrawGridLines(true);
                        rightAxis.setDrawGridLines(false);

                        // Axis lines
                        xAxis.setDrawAxisLine(true);
                        leftAxis.setDrawAxisLine(true);
                        rightAxis.setDrawAxisLine(false);

                        // Legend setup
                        Legend legend = chartScanStats.getLegend();
                        legend.setEnabled(true);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setTextColor(textColor);
                        legend.setDrawInside(false);
                        legend.setWordWrapEnabled(true);
                        legend.setTextSize(10f);
                        legend.setXEntrySpace(10f);
                        legend.setYEntrySpace(5f);

                        // Custom legend entries matching labels and colors
                        List<LegendEntry> legendEntries = new ArrayList<>();
                        for (int i = 0; i < labels.size(); i++) {
                            LegendEntry legendEntry = new LegendEntry();
                            legendEntry.label = labels.get(i);
                            legendEntry.formColor = colors.get(i);
                            legendEntry.form = Legend.LegendForm.SQUARE;
                            legendEntries.add(legendEntry);
                        }
                        legend.setCustom(legendEntries);

                        chartScanStats.setExtraBottomOffset(20f);

                    chartScanStats.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        // re-apply to compute with final width
                                        Legend lg = chartScanStats.getLegend();
                                        lg.setWordWrapEnabled(true);
                                        lg.setCustom(legendEntries);
                                        chartScanStats.notifyDataSetChanged();
                                        chartScanStats.invalidate();

                                        // remove listener
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            chartScanStats.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        } else {
                                            chartScanStats.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                        }
                                    }
                                }
                        );

                        chartScanStats.invalidate();
                        chartScanStats.setVisibility(View.VISIBLE);

                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to load scan statistics: " + e.getMessage());
                requireActivity().runOnUiThread(() -> {
                    tvStatsTotal.setText("Total Scans: 0");
                    tvStatsCommonDisease.setText("Most Common Disease: None");
                    chartScanStats.clear();
                    chartScanStats.setVisibility(View.GONE);
                });
            }
        });
    }

    // Method to shorten disease names
    private String shortenDiseaseName(String fullName) {
        // Predefined abbreviations for common cassava diseases
        Map<String, String> diseaseAbbreviations = new HashMap<>();
        diseaseAbbreviations.put("Cassava Mosaic Disease", "CMD");
        diseaseAbbreviations.put("Cassava Brown Streak Disease", "CBSD");
        diseaseAbbreviations.put("Cassava Bacterial Blight", "CBB");
        diseaseAbbreviations.put("Cassava Green Mite", "CGM");
        diseaseAbbreviations.put("Healthy", "Healthy");


        // Check if the disease has a predefined abbreviation
        String shortName = diseaseAbbreviations.get(fullName);
        if (shortName != null) {
            return shortName;
        }

        // Fallback: Take first letter of each word
        String[] words = fullName.split(" ");
        StringBuilder abbreviated = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                abbreviated.append(word.charAt(0));
            }
        }
        return abbreviated.length() > 0 ? abbreviated.toString() : fullName.substring(0, Math.min(3, fullName.length()));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) executorService.shutdown();
        if (db != null) db.close();
    }
}
