package coin;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import forkjoinpool.CoinTask;
import tests.Boxplot;
import tests.PerformanceTests;
import tests.StatisticalTests;

public class Coin {
	public static final int LIMIT = 999;
	static int nCores = 20;// which is Runtime.getRuntime().availableProcessors() ;

	public static int[] createRandomCoinSet(int N) {
		int[] r = new int[N];
		for (int i = 0; i < N ; i++) {
			if (i % 10 == 0) {
				r[i] = 400;
			} else {
				r[i] = 4;
			}
		}
		return r;
	}

	public static int seq(int[] coins, int index, int accumulator) {
		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) {
			return -1;
		}
		int a = seq(coins, index+1, accumulator);
		int b = seq(coins, index+1, accumulator + coins[index]);
		return Math.max(a,  b);
	}

	public static int parallelSeqWithCompletableFuture(int[] coins, int numThreads) throws ExecutionException, InterruptedException {
		ForkJoinPool pool = new ForkJoinPool(numThreads);
		CoinTask coinTask = new CoinTask(coins, 0, 0,0);

		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> pool.invoke(coinTask));
		return future.get();
	}    


	public static void main(String[] args) throws Exception {
		int[] coins = createRandomCoinSet(30);
		int repeats = 30;
		List<Float> sequentialTimes = new ArrayList<>();
		List<Float> forkJoinTimes = new ArrayList<>();
		List<Float> forkJoinPoolTimes = new ArrayList<>();
		List<Float> completableFutureTimes = new ArrayList<>();
		System.out.println("Using " + nCores + " Cores:");
			for (int i = 0; i < repeats; i++) {
				/*  .....  Computing sequential Version  .....  */
				
				System.out.println("REPEAT Nº " + i);
				long seqInitialTime = System.currentTimeMillis();
				int rs = seq(coins, 0, 0);
				long seqEndTime = System.currentTimeMillis();
				float seqExecTimeInSec = (seqEndTime - seqInitialTime) /1000F;
				System.out.println(nCores + ";Sequential  ;" + seqExecTimeInSec + ";" + rs);
				sequentialTimes.add(seqExecTimeInSec);

				/*  .....  Computing ForkJoin Version   .....  */
				long fjInitialTime = System.currentTimeMillis();
				CoinTask coinTask = new CoinTask(coins, 0, 0, 0);
				coinTask.fork();
				int rfj = coinTask.join();
				long fjEndTime = System.currentTimeMillis();
				float fjExecInSec = (fjEndTime - fjInitialTime) /1000F;
				System.out.println(nCores + ";ForkJoin    ;" + fjExecInSec + ";" + rfj);
				forkJoinTimes.add(fjExecInSec);

				if (rfj != rs) {
					System.out.println("Wrong Result!");
					System.exit(-1);
				}

				/*  .....  Computing ForkJoin with ForkJoinPool management Version   .....  */
				ForkJoinPool pool = new ForkJoinPool(nCores);
				long fjpInitialTime = System.currentTimeMillis();
				CoinTask coinT = new CoinTask(coins, 0, 0, 0);

				int rfjp = pool.invoke(coinT);
				long fjpEndTime = System.currentTimeMillis();
				float fjpExecInSec = (fjpEndTime - fjpInitialTime) /1000F;
				System.out.println(nCores + ";ForkJoinPool;" + fjpExecInSec + ";" + rfjp);
				forkJoinPoolTimes.add(fjpExecInSec);

				if (rfjp != rs) {
					System.out.println("Wrong Result!");
					System.exit(-1);
				}

				/*  .....  Computing ForkJoinPool With CompletableFuture Version   .....  */
				long cfInitialTime = System.currentTimeMillis();;
				int rcf = parallelSeqWithCompletableFuture(coins, nCores);
				long cfEndTime = System.currentTimeMillis();
				Float cfExecInSec = (cfEndTime - cfInitialTime) /1000F;
				System.out.println(nCores + ";Compl.Future;" + cfExecInSec + ";" + rcf);
				completableFutureTimes.add(cfExecInSec);

				if (rcf != rs) {
					System.out.println("Wrong Result!");
					System.exit(-1);
				}
			}
		
		/* .............................BENCHMARKING................................. */

		// Passing the lists to the PerformanceTests class for chart generation and statistical tests
		PerformanceTests.displayChart(sequentialTimes, forkJoinTimes,forkJoinPoolTimes, completableFutureTimes);
		StatisticalTests.testApproaches(forkJoinTimes,completableFutureTimes);
        //Boxplot.createCustomDataset(allExecutionTimes);
        

        
	}
}