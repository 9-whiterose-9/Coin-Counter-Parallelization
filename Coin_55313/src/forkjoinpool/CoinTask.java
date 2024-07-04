package forkjoinpool;

import java.util.concurrent.RecursiveTask;

import coin.Coin;

public class CoinTask extends RecursiveTask<Integer> {
	private static final long serialVersionUID = 1L;
	public static final int LIMIT = 999;
	private int depth;
	
	private int[] coins;
    private int index;
    private int accumulator;

    public CoinTask(int[] coins, int index, int accumulator, int depth) {
        this.coins = coins;
        this.index = index;
        this.accumulator = accumulator;
        this.depth = depth;
    }
	
	private static int seq(int[] coins, int index, int accumulator) {
		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) return -1;
		
		int a = seq(coins, index+1, accumulator);
		int b = seq(coins, index+1, accumulator + coins[index]);
		return Math.max(a,  b);
	}
	
    @Override
    protected Integer compute() {
        if (index >= coins.length) {
            if (accumulator < Coin.LIMIT) {
                return accumulator;
            }
            return -1;
        }
        if (accumulator + coins[index] > Coin.LIMIT) {
            return -1;
        }
        
		// Max level of recursion:
		if (depth >= 15) return seq(coins, index,accumulator);
		
        // if the current queue has more than 2 tasks than the average
        //if (RecursiveTask.getSurplusQueuedTaskCount() > 2) return seq(coins, index, accumulator);
        
		
        CoinTask includeNextCoin = new CoinTask(coins, index + 1, accumulator, depth +1);
        CoinTask excludeNextCoin = new CoinTask(coins, index + 1, accumulator + coins[index], depth +1);

        includeNextCoin.fork();
        int b = excludeNextCoin.compute();
        int a = includeNextCoin.join();

        return Math.max(a, b);
    }
}
