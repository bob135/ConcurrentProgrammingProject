package multiThreadDownloader;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Splits up and starts threads to download, convert, and save image files
 */
public class Controller {

	protected BlockingQueue<Object> downloadedQueue;
    protected BlockingQueue<Object> convertedQueue;
    private File downloadDir;
    private int numThreadsInt;
    private Vector<String> fileList = new Vector<String>();
    
	public Controller()
	{
	}
	
	/*
	 * @Input File
	 * @Input String
	 * @Input Vector<String>
	 * 
	 * Creates and kicks off threads to download convert and store images
	 */
	public void downloadConvertStoreImages(File inDownloadDir, String numThreads, Vector<String> inList)
	{
		downloadDir = inDownloadDir;
		numThreadsInt = Integer.parseInt(numThreads);
		fileList = inList;
		
		downloadedQueue = new ArrayBlockingQueue<Object>(21);
		convertedQueue = new ArrayBlockingQueue<Object>(21);
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreadsInt);
		executor.submit(new Downloader(downloadedQueue, fileList, downloadDir));
		
		AtomicInteger numFileSaversCreated = new AtomicInteger(0);
		final long startTime = System.currentTimeMillis();
		
		for (int i=0; i < numThreadsInt / 2; i++)
		{
			executor.submit(new Converter(downloadedQueue, convertedQueue));
			executor.submit(new FileSaver(convertedQueue, numFileSaversCreated));
			numFileSaversCreated.incrementAndGet();
		}
		
		executor.shutdown();
		
		while (numFileSaversCreated.get() > 0)
		{
			try        
			{
				  Thread.sleep(1);
			}
			catch(InterruptedException ex) 
			{
				   Thread.currentThread().interrupt();
			}
		}
	    
	    final long durationMillis = (System.currentTimeMillis() - startTime);
	    
	    String durationString = Long.toString(durationMillis);
	    System.out.println("Duration: " + durationString + "ms");
	}
}
