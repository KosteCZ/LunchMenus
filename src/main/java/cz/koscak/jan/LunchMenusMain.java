package cz.koscak.jan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import cz.koscak.jan.model.MenuItem;
import cz.koscak.jan.model.RestaurantMenu;
import cz.koscak.jan.slack.SlackMessage;
import cz.koscak.jan.slack.SlackUtils;

public class LunchMenusMain {

	private List<RestaurantMenu> listOfMenus = new ArrayList<>();
	
	public static void main(String[] args) {
		
		LunchMenusMain lunchMenus = new LunchMenusMain();
		
		//lunchMenus.processAllPages();
		
		lunchMenus.processAllPages2();
		
		//sendSlackMessage();
		
	}

	private void processAllPages2() {
		
		PreferencesHandler.loadPreferences();
		
		MenuProcessor menuProcessor = new MenuProcessor();
		
		listOfMenus = menuProcessor.processAllRestaurants();
				
		/*System.out.println(listOfMenus.get(0));
		System.out.println(listOfMenus.get(1));
		System.out.println(listOfMenus.get(2));*/
		
		for (int i = 0; i < listOfMenus.size(); i++) {
			System.out.println(listOfMenus.get(i));	
		}
		System.out.println();
		
		/*System.out.println();
		PreferencesHandler.processPreferencesIntoHTML(listOfMenus.get(0));
		System.out.println();
		PreferencesHandler.processPreferencesIntoHTML(listOfMenus.get(1));
		System.out.println();
		PreferencesHandler.processPreferencesIntoHTML(listOfMenus.get(2));*/
		
		printHTML();
		
		String messageForSlack = getTextForSlack();
		
		System.out.println(messageForSlack);
		
		// sendSlackMessage(messageForSlack); // <--
		
	}
	
	private String getTextForSlack() {
		
		StringBuilder text = new StringBuilder();
		
		for (int i = 0; i < listOfMenus.size(); i++) {
			text.append("*" + listOfMenus.get(i).getName() + "*" + "\n");
			for (MenuItem soup : listOfMenus.get(i).getListOfSoups()) {
				text.append("- " + org.apache.commons.lang3.StringUtils.stripAccents(StringEscapeUtils.unescapeHtml(soup.getNameAndPrice())) + "\n");
			}
			for (MenuItem meal : listOfMenus.get(i).getListOfMeals()) {
				text.append("- " + org.apache.commons.lang3.StringUtils.stripAccents(StringEscapeUtils.unescapeHtml(meal.getNameAndPrice())) + "\n");
			}
			text.append("\n");
		}
		
		return text.toString();
		
	}
	
	private void printHTML() {
		
		StringBuilder html = new StringBuilder();
		html.append(generateHTMLStart());
		
		for (int i = 0; i < listOfMenus.size(); i++) {
			html.append(PreferencesHandler.processPreferencesIntoHTML(listOfMenus.get(i)));
			html.append("\n");
		}
		
		html.append(generateHTMLEnd());
		// System.out.println(html.toString()); // HTML output <--
		
		MenuProcessor.saveToFile(html.toString());
		
	}
	
	private String generateHTMLStart() {
		
		return "<html>" + "\n" + "<body>" + "\n";
		
	}
	
	private String generateHTMLEnd() {
		
		return "</body>" + "\n" + "</html>" + "\n";
		
	}
	
	/*@Deprecated
	private void processAllPages() {
		
		// PreferencesHandler.loadPreferences();
		
		MenuProcessor menuProcessor = new MenuProcessor();
		
		String result = menuProcessor.processAllRestaurantsOld();
		
		MenuProcessor.saveToFile(result);
		
		System.out.println("Finished.");
		
	}*/
	
	private static void sendSlackMessage(String text) {
		
	      SlackMessage slackMessage = SlackMessage.builder()
	        //.channel("lunch")
	    	//.username("technical-user-1")
	    	//.text("just testing")
	        //.icon_emoji(":twice:")
	        .username("Lunch Menu Robot")
	        .text(text)
	        .build();
	      SlackUtils.sendMessage(slackMessage);
	      
	      System.out.println();
	      System.out.println("Slack message sent.");
	      System.out.println();
	      
	}
	
}
