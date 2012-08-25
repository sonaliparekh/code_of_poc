// main program
import java.io.File;
import java.io.FileInputStream;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

import methods.parsing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class first
{
	public static void main(String[] args) throws Exception 
	{
		String name;
		parsing P=new parsing();
		doc_file doc=new doc_file();
		docx_file docx=new docx_file();
		pdf_file pdf=new pdf_file();
		rtf_file rtf=new rtf_file();
		name="C:\\Users\\DELL\\Dropbox\\Sample Resumes\\aasma_shaikh__2.00_yrs.doc";
		
		if(name.endsWith(".doc")==true)
			doc.main_method(name,P);
		else if(name.endsWith(".docx")==true)
			docx.main_method(name,P);
		else if(name.endsWith(".pdf")==true)
			pdf.main_method(name,P);
		else if(name.endsWith(".rtf")==true)
			rtf.main_method(name,P);
		else
			System.out.println("File having wrong format!");
	}
}
// for .doc file.
class doc_file
{
	void main_method(String name,parsing P)
	{
		File file = null;
		String[] fileData=new String[300];
		WordExtractor extractor = null ;
		try 
		{
			file = new File(name);
			FileInputStream fis=new FileInputStream(file.getAbsolutePath());
				HWPFDocument document=new HWPFDocument(fis);
				extractor = new WordExtractor(document);
				//String [] fileData = extractor.getParagraphText();
				String temp=extractor.getText();
				fileData=temp.split("[\r\n]+");
				P.start(fileData);
			
		}
		catch(Exception exep){
			exep.printStackTrace();
		}
	}
}

// for .docx file
class docx_file
{
	void main_method(String name,parsing P)
	{
		try 
		{
	    	String[] fileData=new String[300];
	    	XWPFDocument docx = new XWPFDocument(OPCPackage.openOrCreate(new File(name)));  
	    	XWPFWordExtractor wx = new XWPFWordExtractor(docx);  
	    	String text = wx.getText();  
	    	fileData=text.split("[\n\r]+");
			P.start(fileData);
		}
		catch(Exception exep){
			exep.printStackTrace();
		}
	}
}
// for .pdf file
class pdf_file
{
	void main_method(String name,parsing P)
	{
		try 
		{
			String[] fileData=new String[300];
			File myFile = new File(name);
	        PDDocument pdDoc = null;
	        pdDoc = PDDocument.load(myFile);
            PDFTextStripper pdf = new PDFTextStripper();
            String output = pdf.getText(pdDoc);
	    	fileData=output.split("[\n\r]+");
			P.start(fileData);
		}
		catch(Exception exep){
			exep.printStackTrace();
		}
	}
}
//for .rtf file
class rtf_file
{
	void main_method(String name,parsing P)
	{
		try 
		{
			String[] fileData=new String[300];
			FileInputStream stream = new FileInputStream(name);  
	        RTFEditorKit kit = new RTFEditorKit();  
	        Document doc = kit.createDefaultDocument();  
	        kit.read(stream, doc, 0);  
	   
	        String plainText = doc.getText(0, doc.getLength());    
	        fileData=plainText.split("[\n\r]+");
			P.start(fileData);
		}
		catch(Exception exep){
			exep.printStackTrace();
		}
	}
}
