package tests;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

public class Boxplot {
    public static void createCustomDataset(
            List<Float> forkJoinTimes10Cores, List<Float> forkJoinPoolTimes10Cores, List<Float> completableFutureTimes10Cores,
            List<Float> forkJoinTimes14Cores, List<Float> forkJoinPoolTimes14Cores, List<Float> completableFutureTimes14Cores,
            List<Float> forkJoinTimes18Cores, List<Float> forkJoinPoolTimes18Cores, List<Float> completableFutureTimes18Cores,
            List<Float> forkJoinTimes20Cores, List<Float> forkJoinPoolTimes20Cores, List<Float> completableFutureTimes20Cores
    ) {
        final String[] seriesCount = {"ForkJoin", "ForkJoinPool", "CompletableFuture"};
        final int[] categoryCount = {10, 14, 18, 20};

        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        List<List<List<Float>>> allExecutionTimes = new ArrayList<>();
        allExecutionTimes.add(List.of(forkJoinTimes10Cores, forkJoinPoolTimes10Cores, completableFutureTimes10Cores));
        allExecutionTimes.add(List.of(forkJoinTimes14Cores, forkJoinPoolTimes14Cores, completableFutureTimes14Cores));
        allExecutionTimes.add(List.of(forkJoinTimes18Cores, forkJoinPoolTimes18Cores, completableFutureTimes18Cores));
        allExecutionTimes.add(List.of(forkJoinTimes20Cores, forkJoinPoolTimes20Cores, completableFutureTimes20Cores));

        for (int i = 0; i < seriesCount.length; i++) {
            for (int j = 0; j < categoryCount.length; j++) {
                final List<Float> list = new ArrayList<>();
                for (int k = 0; k < allExecutionTimes.size(); k++) {
                	System.out.println("Adding "+ allExecutionTimes.get(k).get(i)+ "to list:");
                    list.addAll(allExecutionTimes.get(k).get(i));
                    for (Float e : list) {
						System.out.println(e);
					}
                }
                System.out.println("List that is added to boxplot:");
                for (Float e : list) System.out.println(e);
				
                dataset.add(list, seriesCount[i], categoryCount[j] + " Cores");
                System.out.println(seriesCount[i] + " and " + categoryCount[j] + " Cores");
            }
        }

        JFreeChart boxplot = ChartFactory.createBoxAndWhiskerChart(
                "Execution Time Comparison",
                "Core Count",
                "Execution Time (seconds)",
                dataset,
                true
        );

        CategoryPlot plot = boxplot.getCategoryPlot();
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(boxplot);
        chartPanel.setPreferredSize(new Dimension(1500, 1300));

        JFrame frame = new JFrame("Boxplot Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

}
