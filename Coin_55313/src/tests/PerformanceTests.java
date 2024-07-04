package tests;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class PerformanceTests {

	private static DefaultCategoryDataset createDataset(
			List<Float> sequentialTimes,
			List<Float> forkJoinTimes,
			List<Float> forkJoinPoolTimes,
			List<Float> completableFutureTimes) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < sequentialTimes.size(); i++) {
			dataset.addValue(sequentialTimes.get(i), "Sequential", "Iteration " + (i + 1));
			dataset.addValue(forkJoinTimes.get(i), "ForkJoin", "Iteration " + (i + 1));
			dataset.addValue(forkJoinPoolTimes.get(i), "ForkJoinPool", "Iteration " + (i + 1));
			dataset.addValue(completableFutureTimes.get(i), "CompletableFuture", "Iteration " + (i + 1));
		}

		return dataset;
	}


	public static void displayChart(
			List<Float> sequentialTimes,
			List<Float> forkJoinTimes,
			List<Float> forkJoinPoolTimes,
			List<Float> completableFutureTimes
			) {
		DefaultCategoryDataset dataset = createDataset(sequentialTimes, forkJoinTimes,forkJoinPoolTimes, completableFutureTimes);

		JFreeChart lineChartObject = ChartFactory.createLineChart(
				"Execution Times Evolution", "Iteration", "Execution Time (s)",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		int width = 800;
		int height = 600;
		File chartDirectory = new File("src/plots");
		chartDirectory.mkdirs(); // Creating the "plots" directory if it doesn't exist

		File lineChart = new File(chartDirectory, "exec_times_evolution_20c_15depth_delivery.jpeg");

		try {
			ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
