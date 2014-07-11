package com.codinghero.sql.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

public final class FileUtils {
	
	/**
	 * linux:<code>\n</code><br/>
	 * windows:<code>\r\n</code>
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**
	 * linux:<code>/</code><br/>
	 * windows:<code>\</code>
	 */
	public static final String FILE_SEPARATOR = File.separator;
	
	/**
	 * linux:<code>//</code><br/>
	 * windows:<code>\\</code>
	 */
	public static final String FILE_SEPARATOR_REGEX = File.separator.equals("//")?"////":"\\\\";
	
	private static final int BUFFER_SIZE = 1024;
	
	private static final String TEMP_FILE_POSTFIX = ".com.bupt.liutong.util";
	
	public static String getFileType(String orig) {
		if (StringUtils.isEmpty(orig))
			throw new NullPointerException("The file path should not be null");
		int index = orig.lastIndexOf(".");
		if (index == -1)
			return "";
		else
			return orig.substring(index + 1, orig.length());
	}
	
	public static String getFileName(String orig) {
		if (StringUtils.isEmpty(orig))
			throw new NullPointerException("The file path should not be null");
		String fileName = new File(orig).getName();
		if (fileName.contains("."))
			return fileName;
		else
			return "";
	}
	
	public static String getSimpleFileName(String orig) {
		if (StringUtils.isEmpty(orig))
			throw new NullPointerException("The file path should not be null");
		String fileName = getFileName(orig);
		int index = fileName.lastIndexOf(".");
		if (index == -1)
			return "";
		else
			return fileName.substring(0, index);
	}
	
	@Deprecated
	public static String getFileNameWithoutPostfix(String orig) {
		return getSimpleFileName(orig);
	}
	
	public static boolean newFolder(String folderPath) {
		if (folderPath == null)
			throw new NullPointerException("The folder path should not be null");
		File folder = new File(folderPath);
		if (!folder.exists())
			return folder.mkdirs();
		else
			return true;
	}
	
	public static boolean newFile(String filePath) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		try {
			File file = new File(filePath);
			// If the file not exists
			if (file.exists()) {
				return false;
			} else {
				// If the folder not exists
				if (file.getParent() != null && !file.getParentFile().exists()) {
					newFolder(file.getParent());
				}
				return file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean newFile(String filePath, String fileContent) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		if (fileContent == null)
			throw new NullPointerException("The file content should not be null");
		if (!newFile(filePath)) {
			return false;
		} else {
			return appendFile(filePath, fileContent);
		}
	}
	
	public static boolean newFile(String filePath, String fileContent, String encoding) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		if (fileContent == null)
			throw new NullPointerException("The file content should not be null");
		if (!newFile(filePath)) {
			return false;
		} else {
			return appendFile(filePath, fileContent, encoding);
		}
	}
	
