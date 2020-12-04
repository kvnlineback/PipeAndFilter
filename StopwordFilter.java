import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class StopwordFilter implements IFilter, Runnable {
	
	private File stopWords;
	private BlockingQueue<String> inputPipe;
	private BlockingQueue<String> outputPipe;
	private List<String> stopWordsList;
//	private long startTime;
//	private long endTime;
//	private long durationInNano = 0L;

	public StopwordFilter(File stopWords, BlockingQueue<String> inputPipe, BlockingQueue<String> outputPipe) {
		this.inputPipe = inputPipe;
		this.outputPipe = outputPipe;
		this.stopWords = stopWords;
		stopWordsList = new ArrayList<String>();
		initialize();
	}
	
	Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

	@Override
	public void run() {
		read();
//		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
//		System.out.println("response time for stopwordFilter = " + durationInMillis + " milliseconds");
	}

	@Override
	public void read() {
		OUTER: while(true) {
			try {
				String str = inputPipe.take();
//				startTime = System.nanoTime();
				if(str.equals("stopFiltering")) {
					write("stopFiltering");
					return;
				}
				for(String s: stopWordsList) {
					if(str.toLowerCase().equals(s)) {
						continue OUTER;
					}
				}
				write(str);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
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
	
	private void initialize() {
		try {
			Scanner myReader = new Scanner(stopWords);
			while (myReader.hasNext()) {
				stopWordsList.add(myReader.next());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
