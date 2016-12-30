package net.latin.server.utils.reports.constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.SimpleBookmark;

import net.latin.server.persistence.SpringUtils;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.fileTypes.Pdf;
import net.latin.server.utils.helpers.FileUtils;
import net.latin.server.utils.reports.visualizer.ReportViewerServlet;
import net.latin.server.utils.resources.ResourceFinder;


/**
 * Utilidades varias con pdfs
 * ver para ejemplos <code> http://1t3xt.info/examples/browse/ </code>
 * @author Axel Forgaral
 *
 */
public class PdfUtils {

	private static final Log LOG = LogFactory.getLog(PdfUtils.class);

	public static void Merge(List<Pdf> pdfs, String outFile) {

		try {
			int pageOffset = 0;
			ArrayList master = new ArrayList();
			Document document = null;
			PdfCopy writer = null;

			for (Pdf pdf : pdfs) {

				PdfReader reader = new PdfReader(pdf.getContent());
				reader.consolidateNamedDestinations();
				int n = reader.getNumberOfPages();
				List bookmarks = SimpleBookmark.getBookmark(reader);
				if (bookmarks != null) {
					if (pageOffset != 0)
						SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
					master.addAll(bookmarks);
				}

				if (pdfs.indexOf(pdf) == 0) {
					// step 1: creation of a document-object
					document = new Document(reader.getPageSizeWithRotation(1));
					// step 2: we create a writer that listens to the document
					writer = new PdfCopy(document, new FileOutputStream(outFile));
					// step 3: we open the document
					document.open();
				}
				pageOffset += n;
				// step 4: we add content
				PdfImportedPage page;
				for (int i = 0; i < n;) {
					++i;
					page = writer.getImportedPage(reader, i);
					writer.addPage(page);
				}
				PRAcroForm form = reader.getAcroForm();
				if (form != null)
					writer.copyAcroForm(reader);
			}
			if (!master.isEmpty())
				writer.setOutlines(master);
			// step 5: we close the document
			document.close();
			writer.close();

		} catch (Exception e) {
			LOG.error("No se pudo concatenar los PDFs", e);
		}
	}

	public static void MergeAndShow(List<Pdf> pdfs) {
		String tempDir = LnwGeneralConfig.getInstance().getReportsTestingOutputFolder() + "/";
		File folder = new File(tempDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		SpringUtils.getProperty("ReportsTestingOutputFolder");
		Random rnd = new Random();
		Long num = Math.abs(rnd.nextLong());
		String fileName = "tempPDF" + num + ".pdf";
		File file = new File(tempDir + fileName);
		Merge(pdfs, tempDir + fileName);
		ReportViewerServlet.setFileToShow(getPdf(tempDir + fileName));
		file.delete();
	}

	public static void addPageNumbers(String pdfPath) {
		try {
			String tempPath = pdfPath + "temp";
			copy(pdfPath, tempPath);
			PdfReader reader = new PdfReader(tempPath);
			int i = 0;
			int n = reader.getNumberOfPages();
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(pdfPath));
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

			float x;
			float y;
			float len = bf.getWidthPoint("Página " + i + " de " + n, 12);
			Document doc = new Document();

			while (i < n) {

				i++;
				x = reader.getPageSize(i).getWidth() - len - 50;
				// altura a la q va el texto
				y = 40;
				over = stamp.getOverContent(i);
				over.beginText();
				over.setFontAndSize(bf, 12);
				over.showTextAligned(Element.ALIGN_LEFT, "Página " + i + " de " + n, x, y, 0);
				over.endText();
			}
			stamp.close();
			delete(tempPath);
		} catch (Exception de) {
			LOG.error(de);
		}
	}

	private static void delete(String fileName) {
		File file = new File(fileName);
		FileInputStream fi;
		try {

			fi = new FileInputStream(file);
			int total = (int) file.length();
			byte[] b = new byte[total];
			fi.read(b);
			fi.close();
			file.delete();
			ReportViewerServlet.setFileToShow(new Pdf(fileName, b));
			file.delete();
		} catch (Exception e) {
			LOG.error("No se pudo abrir el archivo", e);
		}

	}

	public static void copy(String nombreFuente, String nombreDestino) {
		try {
			FileInputStream fis = new FileInputStream(nombreFuente);
			FileOutputStream fos = new FileOutputStream(nombreDestino);
			FileChannel canalFuente = fis.getChannel();
			FileChannel canalDestino = fos.getChannel();
			canalFuente.transferTo(0, canalFuente.size(), canalDestino);
			fis.close();
			fos.close();
		} catch (Exception e) {
			LOG.error("No se pudo abrir el archivo", e);
		}
	}

