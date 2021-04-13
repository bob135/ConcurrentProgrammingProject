package multiThreadDownloader;
import java.awt.Color;
import java.util.concurrent.BlockingQueue;

/**
 * @Input BlockingQueue<Object>
 * @Input Blockingqueue<Object>
 * 
 * NOTE:  Grayscale conversion logic taken from:
 *        https://www.tutorialspoint.com/java_dip/grayscale_conversion.htm
 *        
 * Class to handle converting image files to grayscale
 */
public class Converter implements Runnable 
{
    protected BlockingQueue<Object> downloadedQueue;
    protected BlockingQueue<Object> convertedQueue;
 
    Converter(BlockingQueue<Object> inDownloadedQueue, BlockingQueue<Object> inConvertedQueue) 
    {
        this.downloadedQueue = inDownloadedQueue;
        this.convertedQueue = inConvertedQueue;
    }
 
    /*
     * Pulls downloaded files from queue, converts them, then adds to converted queue
     * Stops when a poison pill is found
     * New poison pill added to downloaded queue to tell remaining Converter threads to stop
     * New poison pill added to end of converted queue to tell FileSaver no more files remain to be saved
     */
    public void run() 
    {
        try
        {
            while (true) 
            {
                Object tempObj = downloadedQueue.take();
                if (tempObj instanceof PoisonPill)
                {
                	downloadedQueue.put(new PoisonPill());
                	convertedQueue.put(new PoisonPill());
                	break; //task ends
                }
                FileObject obj = (FileObject) tempObj;
        		int width = obj.getImage().getWidth();
        		int height = obj.getImage().getHeight();
        		
        		for(int i=0; i<height; i++)
        		{
        	         
                    for(int j=0; j<width; j++)
                    {
                    
                       Color c = new Color(obj.getImage().getRGB(j, i));
                       int red = (int)(c.getRed() * 0.299);
                       int green = (int)(c.getGreen() * 0.587);
                       int blue = (int)(c.getBlue() *0.114);
                       Color newColor = new Color(red+green+blue,
                           red+green+blue,red+green+blue);
                       
                       obj.getImage().setRGB(j,i,newColor.getRGB());
                    }
        		}
                convertedQueue.put(obj);
            }
        }
        catch (InterruptedException ex) 
        {
            System.out.println("Converter INTERRUPTED");
        }
    }
}