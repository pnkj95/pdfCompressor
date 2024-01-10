
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;



public class PDFCompressor2 {
  public static void main(String[] args) throws Exception {
    PdfReader reader = new PdfReader("C:\\Users\\g802940\\Downloads\\iText\\null.pdf");
    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C:\\Users\\g802940\\Downloads\\iText\\my.pdf"),PdfWriter.VERSION_1_7);
    stamper.setFullCompression();
    stamper.close();

    reader = new PdfReader("C:\\Users\\g802940\\Downloads\\iText\\null.pdf");
    stamper = new PdfStamper(reader, new FileOutputStream("C:\\Users\\g802940\\Downloads\\iText\\myDecompressed.pdf"), '1');
    Document.compress = false;
    int total = reader.getNumberOfPages() + 1;
    for (int i = 1; i < total; i++) {
      reader.setPageContent(i, reader.getPageContent(i));
    }
    stamper.close();

    showFileSize("C:\\Users\\g802940\\Downloads\\iText\\null.pdf");
    showFileSize("C:\\Users\\g802940\\Downloads\\iText\\my.pdf");
    showFileSize("C:\\Users\\g802940\\Downloads\\iText\\myDecompressed.pdf");

  }

  private static void showFileSize(String filename) throws IOException {
    PdfReader reader = new PdfReader(filename);
    System.out.print("Size ");
    System.out.print(filename);
    System.out.print(": ");
    System.out.println(reader.getFileLength());
  }
}
