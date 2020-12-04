import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class DataSource implements Runnable {
	private BlockingQueue<String> output;
	private String dataSource;
//	public static long startTime;
	
	public DataSource(String datasource, BlockingQueue<String> output) {
		this.output = output;
		this.dataSource = datasource;
	}

	@Override
	public void run() {
		start();
	}
	
	private void start() {
		try {
			File file = new File(dataSource);
			Scanner myReader = new Scanner(file);
//			startTime = System.nanoTime();
			while (myReader.hasNext()) {
				String s = myReader.next();
				if (s != "\t" && s != " " && s != "")
					try {
						output.put(s);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			try {
				output.put("stopFiltering");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

}