	/**
	 * Agrega en el centro de todas las paginas la imagen pasada por parametro y
	 * devuelve el pdf creado, con la rotacion especificada
	 * 
	 * @param pdfPath
	 * @param pathImagen
	 * @param rotationDegrees
	 *            cantidad de grados a rotar la imagen en sentido horario
	 */
	public static void stampImage(String pdfPath, String pathImagen, Integer rotationDegrees) {
		String tempPath = pdfPath + "temp";
		copy(pdfPath, tempPath);
		try {
			PdfReader reader = new PdfReader(tempPath);
			int n = reader.getNumberOfPages();
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(pdfPath));
			PdfContentByte under;

			Image img = Image.getInstance(ResourceFinder.getFile(pathImagen).getCanonicalPath());
			img.setRotationDegrees(rotationDegrees);
			// img.setTransparency(new int[]{0x5B,0x5D});

			float x;
			float y;
			for (int i = 1; i <= n; i++) {
				x = (reader.getPageSize(i).getWidth() - img.getWidth()) / 2;
				y = (reader.getPageSize(i).getHeight() - img.getHeight()) / 2;
				img.setAbsolutePosition(x, y);
				under = stamp.getUnderContent(i);
				under.addImage(img);
			}
			stamp.close();

		} catch (Exception de) {
			LOG.equals(de);
		}
	}

	/**
	 * Agrega en el centro de todas las paginas la imagen pasada por parametro y
	 * devuelve el pdf creado, sin rotar de la imagen
	 * 
	 * @param pdfPath
	 * @param pathImagen
	 * @return
	 */
	public static void stampImage(String pdfPath, String pathImagen) {

		stampImage(pdfPath, pathImagen, 0);

	}

	/**
	 * Genera un Pdf desde un path
	 * 
	 * @param path
	 * @return
	 */
	public static Pdf getPdf(String path) {
		File file = new File(path);
		FileInputStream fi;
		try {

			fi = new FileInputStream(file);
			int total = (int) file.length();
			byte[] b = new byte[total];
			fi.read(b);
			fi.close();
			file.delete();
			return new Pdf(path, b);
		} catch (Exception e) {
			LOG.error("No se pudo abrir el archivo", e);
			return null;
		}
	}

	/**
	 * Crea un Pdf en memoria, borra el archivo leido del disco, y lo muestra
	 * 
	 * @param fileName
	 */
	public static void showAndDelete(String fileName) {
		showAndDelete(fileName,fileName);
	}
	/**
	 * Crea un Pdf en memoria, borra el archivo leido del disco, y lo muestra
	 * 
	 * @param fileName: Nombre del archivo a leer
	 * @param downloadFileName: Nombre del archivo a utilizar en la descarga
	 */
	public static void showAndDelete(String fileName,String downloadFileName) {
		File file = new File(fileName);
		FileInputStream fi;
		try {
			
			fi = new FileInputStream(file);
			int total = (int) file.length();
			byte[] b = new byte[total];
			fi.read(b);
			fi.close();
			ReportViewerServlet.setFileToShow(new Pdf(downloadFileName, b));
			file.delete();
		} catch (Exception e) {
			LOG.error("No se pudo abrir el archivo", e);
		}
		
	}

	/**
	 * Agrega un texto centrado en helvetica
	 * 
	 * @param pdfPath
	 * @param text
	 * @param fontSize
	 * @param opacity
	 * @param rotationDegrees
	 */
	public static void addCenteredText(String pdfPath, String text, Integer fontSize, Float opacity,
			Integer rotationDegrees) {
		try {
			String tempPath = pdfPath + "temp";

			PdfGState gstate = new PdfGState();
			gstate.setFillOpacity(opacity);
			gstate.setStrokeOpacity(opacity);

			copy(pdfPath, tempPath);
			PdfReader reader = new PdfReader(tempPath);
			int n = reader.getNumberOfPages();
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(pdfPath));
			int i = 0;
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
			while (i < n) {
				i++;
				over = stamp.getOverContent(i);
				over.beginText();
				over.setFontAndSize(bf, fontSize);
				over.setTextRenderingMode(4);
				over.setGState(gstate);
				float x = (reader.getPageSize(i).getWidth() / 2);
				float y = reader.getPageSize(i).getHeight() / 2;
				y = (float) (y + fontSize * 1.5);
				over.showTextAligned(Element.ALIGN_CENTER, text, x, y, rotationDegrees);
				over.endText();
			}
			stamp.close();
			delete(tempPath);
		} catch (Exception de) {
			LOG.error(de);
		}
	}
	public static void addCenteredText(File pdf, String text, Integer fontSize, Float opacity,Integer rotationDegrees) {
		PdfStamper stamp = null;
		File tempFile = null;
		FileOutputStream os = null;
		try {
			tempFile = File.createTempFile("pdfTempForStamp", "");
			String tempPath = tempFile.getAbsolutePath();
			PdfGState gstate = new PdfGState();
			gstate.setFillOpacity(opacity);
			gstate.setStrokeOpacity(opacity);

			copy(pdf.getAbsolutePath(), tempPath);
			PdfReader reader = new PdfReader(tempPath);
			int n = reader.getNumberOfPages();
			os = new FileOutputStream(pdf);
			stamp = new PdfStamper(reader, os);
			int i = 0;
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1250, BaseFont.EMBEDDED);
			while (i < n) {
				i++;
				over = stamp.getOverContent(i);
				over.beginText();
				over.setFontAndSize(bf, fontSize);
				over.setTextRenderingMode(4);
				over.setGState(gstate);
				float x = (reader.getPageSize(i).getWidth() / 2);
				float y = reader.getPageSize(i).getHeight() / 2;
				y = (float) (y + fontSize * 1.5);
				over.showTextAligned(Element.ALIGN_CENTER, text, x, y, rotationDegrees);
				over.endText();
			}
			
		} catch (Exception de) {
			LOG.error(de);
		}finally{
			try {
				stamp.close();
			} catch (Exception e) {
				// ignore
			}
			IOUtils.closeQuietly(os);
			FileUtils.delete(tempFile);
		}
	}
}
