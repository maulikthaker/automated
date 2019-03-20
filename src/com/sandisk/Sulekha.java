package com.sandisk;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.sandisk.SendMail;

public class Sulekha {

	
	Element doc1 = new Element(Tag.valueOf("table"), "");
	boolean south = false;
	boolean upcoming = false;
	
	
	private String getdate(String input2) {
		switch(input2) {
        case "january":
        case "jan":
            input2 = "01";
        break;

        case "febuary":
        case "feb":
            input2 = "02";
        break;

        case "march":
        case "mar":
            input2 = "03";
        break;

        case "april":
        case "apr":
            input2 = "04";
        break;

        case "may":
            input2 = "05";
        break;

        case "june":
        case "jun":
            input2 = "06";
        break;

        case "july":
        case "jul":
            input2 = "07";
        break;

        case "august":
        case "aug":
            input2 = "08";
        break;

        case "september":
        case "sep":
        case "sept":
            input2 = "09";
        break;

        case "october":
        case "oct":
            input2 = "10";
        break;

        case "november":
        case "nov":
            input2 = "11";
        break;

        case "december":
        case "dec":
            input2 = "12";
        break;
        }
		return input2;
	}
	
	public void upcoming() throws IOException, ParseException {

		doc1.append("<h2> UpComing Events </h2>");
		String file = "/tmp/slkh";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\sulekha.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});


			Document doc = Jsoup.connect("http://events.sulekha.com/bay-area").get();		
			
//			$('article.tktwrp div.tktdecs h3 a').each(function(){
//				console.log($(this).attr("title"))
//				console.log($(this).closest("article.tktwrp").find("div.tktdatetme span.tktmonth").text())
//				console.log($(this).closest("article.tktwrp").find("div.tktdatetme span.tkttime").text())
//				
//			})
			
			Elements popular = doc.select("article.tktwrp");		
			for(Element n : popular){
				
				
				
				
//				
				String primKey = n.select("div.tktdecs h3 a").attr("title");
				String month = n.select("div.tktdatetme span.tktmonth").text();
				String time = n.select("div.tktdatetme span.tkttime").text();
				String day = n.select("div.tktdatetme span.tktdate").text();
				String event = primKey + " : "+ month + ", " + time + " " + day;
				
				SimpleDateFormat displayFormat = new SimpleDateFormat("HHmm");
				SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
			    Date date = parseFormat.parse(time);
			    
				String fromtime = "T" + displayFormat.format(date) + "00";
				String fromdate = "2019"+ getdate(month.split("\\s")[0].toLowerCase()) + month.split("\\s")[1]  + fromtime;
				String todate = "2019"+ getdate(month.split("\\s")[0].toLowerCase()) + month.split("\\s")[1]  +"T235959";
				String callink = "https://calendar.google.com/calendar/r/eventedit?" +
									"&text=" + primKey +	
									"&dates=" + fromdate + "/" + todate;									 
//									"location=Garage+Boston+-+20+Linden+Street+-+Allston,+MA+02134";
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
				
				this.upcoming = true;				
				Element tr = doc1.appendElement("tr");
				Element td = tr.appendElement("td");
				
				Element a = new Element(Tag.valueOf("a"), "");
				a.appendText(event + " | ");
				
				
				Element cal = new Element(Tag.valueOf("a"),"link");
				cal.attr("href",callink);
				cal.text("Add to Cal");
				
				
				a.appendChild(cal);
				td.appendChild(a);
				
				
				out.append(primKey + "\n");
			}
	
		
		out.close();
	}
	
	
	public void southbay() throws IOException {

		doc1.append("<h2> Southbay </h2>");
		String file = "/tmp/slkh";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\sulekha.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});

			Document doc;
			doc = Jsoup.connect("http://events.sulekha.com/bay-area").get();		
			Elements popular = doc.select("aside.event-container");		
			for(Element n : popular){
				
				
				
				String primKey = n.select("section.event-bd h3 a").attr("title");
				String href = "http://events.sulekha.com" + n.select("section.event-bd h3 a").attr("href");
				String img = n.select("figure img").first().attr("src");
				
				String month = n.select("section.event-bd div.caldr.ftlt span.month").text();
				String day = n.select("section.event-bd div.caldr.ftlt span.day").text();
				String time = n.select("section.event-bd span.times").text();
				String event = primKey + " : "+ month + " " + day + "" + time; 
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
				
				SimpleDateFormat displayFormat = new SimpleDateFormat("HHmm");
				SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			    Date date = new Date();;
				try {
					date = parseFormat.parse(time.substring(5,13));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
				String fromtime = "T" + displayFormat.format(date) + "00";
				String fromdate = "2019"+ getdate(month.toLowerCase()) + day  + fromtime;
				String todate = "2019"+ getdate(month.toLowerCase()) + day  +"T235959";
				String callink = "https://calendar.google.com/calendar/r/eventedit?" +
									"&text=" + primKey +	
									"&dates=" + fromdate + "/" + todate;									 
//									"location=Garage+Boston+-+20+Linden+Street+-+Allston,+MA+02134";

				
				
				Element tr = doc1.appendElement("tr");
				Element td = tr.appendElement("td");

				Element cal = new Element(Tag.valueOf("a"),"link");
				cal.attr("href",callink);
				cal.text("Add to Cal | ");

				
				Element a = new Element(Tag.valueOf("a"), "");
				a.attr("href",href).attr("style","text-decoration:none;");
				a.appendChild(cal);
				a.appendElement("p").appendText(event);

				

				
				a.appendElement("img").attr("src", img).attr("width", "300px");
				a.appendElement("br");
				
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
		
	
	
	public void sendMail() throws IOException, ParseException{
		SendMail s = new SendMail();
		Calendar c = Calendar.getInstance();
		
		
		/////////////////////////////////////////////////////////////////////////////////////
		//						ADD DEALS THAT YOU WANT
		/////////////////////////////////////////////////////////////////////////////////////
		fileCheck();
		upcoming();
		southbay();

		if(this.south == true || this.upcoming == true) {
			s.sendFromGMail("Sulekha Events : " + c.getTime().toString(), doc1.toString(), "Personal Sulekha");
		}
		
//		if (doc1.children().size() > 0) {
//			s.sendFromGMail("SF Fun Events : " + c.getTime().toString(), doc1.toString());
//		}
		
		doc1.empty();

		
	}

	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
//		while(true){
			Sulekha fp = new Sulekha();
			fp.sendMail();						
//			Thread.sleep(21600000);

//		}
	}
}



