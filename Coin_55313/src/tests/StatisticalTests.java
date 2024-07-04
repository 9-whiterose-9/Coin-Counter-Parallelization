package tests;


import java.util.List;


import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

public class StatisticalTests {
	public static void testApproaches(List<Float> forkJoinTimes, List<Float> completableFutureTimes) {
	    double[] forkJoinArray = forkJoinTimes.stream().mapToDouble(Float::doubleValue).toArray();

	    double[] completableFutureArray = completableFutureTimes.stream().mapToDouble(Float::doubleValue).toArray();

	    // Mann-Whitney U Test
	    MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
	    double pValueMWU = mannWhitneyUTest.mannWhitneyU(forkJoinArray, completableFutureArray);
	    System.out.println("Mann-Whitney U Test p-value: " + pValueMWU);
	}

}

