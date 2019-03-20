package com.sandisk;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.sandisk.SendMail;

public class Funcheap {

	Document doc;
	Element doc1 = new Element(Tag.valueOf("table"), "");
	boolean south = false;
	boolean popular = false;
	
	
	public void popular() throws IOException {

		doc1.append("<h2> Popular Events </h2>");
		String file = "/tmp/funcheap";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\funcheap.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});


			doc = Jsoup.connect("http://sf.funcheap.com/").get();		
			Elements popular = doc.select("div#sidebar-top ol li");		
			for(Element n : popular){
				
				String primKey = n.text();
//				String link = n.select("a").attr("href");
				String link = "http://www.google.com/calendar/event?location=428+11th+Street,+San+Francisco,+CA&action=TEMPLATE&sprop=name%3AModern+Web;&sprop=website%3Ahttps%3A%2F%2Fwww.meetup.com%2Fmodernweb%2Fevents%2F257967875&details=For+full+details%2C+including+the+address%2C+and+to+RSVP+see%3A+https%3A%2F%2Fwww.meetup.com%2Fmodernweb%2Fevents%2F257967875%0A%0AHappy+New+Year%21%0A%0AWe%27re+super+excited+to+have+you+at+our+next+Modern+Web+%2B+GDGSV+%2B+Angular+MTV+event%21...&text=4th+Annual+National+Tater+Tot+Day+|+SoMa+StrEat+Food&dates=20190202T023000Z%2F20190202T050000Z";
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
				Element td = tr.appendElement("td");
				
				Element a = new Element(Tag.valueOf("a"), "");
				a.attr("href",link).attr("style","text-decoration:none;");			
				a.appendElement("h4").appendText(primKey);
				
				td.appendChild(a);
				
				out.append(primKey + "\n");
			}
	
		
		out.close();
	}
	
	public void southbay() throws IOException {

		doc1.append("<h2> Southbay </h2>");
		String file = "/tmp/funcheap";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\funcheap.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});


			doc = Jsoup.connect("http://sf.funcheap.com/category/location/san-francisco-bay-area/south-bay/").get();		
			Elements popular = doc.select("div.tanbox.left");		
			for(Element n : popular){
				
				String primKey = n.select("span a").text();
				String link = n.select("span a").attr("href");
				String when = n.select("div.meta.archive-meta").text().replaceAll("\\|(.*?)", "");
				if(primKey.length() < 1){
					continue;
				}

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
				this.south = true;
				Element tr = doc1.appendElement("tr");
				Element td = tr.appendElement("td");
				
				Element a = new Element(Tag.valueOf("a"), "");
				a.attr("href",link).attr("style","text-decoration:none;");			
				a.appendElement("h4").appendText( when + " : ").appendText(primKey);
				
				td.appendChild(a);
				
				out.append(primKey + "\n");
			}
	
		
		out.close();
	}
	
	
	
	public void fileCheck() throws IOException{
		String file = "/tmp/funcheap";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\funcheap.txt";
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
		southbay();

		if(this.popular == true || this.south == true) {
			s.sendFromGMail("SF Fun Events : " + c.getTime().toString(), doc1.toString(), "SF Fun Cheap Events");
		}
		
//		if (doc1.children().size() > 0) {
//			s.sendFromGMail("SF Fun Events : " + c.getTime().toString(), doc1.toString());
//		}
		
		doc1.empty();

		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
//		while(true){
			Funcheap fp = new Funcheap();
			fp.sendMail();						
//			Thread.sleep(21600000);

//		}
	}
}



