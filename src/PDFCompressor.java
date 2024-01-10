import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFCompressor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			PdfReader reader = new PdfReader(new FileInputStream("C:\\Users\\g802940\\Downloads\\null.pdf"));
			System.out.println("The initial size of pdf is : "+reader.getFileLength() + " with pages " + reader.getNumberOfPages());
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C:\\Users\\g802940\\Downloads\\output.pdf"));
			int total = reader.getNumberOfPages() + 1;
			for ( int i=1; i<total; i++) {
			   reader.setPageContent(i + 1, reader.getPageContent(i + 1));
			}
			//stamper.setFullCompression();
			stamper.close();
			
			PdfReader final_reader = new PdfReader(new FileInputStream("C:\\Users\\g802940\\Downloads\\output.pdf"));
			System.out.println("Compressed successfully with final size = " + final_reader.getFileLength() + " with pages " +final_reader.getNumberOfPages());
			
		} catch (Exception e) {
			
			// TODO: handle exception
			e.printStackTrace();
		}
		

	}

}
