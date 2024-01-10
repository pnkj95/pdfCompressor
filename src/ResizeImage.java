import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;

public class ResizeImage {

	/** The resulting PDF file. */
//public static String RESULT = "results/part4/chapter16/resized_image.pdf";
	/** The multiplication factor for the image. */
	public static float FACTOR = 0.5f;

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * 
	 * @param src  the original PDF
	 * @param dest the resulting PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfName key = new PdfName("ITXT_SpecialId");
		PdfName value = new PdfName("123456789");
		// Read the file
		PdfReader reader = new PdfReader(src);
		int n = reader.getXrefSize();
		PdfObject object;
		PRStream stream;
		// Look for image and manipulate image stream
		for (int i = 0; i < n; i++) {
			object = reader.getPdfObject(i);
			if (object == null || !object.isStream())
				continue;
			stream = (PRStream) object;
			// if (value.equals(stream.get(key))) {
			PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
			System.out.println(stream.type());
			if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
				PdfImageObject image = new PdfImageObject(stream);
				BufferedImage bi = image.getBufferedImage();
				if (bi == null)
					continue;
				int width = (int) (bi.getWidth() * FACTOR);
				int height = (int) (bi.getHeight() * FACTOR);
				BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
				Graphics2D g = img.createGraphics();
				g.drawRenderedImage(bi, at);
				ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
				ImageIO.write(img, "JPG", imgBytes);
				stream.clear();
				stream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
				stream.put(PdfName.TYPE, PdfName.XOBJECT);
				stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
				stream.put(key, value);
				stream.put(PdfName.FILTER, PdfName.DCTDECODE);
				stream.put(PdfName.WIDTH, new PdfNumber(width));
				stream.put(PdfName.HEIGHT, new PdfNumber(height));
				stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
				stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
			}
		}
		// Save altered PDF
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

		stamper.close();
		reader.close();

		// File (or directory) with old name
		File file = new File(src);

		// File (or directory) with new name
		File file2 = new File(dest);

		if (file.exists())
			file.delete();

		// Rename file (or directory)
		boolean success = file2.renameTo(file);

		if (!success) {
			// File was not successfully renamed
			System.err.println("File was not successfully renamed!!");
		}
	}

	/**
	 * Main method.
	 *
	 * @param args no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		// createPdf(RESULT);
		String src = "C:/Users/g802940/Downloads/iText/260-M-120-20231118-Z007715.pdf";
		String dest = src + "_" + "new";
		String binaryStr = new String(
				Files.readAllBytes(Paths.get("C:/Users/g802940/Downloads/iText/260-M-120-20231118-Z007714.pdf")),
				StandardCharsets.UTF_8);
		byte[] str = Base64.getEncoder().encode(binaryStr.getBytes());
		System.out.println(binaryStr);
		
		new ResizeImage().manipulatePdf(src, dest);

	}

}