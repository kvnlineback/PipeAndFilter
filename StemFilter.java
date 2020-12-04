import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class StemFilter implements IFilter, Runnable {
	
	private IStemmer stemmer;
	private BlockingQueue<String> inputPipe;
	private BlockingQueue<String> outputPipe;
//	private long startTime;
//	private long endTime;
//	private long durationInNano = 0L;

	public StemFilter(IStemmer stemmer, BlockingQueue<String> inputPipe, BlockingQueue<String> outputPipe) {
		this.inputPipe = inputPipe;
		this.outputPipe = outputPipe;
		this.stemmer = stemmer;
	}

	@Override
	public void run() {
		read();	
//		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
//		System.out.println("StemFilter response time = " + durationInMillis + " milliseconds");
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
				stemmer.add(str.toCharArray(), str.length());
				stemmer.stem();
				String newWord = stemmer.toString();
				if(newWord.equals("")) {
					continue;
				}
				write(newWord);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String s) {
		try {
			outputPipe.put(s);
//			endTime = System.nanoTime();
//			durationInNano += (endTime - startTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

}
