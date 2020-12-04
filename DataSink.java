import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DataSink implements Runnable {

	private BlockingQueue<String> inputPipe;
	private BlockingQueue<String> outputPipe;


	public DataSink(BlockingQueue<String> inputPipe) {
		this.inputPipe = inputPipe;
	}

	@Override
	public void run() {
		read();
		long endTime = System.nanoTime();
//		long durationInNano = (endTime - DataSource.startTime);
//		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
//		System.out.println("application response time for 2nd version = " + durationInMillis + " milliseconds");
	}

	public void read() {
		List<String> list = new ArrayList<String>();
		try {
			while (true) {
				String str = inputPipe.take();
				if (str.equals("stopFiltering")) {
					processTopTen(list);
					return;
				}
				list.add(str);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void processTopTen(List<String> data) {
		data.removeIf(s -> s.equals(""));
		Map<String, Long> map = data.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
		List<Map.Entry<String, Long>> result = map.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toList());
		final HashMap<String, Long> map2 = new HashMap<>();
		for (Map.Entry<String, Long> item : result) {
			map2.put(item.getKey(), item.getValue());
		}
		System.out.println("*******Top 10 terms*******");
		map2.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
				.forEach(s -> print(s));
	}

	private static void print(Map.Entry<String, Long> entry) {
		System.out.println('"' + entry.getKey() + '"' + " ocurrs " + entry.getValue() + " times");
	}
}
