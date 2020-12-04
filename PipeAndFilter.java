import java.io.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

public class PipeAndFilter {
	

	private static final BlockingQueue<String> pipe01 = new ArrayBlockingQueue<>(10);
	private static final BlockingQueue<String> pipe12 = new ArrayBlockingQueue<>(10);
	private static final BlockingQueue<String> pipe23 = new ArrayBlockingQueue<>(10);
	private static final BlockingQueue<String> pipe34 = new ArrayBlockingQueue<>(10);
	private static final Map<String,String> charsToIncludeMap = new HashMap<String, String>();
	private static final Map<String,File> stopWordsMap = new HashMap<String, File>();
	private static final Map<String,IStemmer> stemmerMap = new HashMap<String, IStemmer>();

	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Must specify a language and input file name");
			return;
		}
		initializeLanguageParameters();
		String language = args[0].toLowerCase();
		if(!language.equals("english")) {
			throw new RuntimeException("only english is supported at this time");
		}
		String inputFile = args[1].toLowerCase();
		new Thread(new DataSource(inputFile, pipe01)).start();
		new Thread(new RemoveNonAlphabeticalFilter(charsToIncludeMap.get(language), pipe01, pipe12)).start();
		new Thread(new StopwordFilter(stopWordsMap.get(language), pipe12, pipe23)).start();
		new Thread(new StemFilter(stemmerMap.get(language), pipe23, pipe34)).start();
		new Thread(new DataSink(pipe34)).start();
	}
	
	
	private static void initializeLanguageParameters() {
		charsToIncludeMap.put("english", "[^a-zA-Z]");
		stopWordsMap.put("english", new File("stopwords.txt"));
		stemmerMap.put("english", new Stemmer());
	}
}
