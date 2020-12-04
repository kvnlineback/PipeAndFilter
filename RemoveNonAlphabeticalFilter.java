import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RemoveNonAlphabeticalFilter implements Runnable, IFilter {

	private String charsToBeIncluded;
	private BlockingQueue<String> inputPipe;
	private BlockingQueue<String> outputPipe;
//	private long startTime;
//	private long endTime;
//	private long durationInNano = 0L;

	public RemoveNonAlphabeticalFilter(String charsToBeIncluded, BlockingQueue<String> inputPipe,
			BlockingQueue<String> outputPipe) {
		this.charsToBeIncluded = charsToBeIncluded;
		this.inputPipe = inputPipe;
		this.outputPipe = outputPipe;
	}

	@Override
	public void run() {
		read();
//		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
//		System.out.println("RemoveNonAlphabeticalFilter response time = " + durationInMillis + " milliseconds");
	}

	@Override
	public void read() {
		try {
			while (true) {
				String str = inputPipe.take();
//				startTime = System.nanoTime();
				if (str.equals("stopFiltering")) {
					write("stopFiltering");
					return;
				}
				String newStr = str.replaceAll(charsToBeIncluded, "");
				write(newStr);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void write(String str) {
		try {
			outputPipe.put(str);
//			endTime = System.nanoTime();
//			durationInNano += (endTime - startTime);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
