package multiThreadDownloader;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

/**
 * @Input BlockingQueue<Object>
 * @Input AtomicInteger
 * 
 * Writes files to disk.
 *
 */
public class FileSaver implements Runnable {


protected BlockingQueue<Object> queue;
private AtomicInteger numFileSaverThreads;

 FileSaver (BlockingQueue<Object> theQueue, AtomicInteger inNumFileSaverThreads) 
 {
    this.queue = theQueue;
    numFileSaverThreads = inNumFileSaverThreads;
}

 /*
  * Saves the converted files
  * Stops when a poison pill is found
  * New poison pill added to queue to tell remaining FileSaver threads to stop
  */
 public void run() 
 {
    try
    {
        while (true) 
        {
            Object tempObj = queue.take();
            if (tempObj instanceof PoisonPill)
            {
            	queue.put(new PoisonPill());
            	numFileSaverThreads.decrementAndGet();
            	break; //task ends
            }
            FileObject obj = (FileObject) tempObj;
            
    		File outFile = new File(obj.getDownloadPath() + "/" + obj.getFileName());		
    		ImageIO.write(obj.getImage(), "jpg", outFile);
        }
    } 
    catch (InterruptedException | IOException ex) 
    {
        System.out.println("FileSaver INTERRUPTED");
    }
 }
 
}
