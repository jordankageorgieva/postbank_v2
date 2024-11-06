package com.postbank.currenciesBNB.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class BnbCurrenciesReadService {

	private final String bnbCurrenciesPoint = "https://www.bnb.bg/Statistics/StExternalSector/StExchangeRates/StERForeignCurrencies/index.htm?download=xml&search=&lang=BG";

	private final String LOCAL_FILE = "currencies.xml";

	public void connect() {

		try {
			saveFileInMyProject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	protected void saveFileInMyProject() throws IOException {

		URL url = new URL(bnbCurrenciesPoint);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		// Getting the response code
		int responsecode = conn.getResponseCode();

		if (responsecode != 200) {
			throw new RuntimeException("HttpResponseCode: " + responsecode);
		} else {

			String inline = "";
			Scanner scanner = new Scanner(url.openStream());

			// Write all the XML data into a string using a scanner
			while (scanner.hasNext()) {
				inline += scanner.nextLine();
			}

			// create file
			try {
				File myObj = new File(LOCAL_FILE);
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			// write to file
			try {
				FileWriter myWriter = new FileWriter(LOCAL_FILE);
				myWriter.write(inline);
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

			/*
			 * System.out.println("\nJSON data in string format");
			 * System.out.println(inline);
			 */

			// current file
			System.out.println(inline);

			// Close the scanner
			scanner.close();

		}
	}

	public Map<String, String> xmlCurrenciesParser() {
		// Get the Document Builder

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Map<String, String> curreciesMap = new HashMap<String, String>();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Get Document

			Document document = builder.parse(new File(LOCAL_FILE));

			// Normalize the xml structure
			document.getDocumentElement().normalize();

			// Get all the element by the tag name

			NodeList rowList = document.getElementsByTagName("ROW");

			for (int i = 0; i < rowList.getLength(); i++) {
				Node row = rowList.item(i);

				String cur = null;
				String curval = null;

				if (row.getNodeType() == Node.ELEMENT_NODE) {

					Element currenciesElement = (Element) row;
					// System.out.println("ROW data: " + laptopElement.getAttribute("CODE"));

					NodeList laptopDetails = row.getChildNodes();

					for (int j = 0; j < laptopDetails.getLength(); j++) {
						Node detail = laptopDetails.item(j);

						if (detail.getNodeType() == Node.ELEMENT_NODE) {
							Element detailElement = (Element) detail;

							if ("CODE".equals(detailElement.getTagName())) {
								cur = detailElement.getTextContent();
//	                            	  System.out.println("     " + detailElement.getTagName() + ": " + 
//	  	                            		detailElement.getTextContent());
							}

							if ("RATE".equals(detailElement.getTagName())) {

								curval = detailElement.getTextContent();
//	                            	 System.out.println("     " + detailElement.getTagName() + ": " + 
//	 	                            		detailElement.getTextContent());

							}

						}

						curreciesMap.put(cur, curval);

					}
				}

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Iterating HashMap through for loop
//            System.out.println("++++++++++++ final out data ++++++++++++++++++++");
//            curreciesMap.forEach(
//                    (key, value)
//                        -> System.out.println(key + " = " + value));

		return curreciesMap;
	}
}
