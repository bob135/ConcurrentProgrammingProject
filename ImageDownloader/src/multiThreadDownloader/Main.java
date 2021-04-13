package multiThreadDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Driver to take input from user and start image controller
 */
public class Main 
{

	/*
	 * Packs list of files to download and hands that
	 * along with user input to main controller class
	 */
	public static void main(String[] args) throws IOException 
	{
		File downloadDir;
		Scanner scanner = new Scanner(System.in);
		
		String path;
		String numThreads;
		do {
			System.out.println("Enter a valid path for file storage:  ");
			path = scanner.next();
			System.out.println("Enter the number of threads to be run");
			numThreads = scanner.next();
			downloadDir = new File(path);
			downloadDir.isDirectory();
		} while (!downloadDir.isDirectory() || !downloadDir.canWrite());
		scanner.close();
		
		Vector<String> fileList = new Vector<String>();

		fileList.add("http://elvis.rowan.edu/~carter89/Pics/Beach%20background.jpg");
		fileList.add("http://elvis.rowan.edu/~carter89/Pics/Game%20background.jpg");
		fileList.add("http://elvis.rowan.edu/~carter89/Pics/Rowan%20backgroundd.jpg");
		fileList.add("http://elvis.rowan.edu/~carter89/Pics/Sired.png");
		fileList.add("http://elvis.rowan.edu/~carter89/Pics/SirTy.png");
		fileList.add("http://elvis.rowan.edu/~carter89/Pics/Social%20background.jpg");

		fileList.add("NOMOREFILES");
		
		Controller mainController = new Controller();
		mainController.downloadConvertStoreImages(downloadDir, numThreads, fileList);
		
	}
	
}
