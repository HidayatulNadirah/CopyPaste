package project01;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Scanner;

public class CopyPasteFIle {

	public static void main(String[] args) throws FileNotFoundException,
	IOException{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please type source file (including its path)");
		String source = scanner.nextLine();
		File sourceFile = new File(source);
		
		while(!sourceFile.exists()) {
			System.out.println("Source file does not exists.");
			System.out.println("Please type source file (including its path)");
			source = scanner.nextLine();
			sourceFile = new File(source);
		}
		
		System.out.println("Please type target file (including its path)");
		String target = scanner.nextLine();
		File targetFile = new File(target);
		while(targetFile.exists()) {
			System.out.println("Target file already exists.");
			System.out.println("Please type target file (including its path)");
			target = scanner.nextLine();
			targetFile = new File(target);
		}
		 try( 
		BufferedInputStream input = new BufferedInputStream(new 
				FileInputStream(sourceFile));
		BufferedOutputStream output = new BufferedOutputStream(new 
				FileOutputStream(targetFile));
				 ){
			 int fileContent = 0;
			 int numberOfBytesCopied = 0;
			 while((fileContent = input.read()) != -1) {
				 output.write((byte) fileContent);
				 numberOfBytesCopied++;
			 }
			 System.out.println("File is copied. No of Bytes: "+ numberOfBytesCopied);
		 }
		
	scanner.close();	
		
	}

}
