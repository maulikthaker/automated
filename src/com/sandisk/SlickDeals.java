package com.sandisk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class SlickDeals {

	Document doc;
	Element doc1 = new Element(Tag.valueOf("table"), "");
	
	
	public void popular() throws IOException {

		String file = "/tmp/pulse";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\slick.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});

		for (int pageNumber = 1; pageNumber < 3; pageNumber++){
//			try{
				doc = Jsoup.connect("http://slickdeals.net/deals/?page=" + pageNumber + "&sort=recent").get();
//			}catch(Exception e){
//				out.close();
//				return;
				
//			}
					
			Elements popular = doc.select("div.mainDealInfo");		
			for(Element n : popular){
				String img, link, text;
				img = n.select("a noscript img[src]").attr("src").replace("100x100/", "").replace("thumb", "attach").toString();
				if(img == ""){
					continue;
				}
				text = n.select("a").text().toString();
				link = n.select("a").attr("href").toString();

				String primKey = img.replaceAll("(.*?)\\/(\\d+)(\\.attach)$", "$2").toString();			
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
				
				Element tr = doc1.appendElement("tr");
				Element td = tr.appendElement("td");
				td.appendElement("h3").attr("style","color:blue").appendText(text);
				td.appendElement("br");
				td.appendElement("a").attr("href", "http://slickdeals.net" + link)
				.appendElement("img").attr("src", img).attr("width", "300px");
				
				out.append(primKey + "\n");
			}
		}
		
		out.close();
	}
	
	
	
	
	
	
	public void fpage() throws IOException{

		
		String file = "/tmp/pulse";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\slick.txt";
		}

		FileWriter out = new FileWriter(file, true);			

		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		String[] stringArray = stringList.toArray(new String[] {});

		
		
		doc = Jsoup.connect("http://slickdeals.net").get();
		Elements newsHeadlines = doc.select("div.itemImageLink");
		
		this.doc1.attr("width", "90%");
		for(Element n : newsHeadlines){
			

			String img;
			if(n.select("noscript").first() == null)
				continue;
			Document d = Jsoup.parse(n.select("noscript").first().html());
			String imgsrc = d.select("img").first().attr("src");
			
			if(imgsrc.replace("200x200/", "").replace("thumb", "attach").toString().contains("attach")){
				img = imgsrc.replace("200x200/", "").replace("thumb", "attach").toString();
//				System.out.println("This is Image " + img);
			}
			else{
				continue;
//				img = n.select("div.imageContainer noscript img.lazyimg[data-original]").attr("data-original").replace("200x200/", "").replace("thumb", "attach").toString();
			}			

			String primKey = img.replaceAll("(.*?)\\/(\\d+)(\\.attach)$", "$2").toString();
			
			boolean exists = false;

			for (String s : stringArray) {
				if (s.contains(primKey)) {
					exists = true;
					break;
				}
			}

			if (exists == true) {
				continue;
			}

			
			
			Element tr = doc1.appendElement("tr");
			Element td = tr.appendElement("td");
			td.appendElement("h3").attr("style","color:red").appendText("FrontPage : " + d.select("img").first().attr("title").replaceAll("NEW ", "").replaceAll("(\\d+\\s+\\d+)$", ""));
			td.appendElement("br");
			td.appendElement("a").attr("href", "http://slickdeals.net" + n.select("a.itemTitle").attr("href")).appendElement("img").attr("src", img).attr("width", "300px");
			
			out.append(primKey + "\n");
		}
		out.close();
	}

	public void sendMail() throws IOException{
		SendMail s = new SendMail();
		Calendar c = Calendar.getInstance();
		fileCheck();
		
		/////////////////////////////////////////////////////////////////////////////////////
		//						ADD DEALS THAT YOU WANT
		/////////////////////////////////////////////////////////////////////////////////////
		
		fpage();	
		popular();
	
		
		
		if (doc1.children().size() > 0) {
			s.sendFromGMail("Frontpage : " + c.getTime().toString(), doc1.toString(), "Personal Slick Deals");
//			SendMail.send("Frontpage : " + c.getTime().toString(), doc1.toString());
		}
		doc1.empty();

		
	}
	
	
	
	public void fileCheck() throws IOException{
		String file = "/tmp/pulse";
		if (System.getProperty("os.name").contains("Windows")) {
			file = "C:\\Users\\34087\\Documents\\junk\\demo\\JsoupDemo\\slick.txt";
		}
		File f = new File(file);
		if(!f.exists() && !f.isDirectory())
		{
		    f.createNewFile();
		}
		
			Calendar c = Calendar.getInstance();
			int day = c.get(Calendar.DAY_OF_MONTH);			
//		    if( day == 1 || day == 15){
			if(day == 31 && c.get(Calendar.DAY_OF_MONTH) == 11){
		    	PrintWriter writer;
				try {
					writer = new PrintWriter(file);			    	
			    	writer.close();					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		    }
		    
		

	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {

		
//		while(true){
			SlickDeals slick = new SlickDeals();
			slick.sendMail();
//			Integer randomNum = 10800000 + (int)(Math.random() * (14400000 - 10800000) + 1);			
//			Thread.sleep(randomNum);

//		}
	}
}





//public void fireDeal() throws IOException {
//
//	doc = Jsoup.connect("http://slickdeals.net").get();
//	Elements newsHeadlines = doc.select("div.firedeal");
//
//
//	Element doc1 = new Element(Tag.valueOf("table"), "");
//	doc1.attr("width", "90%");
//
//	Path filePath = new File(file).toPath();
//	Charset charset = Charset.defaultCharset();
//
//	List<String> stringList = Files.readAllLines(filePath, charset);
//	String[] stringArray = stringList.toArray(new String[] {});
//	FileWriter out = new FileWriter(file, true);
//
//	for (Element e : newsHeadlines) {
//
//		boolean exists = false;
//
//		for (String s : stringArray) {
//			if (s.contains(e.select("a.itemImageLink").attr("title"))) {
//				exists = true;
//				break;
//			}
//		}
//
//		if (exists == true) {
//			continue;
//		}
//
//		// Element n = e.select("a.itemImageLink").get(0);
//		Element tr = doc1.appendElement("tr");
//		Element td = tr.appendElement("td");
//		td.appendElement("h3").appendText(e.select("a.itemImageLink").attr("title"));
//		td.appendElement("br");
//
//		Element image = e.select("a.itemImageLink").first();
//		String imgLink;
//		if (image.select("div.imageContainer > img").attr("src").toString()
//				.contains("http://static.slickdealscdn.com/images/slickdeals/blank.gif")) {
//
//			imgLink = image.select("div.imageContainer").select("img").attr("data-original").replace("200x200/", "")
//					.replace("thumb", "attach").toString();
//		} else {
//			imgLink = image.select("div.imageContainer > img").attr("src").toString().replace("200x200/", "")
//					.replace("thumb", "attach").toString();
//		}
//
//		td.appendElement("a").attr("href", "http://slickdeals.net" + e.select("a.itemImageLink").attr("href"))
//				.appendElement("img").attr("src", imgLink).attr("width", "300px");
//		System.out.println(e.select("a.itemImageLink"));
//		out.append(e.select("a.itemImageLink").attr("src") + "\n");
//
//	}
//	out.close();
//
//	// System.out.println(doc1.toString());
//	Calendar c = Calendar.getInstance();
//	SendMail s = new SendMail();
//	if (doc1.children().size() > 0) {
//		try {
//			// s.sendfromEC2("Frontpage Fire : " + c.getTime().toString(),
//			// doc1.toString());
//			s.sendFromGMail("Frontpage Fire : " + c.getTime().toString(), doc1.toString());
//		} catch (Exception e1) {
//			System.out.println("Cannot call the function sendfromEC2 ");
//		}
//	}
//
//}