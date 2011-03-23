package gtna.graph.sorting;

import java.util.Random;

public class Randomizing {
	public static void randomizeOrder(int[] values, Random rand) {
		randomizeOrder(values, rand, 0, values.length - 1);
	}

	public static void randomizeOrder(int[] values, Random rand, int from,
			int to) {
		for (int i = to - from; i > 1; i--) {
			int index = rand.nextInt(i) + from;
			int temp = values[index];
			values[index] = values[i + from];
			values[i + from] = temp;
		}
	}
}
