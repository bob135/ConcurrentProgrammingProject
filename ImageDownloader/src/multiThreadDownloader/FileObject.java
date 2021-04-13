package multiThreadDownloader;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Simple object to contain image to be operated on by all tasks.
 */
public class FileObject {
	private File downloadPath;
	private String fileName;
	private BufferedImage image;
	
	public FileObject (File path, String fileName, BufferedImage img) {
		this.downloadPath = path;
		this.fileName = fileName;
		this.image = img;
	}
	
	public File getDownloadPath()
	{
		return downloadPath;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}

	
}
