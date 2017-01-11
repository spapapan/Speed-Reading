package pkg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.SwingWorker;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLcontent extends SwingWorker {

	private Main main;
	private String url;
	
	public URLcontent(String URL,Main mainclass)
	{
		this.main = mainclass;
		this.url = URL; 
	}

	@Override
	protected Object doInBackground() throws Exception {

		String returndoc;
		if (url.substring(url.length()-3).equals("pdf"))
		{
			returndoc = getPDF(url);
		}
		else if (url.contains("wikipedia."))
		{
			returndoc = getWiki(url);
		}
		else
		{
			returndoc = "404";
		}
		
		main.startReadingFromAsync(returndoc,url);
		return null;
	}
	
	private String getWiki(String url)
	{
        String returndoc;
	    Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Elements paragraphs = doc.select(".mw-content-ltr p, .mw-content-ltr li");

	    Element firstParagraph = paragraphs.first();
	    Element lastParagraph = paragraphs.last();
	    Element p;
	    int i = 1;
	    p = firstParagraph;
	    returndoc = p.text();
	    while (p != lastParagraph) {
	        p = paragraphs.get(i);
	        returndoc = returndoc + " " + p.text();
	        i++;
	    }
		return returndoc;
	}
	
	private String getPDF(String url)
	{
		String gettext;
		downloadPDF(url);
	    PdfToUnicode();
	    gettext = readPDF(); 
	    delete();
	 
		return gettext;
	}
	
	private String readPDF(){
	 
		    PDDocument doc;
			try {
				doc = PDDocument.load(new File("read.pdf"));
				return new PDFTextStripper().getText(doc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 return null;
	}
	
	private void delete(){
		File f1 = new File("download.pdf");
		File f2 = new File("read.pdf");
		if (f1.exists()){
			f1.delete();
		}
		if (f2.exists()){
			f2.delete();
		}
	}

	private void downloadPDF(String pdfurl)
	{
		URL url;
		try {
			url = new URL(pdfurl);
			InputStream in = null;
			try {
				in = url.openStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Files.copy(in, Paths.get("download.pdf"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void PdfToUnicode()
	{
		
		File f = new File("download.pdf");
		try (PDDocument doc = PDDocument.load(f))
		{
		    for (int p = 0; p < doc.getNumberOfPages(); ++p)
		    {
		        PDPage page = doc.getPage(p);
		        PDResources res = page.getResources();
		        for (COSName fontName : res.getFontNames())
		        {
		            PDFont font = res.getFont(fontName);
		            COSBase encoding = font.getCOSObject().getDictionaryObject(COSName.ENCODING);
		            if (!COSName.IDENTITY_H.equals(encoding))
		            {
		                continue;
		            }
		            // get real name
		            String fname = font.getName();
		            int plus = fname.indexOf('+');
		            if (plus != -1)
		            {
		                fname = fname.substring(plus + 1);
		            }
		            if (font.getCOSObject().containsKey(COSName.TO_UNICODE))
		            {
		                continue;
		            }
		            //System.out.println("File '" + f.getName() + "', page " + (p + 1) + ", " + fontName.getName() + ", " + font.getName());
		            if (!fname.startsWith("Calibri-Bold"))
		            {
		                continue;
		            }
		            COSStream toUnicodeStream = new COSStream();
		            try (PrintWriter pw = new PrintWriter(toUnicodeStream.createOutputStream(COSName.FLATE_DECODE)))
		            {
		                // "9.10 Extraction of Text Content" in the PDF 32000 specification
		                pw.println ("/CIDInit /ProcSet findresource begin\n" +
		                        "12 dict begin\n" +
		                        "begincmap\n" +
		                        "/CIDSystemInfo\n" +
		                        "<< /Registry (Adobe)\n" +
		                        "/Ordering (UCS) /Supplement 0 >> def\n" +
		                        "/CMapName /Adobe-Identity-UCS def\n" +
		                        "/CMapType 2 def\n" +
		                        "1 begincodespacerange\n" +
		                        "<0000> <FFFF>\n" +
		                        "endcodespacerange\n" +
		                        "10 beginbfchar\n" + // number is count of entries
		                        "<0001><0020>\n" + // space
		                        "<0002><0041>\n" + // A
		                        "<0003><0042>\n" + // B
		                        "<0004><0044>\n" + // D
		                        "<0013><0065>\n" + // e
		                        "<0012><0064>\n" + // d
		                        "<0017><0069>\n" + // i
		                        "<001B><006E>\n" + // n
		                        "<0015><0067>\n" + // g
		                        "<0020><0075>\n" + // u
		                        "endbfchar\n" +
		                        "endcmap CMapName currentdict /CMap defineresource pop end end");
		            }
		            font.getCOSObject().setItem(COSName.TO_UNICODE, toUnicodeStream);
		        }
		    }
		    File f1 = new File("read.pdf");
		    doc.setAllSecurityToBeRemoved(true);
		    doc.save(f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
