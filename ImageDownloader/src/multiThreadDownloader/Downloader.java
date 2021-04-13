package multiThreadDownloader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

/**
 * @Input BlockingQueue<Object>
 * @Input Vector<String>
 * @Input File
 * 
 * Class to handle downloading of jpg files
 *
 */
class Downloader implements Runnable 
{
    protected BlockingQueue<Object> downloadedQueue;
    private Vector<String>filenames;
    private File downloadDir;
 
    Downloader (BlockingQueue<Object> theQueue, Vector<String> inFilenames, File inDownloadDir) 
    {
        this.downloadedQueue = theQueue;
        filenames = inFilenames;
        downloadDir = inDownloadDir;
    }
 
    /*
     * @throws IOException
     * 
     * Continously downloads files taken from filenames list
     * Stops when specific String is found
     * Poison Pill added to end of queue so converter knows no more files remain
     */
    public void run() 
    {
        try
        {
            for (String filename: filenames) 
            {
            	if(filename.equals("NOMOREFILES"))
            	{
            		downloadedQueue.put(new PoisonPill());
            		break; //end of task
            	}
    			URL url = new URL(filename);
    			BufferedImage image = ImageIO.read(url);
    			String fname = url.getFile().substring(url.getFile().lastIndexOf("/")+1);
    			FileObject obj = new FileObject(downloadDir, fname, image);
                downloadedQueue.put((Object)obj);
            }
            
        } 
        catch (InterruptedException | IOException ex) 
        {
            System.out.println("Downloader INTERRUPTED: " + ex.toString());
        }
    }
    
}