	public static boolean forceNewFile(String filePath) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		deleteFile(filePath);
		return newFile(filePath);
	}
	
	public static boolean forceNewFile(String filePath, String fileContent) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		if (fileContent == null)
			throw new NullPointerException("The file content should not be null");
		deleteFile(filePath);
		return newFile(filePath, fileContent);
	}
	
	public static boolean forceNewFile(String filePath, String fileContent, String encoding) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		if (fileContent == null)
			throw new NullPointerException("The file content should not be null");
		deleteFile(filePath);
		return newFile(filePath, fileContent, encoding);
	}
	
	/**
	 * Add Data To File
	 * 
	 * @param filePath
	 * @param fileContent
	 * @return
	 */
	public static boolean appendFile(String filePath, String fileContent) {
		return appendFile(filePath, fileContent, null);
	}

	public static boolean appendlnFile(String filePath, String fileContent) {
		return appendFile(filePath, fileContent + LINE_SEPARATOR, null);
	}

	public static boolean appendlnFile(String filePath, String fileContent, String encoding) {
		return appendFile(filePath, fileContent + LINE_SEPARATOR, encoding);
	}

	/**
	 * Add Data To File
	 * 
	 * @param filePath
	 * @param fileContent
	 */
	public static boolean appendFile(String filePath, String fileContent, String encoding) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		if (fileContent == null)
			throw new NullPointerException("The file content should not be null");

		// If not exists, new a file firstly
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		PrintWriter printWriter = null;
		// Append content
		try {
			if (encoding == null)
				printWriter = new PrintWriter(new BufferedWriter(
						new FileWriter(file, true)));
			else
				printWriter = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath,
								true), encoding)));
			printWriter.print(fileContent);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			printWriter.close();
		}
	}

	/**
	 * Delete Empty Folder
	 * 
	 * @param folderPath
	 */
	public static boolean deleteEmptyFolder(String folderPath) {
		if (folderPath == null)
			throw new NullPointerException("The folder path should not be null");
		
		File folder = new File(folderPath);
		if (folder.list().length == 0) {
			return folder.delete();
		} else {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isDirectory())
					deleteEmptyFolder(file.getAbsolutePath());
				else
					throw new IllegalStateException(
							"The folder "
							+ folderPath
							+ " should be empty when using the 'deleteEmptyFolder'");
			}
			return folder.delete();
		}
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		if (filePath == null)
			throw new NullPointerException("The file path should not be null");
		return new File(filePath).delete();
	}

	/**
	 * Delete Files In The Folder Cascade, but leave the folder and sub-folder 
	 * 
	 * @param folderPath
	 */
	public static void cleanFolder(String folderPath) {
		cleanFolder(folderPath, ".*");
	}

	public static void cleanFolder(String folderPath, String regex) {
		if (folderPath == null)
			throw new NullPointerException("The folder path should not be null");
		
		File folder = new File(folderPath);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			return;
		}
		
		File[] files = folder.listFiles();
		for (File file : files) {
			// not delete the folder which matches, just clean
			if (file.isDirectory()) {
				cleanFolder(file.getAbsolutePath(), regex);
			}
			// delete the element which is file or directory
			else if (file.isFile() && file.getName().matches(regex)) {
				file.delete();
			}
		}
	}

	public static void deleteFolder(String folderPath, String regex) {
		if (folderPath == null)
			throw new NullPointerException("The folder path should not be null");
		
		File folder = new File(folderPath);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			return;
		}
		
		File[] files = folder.listFiles();
		// delete matches file and folder
		for (File file : files) {
			// delete the folder which matches, not only clean
			if (file.isDirectory() && file.getName().matches(regex)) {
				deleteFolder(file.getAbsolutePath());
			}
			// delete the element which is file or directory
			else if (file.isFile() && file.getName().matches(regex)) {
				file.delete();
			}
		}
		
		// delete empty & matches
		if (folder.listFiles().length == 0 && folder.isDirectory() && folder.getName().matches(regex))
			deleteEmptyFolder(folder.getAbsolutePath());
	}

	/**
	 * Delete Folder And Files In The Folder Cascade
	 * 
	 * @param folderPath
	 */
	public static void deleteFolder(String folderPath) {
		deleteFolder(folderPath, ".*");
	}

	/**
	 * 
	 * @param origPath
	 * @param destPath
	 * @return
	 */
	public static boolean copyFile(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (destPath == null)
			throw new NullPointerException("The destination path should not be null");
		
		InputStream is = null;
		OutputStream os = null;
		try {
			if (new File(origPath).exists() && !new File(destPath).exists()) {
//				reader = new BufferedReader(new FileReader(origPath));
//				writer = new PrintWriter(new BufferedWriter(new FileWriter(
//						destPath)));
//				writer = new PrintWriter(new BufferedWriter(
//						new OutputStreamWriter(new FileOutputStream(
//								destPath), CHARSET)));
				is = new FileInputStream(origPath);
				os = new FileOutputStream(destPath);
				write(is, os);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			close(os);
			close(is);
		}
	}

	public static boolean copyFolder(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (destPath == null)
			throw new NullPointerException("The destination path should not be null");
		
		if (new File(origPath).exists()) {
			if (newFolder(destPath)) {
				File folder = new File(origPath);
				String[] fileNames = folder.list();
				for (int i = 0; i < fileNames.length; i++) {
					File file = new File(origPath + File.separator + fileNames[i]);
					if (file.isFile()) {
						copyFile(file.getPath(), destPath + File.separator + fileNames[i]);
					} else if (file.isDirectory()) {
						copyFolder(file.getPath(), destPath + File.separator + fileNames[i]);
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean forceCopyFile(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (destPath == null)
			throw new NullPointerException("The destination path should not be null");
		if (new File(destPath).exists())
			deleteFile(destPath);
		return copyFile(origPath, destPath);
	}

	public static boolean forceCopyFile(InputStream is, String destPath) {
		if (is == null)
			throw new NullPointerException("The inputstream should not be null");
		if (destPath == null)
			throw new NullPointerException(
					"The destination path should not be null");
		forceNewFile(destPath);
		try {
			OutputStream os = new FileOutputStream(destPath);
			try {
				write(is, os);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				close(os);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			close(is);
		}
		return true;
	}


	/**
	 * 
	 * @param origPath
	 * @param destPath
	 * @return
	 */
	public static boolean forceCopyFolder(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (destPath == null)
			throw new NullPointerException("The destination path should not be null");
		
		if (new File(origPath).exists()) {
			if (newFolder(destPath)) {
				File folder = new File(origPath);
				String[] fileNames = folder.list();
				for (int i = 0; i < fileNames.length; i++) {
					File origFile = new File(origPath + File.separator + fileNames[i]);
					File destFile = new File(destPath + File.separator + fileNames[i]);
					if (origFile.isFile()) {
						// force delete
						if (destFile.exists())
							deleteFile(destFile.getPath());
						copyFile(origFile.getPath(), destFile.getPath());
					} else if (origFile.isDirectory()) {
						// force delete
						if (destFile.exists())
							deleteFolder(destFile.getPath());
						copyFolder(origFile.getPath(), destFile.getPath());
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param origPath
	 * @param destPath
	 */
	public static void forceMoveFile(String origPath, String destPath) {
		deleteFile(destPath);
		copyFile(origPath, destPath);
		deleteFile(origPath);
	}

	/**
	 * 
	 * @param origPath
	 * @param destPath
	 */
	public static void forceMoveFolder(String origPath, String destPath) {
		deleteFolder(destPath);
		copyFolder(origPath, destPath);
		deleteFolder(origPath);
	}

	public static String getContent(String origPath) {
		return getContent(origPath, null);
	}

	public static String getContent(String origPath, String encoding) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (!new File(origPath).exists())
			throw new IllegalArgumentException("The original file doesn't exist");
		
		StringWriter writer = new StringWriter();
		Reader reader = null;
		try {
			if (encoding == null)
				reader = new InputStreamReader(new FileInputStream(origPath));
			else
				reader = new InputStreamReader(new FileInputStream(origPath), encoding);
			write(reader, writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(writer);
			close(reader);
		}
		return writer.toString();
	}
	
	/**
	 * The WEB-INF path in our disk
	 * @return
	 */
	public static String getWebinfPath(){
		return FileUtils.class.getResource("/").getPath().replaceAll("%20", " ")+"../";
	}
	
	/**
	 * The project path in our disk
	 * @return
	 */
	public static String getProjectPath() {
		return FileUtils.class.getResource("/").getPath()
				.replaceAll("%20", " ") + "../../";
	}
	
	/**
	 * unzip a zip file
	 * @param origPath the original zip file
	 * @param destPath the files output path
	 */
	public static void unzip(String origPath, String destPath){
		if (!destPath.endsWith(File.separator)) {
			destPath += File.separator;
		}
		// create the output path
		newFolder(destPath);
		
		InputStream is = null;
		OutputStream os = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(origPath);
			@SuppressWarnings("unchecked")
			Enumeration<ZipEntry> zipEntryEnum = (Enumeration<ZipEntry>) zipFile.entries();
			// iterate find every file in it cascade
			while (zipEntryEnum.hasMoreElements()) {
				ZipEntry zipEntry = zipEntryEnum.nextElement();
				// output directory
				if (zipEntry.isDirectory()) {
					newFolder(destPath + zipEntry.getName());
				}
				// output file
				else {
					newFile(destPath + zipEntry.getName());
					is = zipFile.getInputStream(zipEntry);
					os = new FileOutputStream(destPath + zipEntry.getName());
					write(is, os);
					// close every zip file & output file
					close(os);
					close(is);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(os);
			close(is);
			// close zip file
			closeZipFile(zipFile);
		}
	}
	
	/**
	 * zip file or folder, unless the destination file if already exists
	 * 
	 * @param origPath
	 * @param destPath
	 * @return
	 */
	public static boolean zip(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException("The original path should not be null");
		if (destPath == null)
			throw new NullPointerException("The destination path should not be null");
		File orig = new File(origPath);
		File destFile = new File(destPath);
		if (!orig.exists()) {
			return false;
		} else if (destFile.exists()) {
			return false;
		} else {
			ZipOutputStream zos = null;
			try {
				zos = new ZipOutputStream(new FileOutputStream(destFile));
				zip(orig, zos, "");
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				close(zos);
			}
		}
	}

	/**
	 * zip file or folder whatever
	 * 
	 * @param origPath
	 * @param destPath
	 * @return
	 */
	public static synchronized boolean forceZip(String origPath, String destPath) {
		if (origPath == null)
			throw new NullPointerException(
					"The original path should not be null");
		if (destPath == null)
			throw new NullPointerException(
					"The destination path should not be null");
		if (new File(destPath).exists())
			deleteFile(destPath);
		return zip(origPath, destPath);
	}
	
	/**
	 * 
	 * @param orig the original file|folder ready to zip
	 * @param zos the zip output
	 * @param relativePath relative path in zip file, ex. <code>/</code> or <code>test/</code> or <code>test/sub/sub.txt</code>
	 * @throws IOException
	 */
	private static void zip(File orig, ZipOutputStream zos, String relativePath) throws IOException {
		// zip directory
		if (orig.isDirectory()) {
			if (StringUtils.isEmpty(relativePath))
				relativePath = File.separator;
			else
				relativePath += File.separator;
			File[] files = orig.listFiles();
			for (File file : files) {
				zip(file, zos, relativePath + orig.getName());
			}
		}
		// zip file
		else {
			zos.putNextEntry(new ZipEntry(relativePath + File.separator + orig.getName()));
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(orig);
				write(fis, zos);
			} finally {
				close(fis);
			}
		}
	}
	
	/**
	 * no test, but will use in future
	 * @param origPath
	 * @param origEncoding
	 * @param destEncoding
	 */
	@Deprecated
	public static void convertFileEncoding(String origFilePath, String origEncoding, String destEncoding) {
		if (origFilePath == null)
			throw new NullPointerException("The file path should not be null");
		if (origEncoding == null)
			throw new NullPointerException("The origional encoding should not be null");
		if (destEncoding == null)
			throw new NullPointerException("The destination encoding should not be null");
		// convert to a temporary file
		convertFileEncoding(origFilePath, origFilePath + TEMP_FILE_POSTFIX, origEncoding, destEncoding);
		// copy temporary file to orig path, and delete temporary file
		deleteFile(origFilePath);
		forceMoveFile(origFilePath + TEMP_FILE_POSTFIX, origFilePath);
		
	}
	
	/**
	 * no test, but will use in future
	 * 
	 * @param folderPath
	 * @param origEncoding
	 * @param destEncoding
	 * @return
	 */
	@Deprecated
	public static boolean convertFolderEncoding(String folderPath, String origEncoding, String destEncoding) {
		if (folderPath == null)
			throw new NullPointerException("The folder path should not be null");
		if (origEncoding == null)
			throw new NullPointerException("The origional encoding should not be null");
		if (destEncoding == null)
			throw new NullPointerException("The destination encoding should not be null");
		File folder = new File(folderPath);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					convertFolderEncoding(file.getPath(), origEncoding, destEncoding);
				} else {
					convertFileEncoding(file.getPath(), origEncoding, destEncoding);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * no test, but will use in future
	 * @param origPath
	 * @param destPath
	 * @param origEncoding
	 * @param destEncoding
	 * @return
	 */
	@Deprecated
	public static boolean convertFileEncoding(String origPath, String destPath, 
			String origEncoding, String destEncoding) {
		if (origPath == null)
			throw new NullPointerException("The file path should not be null");
		if (destPath == null)
			throw new NullPointerException("The file path should not be null");
		if (origEncoding == null)
			throw new NullPointerException("The origional encoding should not be null");
		if (destEncoding == null)
			throw new NullPointerException("The destination encoding should not be null");

		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			if (new File(origPath).exists() && !new File(destPath).exists()) {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(origPath), origEncoding));
				writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(
								destPath), destEncoding)));
				// must use char array when coverting
				write(reader,writer);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			close(writer);
			close(reader);
		}
	}
	
	private static void write(InputStream is, OutputStream os) throws IOException{
		if (is == null)
			throw new NullPointerException();
		if (os == null)
			throw new NullPointerException();
		byte[] buffer = new byte[BUFFER_SIZE];
		int byteread = 0;
		while ((byteread = is.read(buffer)) != -1) {
			os.write(buffer, 0, byteread);
		}
	}
	
	public static void write(Reader reader, Writer writer) throws IOException {
		if (reader == null)
			throw new NullPointerException();
		if (writer == null)
			throw new NullPointerException();
        char[] buffer = new char[BUFFER_SIZE];
		int byteread = 0;
        while ((byteread = reader.read(buffer))!=-1) {
        	writer.write(buffer, 0, byteread);
        }
    }
	
	private static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void closeZipFile(ZipFile zipFile) {
		try {
			if (zipFile != null) {
				zipFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
