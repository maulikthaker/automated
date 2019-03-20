package com.sandisk;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.sandisk.SendMail;

public class flightdeal {

	
	Document doc;
	Element doc1 = new Element(Tag.valueOf("table"), "");
	boolean south = false;
	boolean popular = false;
	String file = flightdeal.getFile();
	
	public static String getFile(){		
		
		String file = "/tmp/fdeal";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\fdeal.txt";
		}
		return file;
		
	}
	
	public void popular() throws IOException {

		doc1.append("<h2> Flight Deal </h2>");
		FileWriter out = new FileWriter(file, true);
		

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});


			doc = Jsoup.connect("https://www.theflightdeal.com/category/flight-deals/sfo/").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();		
			Elements popular = doc.select("h1.post-title a");		
			for(Element n : popular){
				
				String primKey = n.text().replace("–", " | ").replace(". Roundtrip, including all Taxes","").replace("-"," | ").replace(":"," | ");
				
				
				
				String link = n.select("a").attr("href");
				Boolean exists = false;
				for (String s : stringArray) {
					if (s.contains(primKey)) {
						exists = true;
						break;
					}
				}

				if (exists == true) {
					continue;
				}

				this.popular = true;
				Element tr = doc1.appendElement("tr");
				
//				for(String s : primKey.split("\\|")) {
					Element td = tr.appendElement("td");
					Element a = new Element(Tag.valueOf("a"), "");
					a.attr("href",link).attr("style","text-decoration:none;");			
					a.appendElement("h4").appendText(primKey.split("\\|")[1]);
					System.out.println(primKey.split("\\|")[1]);
					td.appendChild(a);

					Element td1 = tr.appendElement("td");
					Element a1 = new Element(Tag.valueOf("a"), "");
					a1.attr("href",link).attr("style","text-decoration:none;");			
					a1.appendElement("h4").appendText(primKey.split("\\|")[primKey.split("\\|").length - 1]);
//					System.out.println(primKey.split("\\|")[primKey.split("\\|").length - 1]);
					td1.appendChild(a1);

//				}
				
				out.append(primKey + "\n");
			}
	
		
		out.close();
	}
	
	
	
	
	
	public void fileCheck() throws IOException{
		String file = "/tmp/fdeal";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\fdeal.txt";
		}
		File f = new File(file);
		if(!f.exists() && !f.isDirectory())
		{
		    f.createNewFile();
		}
		
			Calendar c = Calendar.getInstance();
			int day = c.get(Calendar.DAY_OF_MONTH);
		    if( day == 31){
		    	PrintWriter writer;
				try {
					writer = new PrintWriter(file);			    	
			    	writer.close();					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		    }
		    
		

	}
		
	
	
	public void sendMail() throws IOException{
		SendMail s = new SendMail();
		Calendar c = Calendar.getInstance();
		
		
		/////////////////////////////////////////////////////////////////////////////////////
		//						ADD DEALS THAT YOU WANT
		/////////////////////////////////////////////////////////////////////////////////////
		fileCheck();
		popular();
		

		if(this.popular == true || this.south == true) {
			s.sendFromGMail("Flight Deal : " + c.getTime().toString(), doc1.toString(),"Maulik Flight Deals");
		}
		
//		if (doc1.children().size() > 0) {
//			s.sendFromGMail("SF Fun Events : " + c.getTime().toString(), doc1.toString());
//		}
		
		doc1.empty();

		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
//		while(true){
			flightdeal fp = new flightdeal();
			fp.sendMail();						
//			Thread.sleep(21600000);

//		}
	}
}



