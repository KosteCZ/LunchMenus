package cz.koscak.jan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.koscak.jan.model.MenuItem;
import cz.koscak.jan.model.RestaurantMenu;

public class PreferencesHandler {
	
	private static final String PREFERENCE_MEAT = "meat";
	
	private static final String DIRECTORY_INPUT = "in";
	
	private static Map<String, List<String>> mapOfPreferences = new HashMap<>();
	
	public static void loadPreferences() {
		
		mapOfPreferences.put(PREFERENCE_MEAT, loadPreference(PREFERENCE_MEAT));
		
	}

	private static List<String> loadPreference(String name) {
		
		BufferedReader reader = null;;
		List<String> lines = new ArrayList<String>();
		
		try {
			
			reader = new BufferedReader(new FileReader(DIRECTORY_INPUT + "/" + name + ".txt"));
			String line = null;
			while((line = reader.readLine()) != null) {
				line = line.replace("*", "");
			    lines.add(line);
			    //System.out.println(line);
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("ERROR during loading preference: '" + name + "'." + "\n"
					+ "File not found." + "\n"
					+ e.getMessage());
		} catch (IOException e) {
			System.err.println("ERROR during loading preference: '" + name + "'." + "\n"
					+ "Input error." + "\n"
					+ e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println("ERROR during loading preferences: '" + name + "'." + "\n"
						+ "Closing file error." + "\n"
						+ e.getMessage());
			}	
		}
		
		return lines;
		
	}
	
	public static void processPreferences2(RestaurantMenu menu) {
		
		if (!mapOfPreferences.isEmpty()) {
			
			for (String preference : mapOfPreferences.keySet()) {
				
				for (MenuItem menuItem : menu.getListOfSoups()) {
					boolean containsMeat = processLine(menuItem.getName(), mapOfPreferences.get(preference));
					System.out.println(containsMeat + " - " + menuItem.getName() /*+ " - " + menuItem.getPrice()*/);
				}
				
				for (MenuItem menuItem : menu.getListOfMeals()) {
					boolean containsMeat = processLine(menuItem.getName(), mapOfPreferences.get(preference));
					System.out.println(containsMeat + " - " + menuItem.getName() + " - " + menuItem.getPrice());
				}
		
			}
			
		}
	
	}

	public static String processPreferencesIntoHTML(RestaurantMenu menu) {
		
		StringBuilder result = new StringBuilder();
		
		result.append("<h2>" + menu.getName() + "</h2>" + "\n");
		
		if (!mapOfPreferences.isEmpty()) {
			
			for (String preference : mapOfPreferences.keySet()) {
				
				for (MenuItem menuItem : menu.getListOfSoups()) {
					if (menuItem.getPrice() != null && menuItem.getPrice() != "") {
						processMeatLine(menuItem, preference, result, menuItem.getName() + " - " + menuItem.getPrice());
					} else {
						processMeatLine(menuItem, preference, result, menuItem.getName());
					}
				}
				
				result.append("<br>" + "\n");
				
				for (MenuItem menuItem : menu.getListOfMeals()) {
					processMeatLine(menuItem, preference, result, menuItem.getName() + " - " + menuItem.getPrice());
				}
		
			}
			
		}
	
		return result.toString();
		
	}
	
	private static void processMeatLine(MenuItem menuItem, String preference, StringBuilder result, String text) {
		
		boolean containsMeat = processLine(text, mapOfPreferences.get(preference));
		
		if (!containsMeat) {
			result.append("<span style=\"background-color:#ffd966;\">");
		}
		
		result.append(text);
		
		if (!containsMeat) {
			result.append("</span>");
		}
		
		result.append("<br>" + "\n");
		
	}

	public static String processPreferences(String text) {
		
		if (!mapOfPreferences.isEmpty()) {
			
			text = processPreference(PREFERENCE_MEAT, text);
			
		}
		
		return text;
		
	}
	
	private static String processPreference(String preference, String text) {

		List<String> listOfPreferences = mapOfPreferences.get(preference);
		
		text = findLogicalUnit(text, listOfPreferences); 
		
		return text;
		
	}
	
	private static String findLogicalUnit(String source, List<String> listOfPreferences) {
		
		System.out.println("Processing MEAT !!!");
		
		String startingElement = null;
		String endingElement = null;
		
		if (source.contains("<div")) {
			startingElement = "<div";
			endingElement = "</div>";
		} else if (source.contains("<td")) {
			startingElement = "<td";
			endingElement = "</td>";
		}
		
		if (startingElement != null && endingElement != null) {
			
			System.out.println("startingElement: " + startingElement);
			
			int indexOfStartingElement = source.indexOf(startingElement) + startingElement.length();
			indexOfStartingElement = indexOfStartingElement + source.substring(indexOfStartingElement).indexOf(">") + ">".length();
			int indexOfEndingElement = source.indexOf(endingElement);
			
			String sourceLine = source.substring(indexOfStartingElement, indexOfEndingElement);
			System.out.println(sourceLine);
			
			if (processLine(sourceLine, listOfPreferences)) {
			
				source = source.substring(0, indexOfStartingElement) + "<b>" + source.substring(indexOfStartingElement, source.length());
				indexOfEndingElement = indexOfEndingElement + "<b>".length();
				source = source.substring(0, indexOfEndingElement) + "</b>" + source.substring(indexOfEndingElement, source.length());
				
			}
			//String sourcePart = source.substring(0, source.indexOf(endingElement));

			//String sourcePart = source.substring(source.indexOf(startingElement) + startingElement.length());
		}
		
		System.out.println("");
		
		/*String result = source.substring(source.indexOf(startingElement) + startingElement.length());
		
		result = result.substring(0, result.indexOf(endingElement));*/
		
		return source;
			
	}
	
	private static boolean processLine(String text, List<String> listOfPreferences) {
		
		boolean containsSubstring = false;
		
		for (String preference : listOfPreferences) {
			if (text.toLowerCase().contains(preference.toLowerCase())) {
				containsSubstring = true;
			}
		}
		
		return containsSubstring;
		
	}
	
}
