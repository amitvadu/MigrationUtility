package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteData {

	private String fullPath = "";
	private boolean appendToFile = false;
	private static DateFormat dateFormatForDirectory = new SimpleDateFormat("ddMMMyyyy");
	private static Date newdateForDirectory = new Date();
	private static String currentDate = dateFormatForDirectory.format(newdateForDirectory).toUpperCase();

	public WriteData() {

	}

	public WriteData(String path) {
		fullPath = path;
	}

	public WriteData(String path, boolean appendValue) {
		fullPath = path;
		appendToFile = appendValue;
	}

	public static void writeToFile(String fileName, String strLine, boolean fileAppend) {

		String dirFilePath = Constant.BASE_PATH + "/TestData/logs/";

		DateFormat dateFormatForDirectory = new SimpleDateFormat("ddMMMyyyy");
		Date newdateForDirectory = new Date();
		String currentDate = dateFormatForDirectory.format(newdateForDirectory).toUpperCase();
		// System.out.println("currentDate = " + currentDate);

		dirFilePath = dirFilePath + currentDate;
		File dirFilePathfile = new File(dirFilePath);

		if (!dirFilePathfile.exists()) {

			if (dirFilePathfile.mkdir()) {
				System.out.println("Log Directory is created!");
			} else {
				System.out.println("Log Failed to create directory!");
			}
		}

		// -------------------------------------------------------

		// String fullPathWithFileName = Constant.BASE_PATH + "/TestData/logs/" +
		// fileName;
		String fullPathWithFileName = dirFilePath + "/" + fileName;

		FileWriter fw = null;
		PrintWriter pw = null;

		try {
			fw = new FileWriter(fullPathWithFileName, fileAppend);
			pw = new PrintWriter(fw);
			pw.printf("%s" + "%n", strLine);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static boolean fileExist(String fileName) {

		String fullPathWithFileName = Constant.BASE_PATH + "/TestData/DownloadFiles/" + fileName;
		boolean fileExist = false;
		File file = new File(fullPathWithFileName);
		if (file.exists() && file.isFile()) {
			String msg = "File " + fileName + " is exist on disk at " + fullPathWithFileName;
			// System.out.println( msg);
			Utility.printLog("automation-execution.log", "File Exist", msg, "");
			fileExist = true;
		} else {

			String msg = "File " + fileName + " is not exist at " + fullPathWithFileName;
			// System.out.println(msg);
			Utility.printLog("automation-execution.log", "File Exist", msg, "");
		}

		return fileExist;
	}

	public static boolean fileExist(String filePath, String fileName) {

		String fullPathWithFileName = filePath + fileName;
		boolean fileExist = false;
		File file = new File(fullPathWithFileName);
		if (file.exists() && file.isFile()) {
			String msg = "File " + fileName + " is exist on disk at " + fullPathWithFileName;
			// System.out.println( msg);
			Utility.printLog("automation-execution.log", "File Exist", msg, "");
			fileExist = true;
		} else {

			String msg = "File " + fileName + " is not exist at " + fullPathWithFileName;
			// System.out.println(msg);
			Utility.printLog("automation-execution.log", "File Exist", msg, "");
		}

		return fileExist;
	}

	// public void directoryExist(String fullPath,String directoryName){
	public static void directoryExist() {
		String dirFilePath = Constant.BASE_PATH + "/TestData/logs/";

		dirFilePath = dirFilePath + currentDate;
		File dirFilePathfile = new File(dirFilePath);

		if (!dirFilePathfile.exists()) {
			if (dirFilePathfile.mkdir()) {
				System.out.println("A log Directory is created!");
			} else {
				System.out.println("Failed to create a log directory!");
			}
		}
	}

	/**
	 * Use BufferedWriter when number of write operations are more It uses internal
	 * buffer to reduce real IO operations and saves time
	 * 
	 * @param data
	 * @param noOfLines
	 */
	public static void writeUsingBufferedWriter(String fileName, String data) {

		String filePath = Constant.BASE_PATH + "/TestData/logs/";
		filePath = filePath + currentDate + "/";
		fileName = fileName.toLowerCase() + "-stock-price.log";
		String filePathWithFileName = filePath + fileName;

		File file = new File(filePathWithFileName);
		FileWriter fr = null;
		BufferedWriter br = null;
		// String dataWithNewLine=data+System.getProperty("line.separator");
		String dataWithNewLine = data;
		try {

			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			br.write(dataWithNewLine);
			br.newLine();
			br.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}