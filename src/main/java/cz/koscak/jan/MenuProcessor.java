package cz.koscak.jan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.koscak.jan.model.MenuItem;
import cz.koscak.jan.model.RestaurantMenu;

public class MenuProcessor {
	
	private static final String DIRECTORY_OUTPUT = "out";
	
	private List<RestaurantMenu> listOfMenus = new ArrayList<>();
	
	public List<RestaurantMenu> processAllRestaurants() {
		
		listOfMenus = new ArrayList<>();
		
		processPadowetz();
		
		processCharlies();
		
		//processMamut();
		
		processGourmet();
		
		//processNaTahu();
		
		return listOfMenus;
		
	}
	
	public String processAllRestaurantsOld() {
		
		StringBuilder content = new StringBuilder();
	
		content.append("<html><body>\n");
		
		content.append(showPadowetz());
		
		//String text = "<div class=\"loc_datum\">Nabídka platná pro čtvrtek 16. 1. 2020 <a href=\"poledni-menu/tisk.php?den=1579192969\" target=\"tisk\" class=\"tiskni\"><img src=\"images/icons/print_on.png\" title=\"Tisk menu poledniho menu\"></a></div><h5 class='t_Polevky'>Polévky<img src='../ikony/Polevky.png'></h5><table class='jidelnicek' id='t_Polevky'><tr height=35 id=\"r_Polevky0\" class=\"dvojklik\"><td class=\"porce0 r_Polevky0\">0,2l</td><td class=\"nazev0 r_Polevky0\" >Hovězí vývar s petrželkovou zavářkou (1,3,7)</td><td class=\"cena0 r_Polevky0\" style=\"display: none;\">0</td></tr><tr height=35 id=\"r_Polevky1\" class=\"dvojklik\"><td class=\"porce1 r_Polevky1\">0,2l</td><td class=\"nazev1 r_Polevky1\" >Hrachová s krutony (1,3,7)</td><td class=\"cena1 r_Polevky1\" style=\"display: none;\">0</td></tr></table><h5 class='t_Hlavni-chod'>Hlavní chod<img src='../ikony/Hlavni-chod.png'></h5><table class='jidelnicek' id='t_Hlavni-chod'><tr height=35 id=\"r_Hlavni-chod0\" class=\"dvojklik\"><td class=\"porce0 r_Hlavni-chod0\">150g</td><td class=\"nazev0 r_Hlavni-chod0\" >Vepřové výpečky, dušené tuřanské zelí, houskový knedlík (1,3,7)</td><td class=\"cena0 r_Hlavni-chod0\" >99Kč</td></tr><tr height=35 id=\"r_Hlavni-chod1\" class=\"dvojklik\"><td class=\"porce1 r_Hlavni-chod1\">150g</td><td class=\"nazev1 r_Hlavni-chod1\" >Kuřecí kung-pao, jasmínová rýže (1,3,7,8,12)</td><td class=\"cena1 r_Hlavni-chod1\" >109Kč</td></tr><tr height=35 id=\"r_Hlavni-chod2\" class=\"dvojklik\"><td class=\"porce0 r_Hlavni-chod2\">120g</td><td class=\"nazev0 r_Hlavni-chod2\" >Smažený sýr, hranolky, tatarská omáčka (1,3,7)</td><td class=\"cena0 r_Hlavni-chod2\" >119Kč</td></tr><tr height=35 id=\"r_Hlavni-chod3\" class=\"dvojklik\"><td class=\"porce1 r_Hlavni-chod3\">300g</td><td class=\"nazev1 r_Hlavni-chod3\" >Cizrnový salát s grilovanou paprikou a tuňákem (1,4,7)</td><td class=\"cena1 r_Hlavni-chod3\" >129Kč</td></tr><tr height=35 id=\"r_Hlavni-chod4\" class=\"dvojklik\"><td class=\"porce0 r_Hlavni-chod4\">180g</td><td class=\"nazev0 r_Hlavni-chod4\" >MINUTKA: Steak z lososa, grilovaná zelenina, rozpečená bageta (1,3,4,7)</td><td class=\"cena0 r_Hlavni-chod4\" >169Kč</td></tr><tr height=35 id=\"r_Hlavni-chod5\"></tr></table></div>";
		//System.out.println(excludeAAndImgTags(text));
		
		content.append(showCharlies());
		
		content.append(showMamut());
		
		content.append(showAnnapurna());
		
		content.append("\n</body></html>\n");
		
		return content.toString();

	}
	
	private int getDayOfWeek() {
		
		Date date = new Date();
		//DateFormat format2 = new SimpleDateFormat("EEEE"); 
		DateFormat format2 = new SimpleDateFormat("u"); 
		String finalDay = format2.format(date);
		return Integer.valueOf(finalDay);
				
	}
	
	private String showMamut() {
		
		try {
			
			String url = "https://www.mamut-pub.cz/";
			
			String pageContent = loadPage(url);
		    
			int dayOfWeek = getDayOfWeek();
			String day = null;
			switch(dayOfWeek) {
				case 1:
					day = "Ponděl&iacute;";
					break;
				case 2:
					day = "&Uacute;ter&yacute;";
					break;
				case 3:
					day = "Středa";
					break;
				case 4:
					day = "Čtvrtek";
					break;
				case 5:
					day = "P&aacute;tek";
					break;
			}
			if (day != null) {
				return "<h1>Mamut</h1>" + findContentIncludingEelements(pageContent, "<div>" + day + ":<span style=\"white-space:pre\">", "<div>&nbsp;</div>");
			} else {
				return "<h1>Mamut</h1>" + "<p>Error. Invalid day.</p>";
			}
		
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Mamut");
			throw e;
		}
	
	}

	private void processPadowetz() {

		try {
			
			String url = "http://www.restaurant-padowetz.cz/poledni-menu.htm";
			
			String pageContent = loadPage(url);
			
			int dayOfWeek = getDayOfWeek();
			String day = null;
			switch(dayOfWeek) {
				case 1:
					day = "pondělí";
					break;
				case 2:
					day = "úterý";
					break;
				case 3:
					day = "středu"; // "středa"
					break;
				case 4:
					day = "čtvrtek";
					break;
				case 5:
					day = "pátek";
					break;
			}
			
			if (day != null) {
				String result = excludeAAndImgTags(findContentIncludingEelements(pageContent, "<div class=\"loc_datum\">Nabídka platná pro " + day, "<tr height=35 id=\"r_Hlavni-chod5\""));
				//System.out.println(result);
				
				createMenuForPadowetz(result);
							
			} else {
				System.err.println("ERROR: Padowetz - Invalid day");
				throw new RuntimeException("ERROR: Padowetz - Invalid day");
			}
		
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Padowetz");
			throw e;
		}
		
	}
	
	private void createMenuForPadowetz(String result) {
		
		RestaurantMenu menu = new RestaurantMenu("Padowetz");
		listOfMenus.add(menu);
		
		result = processPadowetz(menu, result, true);
		result = processPadowetz(menu, result, true);

		result = processPadowetz(menu, result, false);
		result = processPadowetz(menu, result, false);
		result = processPadowetz(menu, result, false);
		result = processPadowetz(menu, result, false);
		result = processPadowetz(menu, result, false);
		
	}

	private String processPadowetz(RestaurantMenu menu, String result, boolean soup) {
			
		String subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));
		subResult = subResult.substring(subResult.indexOf(">") + 1);
		//System.out.println(subResult);
		int index = result.indexOf("</tr>") + 5;
		result = result.substring(index);
		
		String subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>"));
		String soupName = subSubResult;
		//System.out.println(subSubResult);
		int tempIndex = subResult.indexOf("</td>") + 5;
		subResult = subResult.substring(tempIndex);
		
		subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>"));
		soupName = soupName + " " + subSubResult;
		//System.out.println(subSubResult);
		tempIndex = subResult.indexOf("</td>") + 5;
		subResult = subResult.substring(tempIndex);
		
		subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>")).trim();
		if (subSubResult == null || "".equals(subSubResult) || "0".equals(subSubResult) || "-".equals(subSubResult)) {
			subSubResult = "";
		}
		MenuItem menuItem = new MenuItem();
		menuItem.setName(soupName);
		menuItem.setPrice(subSubResult);
		if (soup) {
			menu.addSoup(menuItem);
		} else {
			menu.addMeal(menuItem);
		}
		//System.out.println(subSubResult);
		
		//System.out.println("RESULT: " + result);
		//System.out.println();
		
		return result;
		
	}
	
	private void processCharlies() {
		
		try {
		
			String url = "https://www.charliessquare.cz/menu/";
			
			String pageContent = loadPage(url);
		    		
			//System.out.println(findContentIncludingEelements(pageContent, "<table class=\"menu-one-day\">", "</table>"));
		
			String result = "<h1>Charlies Square</h1>" + findContentIncludingEelements(pageContent, "<table class=\"menu-one-day\">", "</table>");
	
			//System.out.println(result);
				
			createMenuForCharlies(result);			
			
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Charlies");
			throw e;
		}
		
	}
	
	private void createMenuForCharlies(String result) {
		
		RestaurantMenu menu = new RestaurantMenu("Charlies");
		listOfMenus.add(menu);
		
		//System.out.println();
		//System.out.println("========== Charlies ===========");
		//System.out.println(result);
		//System.out.println();
		
		result = processCharlies(menu, result, true);

		result = processCharlies(menu, result, false);
		result = processCharlies(menu, result, false);
		result = processCharlies(menu, result, false);
		
	}

	private String processCharlies(RestaurantMenu menu, String result, boolean soup) {
		
		String subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));
		//System.out.println("? " + subResult);
		while (!subResult.contains("<td")) {
			subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));	
			result = result.substring(result.indexOf("</tr>") + 5);
			//System.out.println("? " + subResult);
		}
		//String subResult = result.substring(result.indexOf("<tr") + 4, result.indexOf("</tr>"));
		subResult = subResult.substring(subResult.indexOf(">") + 1);
		//System.out.println(subResult);
		int index = result.indexOf("</tr>") + 5;
		result = result.substring(index);
		
		String subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>"));
		String soupName = subSubResult;
		//System.out.println(subSubResult);
		int tempIndex = subResult.indexOf("</td>") + 5;
		subResult = subResult.substring(tempIndex);
		
		subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>")).trim();
		if (subSubResult == null || "".equals(subSubResult) || "0".equals(subSubResult) || "-".equals(subSubResult)) {
			subSubResult = "";
		}
		MenuItem menuItem = new MenuItem();
		menuItem.setName(soupName);
		menuItem.setPrice(subSubResult);
		if (soup) {
			menu.addSoup(menuItem);
		} else {
			menu.addMeal(menuItem);
		}
		//System.out.println(subSubResult);
		
		//System.out.println("RESULT: " + result);
		//System.out.println();
		
		return result;
		
	}
	
	private void processMamut() {

		try {
			
			String url = "https://www.mamut-pub.cz/";
			
			String pageContent = loadPage(url);
		    
			int dayOfWeek = getDayOfWeek();
			String day = null;
			switch(dayOfWeek) {
				case 1:
					day = "Ponděl&iacute;";
					break;
				case 2:
					day = "&Uacute;ter&yacute;";
					break;
				case 3:
					day = "Středa";
					break;
				case 4:
					day = "Čtvrtek";
					break;
				case 5:
					day = "P&aacute;tek";
					break;
			}
			if (day != null) {
				String result = "<h1>Mamut</h1>" + findContentIncludingEelements(pageContent, "<div>" + day + ":<span style=\"white-space:pre\">", "<div>&nbsp;</div>");
				
				//System.out.println(result);
				
				createMenuForMamut(result);
				
			} else {
				throw new RuntimeException("ERROR: Mamut - Invalid day.");
			}
		
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Mamut");
			throw e;
		}
			
	}
	
	private void createMenuForMamut(String result) {
		
		RestaurantMenu menu = new RestaurantMenu("Mamut");
		listOfMenus.add(menu);
		
		//System.out.println();
		//System.out.println("========== Mamut ===========");
		//System.out.println(result);
		//System.out.println();
		
		result = processMamut(menu, result, true);

		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false);
		result = processMamut(menu, result, false); // because potential meal on 2 rows
		
	}
	
	private String processMamut(RestaurantMenu menu, String result, boolean isSoup) {
		
		if (result != null && !result.isEmpty() && !result.trim().startsWith("</div>")) {
		
			String subResult = result.substring(result.indexOf("<div>") + 5, result.indexOf("</div>"));
			//System.out.println("? " + subResult);
			
			if (!subResult.isEmpty() && (subResult.trim() != "") && !subResult.equals("&nbsp;")) {
			
				/*while (!subResult.contains("<div>")) {
					subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));	
					result = result.substring(result.indexOf("</tr>") + 5);
					System.out.println("? " + subResult);
				}*/
				//String subResult = result.substring(result.indexOf("<tr") + 4, result.indexOf("</tr>"));
				
				if (isSoup) {
					subResult = subResult.substring(subResult.indexOf("</span>") + "</span>".length());
				}
				
				//System.out.println(subResult);
				
				String potentialPrefix = subResult.substring(0, 3);
				if (potentialPrefix.contains(")")) {
					subResult = subResult.substring(2).trim();
				}
				
				result = result.substring(result.indexOf("</div>") + "</div>".length());
				
				/*int index = result.indexOf("</tr>") + 5;
				result = result.substring(index);
				
				String subSubResult = result.substring(result.indexOf("<div>") + 5, result.indexOf("</div>"));
				String soupName = subSubResult;
				System.out.println(subSubResult);
				int tempIndex = subResult.indexOf("</div>") + 5;
				subResult = subResult.substring(tempIndex);
				
				subSubResult = subResult.substring(subResult.indexOf("<div>") + 5);
				subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>")).trim();
				if (subSubResult == null || "".equals(subSubResult) || "0".equals(subSubResult) || "-".equals(subSubResult)) {
					subSubResult = "";
				}*/
				
				MenuItem menuItem = new MenuItem();
				menuItem.setName(subResult.trim());
				menuItem.setPrice("");
				
				if (isSoup) {
					
					menu.addSoup(menuItem);
					
				} else {
					
					menu.addMeal(menuItem);
					
					String potentialPrice = subResult.substring(subResult.length() - 8, subResult.length());
					if (potentialPrice.contains("Kč")) {
						menuItem.setPrice(potentialPrice);
						menuItem.setName(subResult.substring(0, subResult.length() - 9).trim());
					} else {
						menuItem.setPrice("119,- Kč");
					}
					
				}
				
				//System.out.println("RESULT: " + result);
				//System.out.println();
			
			}
		
		}
		
		return result;
		
	}
	
	private String showPadowetz() {
				
		try {
			
			String url = "http://www.restaurant-padowetz.cz/poledni-menu.htm";
			
			String pageContent = loadPage(url);
		    
		    //System.out.println(pageContent);
			
			int dayOfWeek = getDayOfWeek();
			String day = null;
			switch(dayOfWeek) {
				case 1:
					day = "pondělí";
					break;
				case 2:
					day = "úterý";
					break;
				case 3:
					day = "středu"; // "středa"
					break;
				case 4:
					day = "čtvrtek";
					break;
				case 5:
					day = "pátek";
					break;
			}
			
			//if (day != null) {
			//	System.out.println(findContentIncludingEelements(pageContent, "<div class=\"loc_datum\">Nabídka platná pro " + day, "</table><h5 class='t_Napoje-k-menu'>" /*"</h5>"*/) + "</h5></div>");
			//} else {
			//	System.out.println("<h1>Mamut</h1>" + "<p>Error. Invalid day.</p>");
			//}
			
			if (day != null) {
				return "<h1>Padowetz</h1>" + excludeAAndImgTags(findContentIncludingEelements(pageContent, "<div class=\"loc_datum\">Nabídka platná pro " + day, "<tr height=35 id=\"r_Hlavni-chod5\"")) + "></tr></table></div>" + "\n";
			} else {
				return "<h1>Padowetz</h1>" + "<p>Error. Invalid day.</p>" + "\n";
			}
		
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Padowetz");
			throw e;
		}
		
	}
	
	private String showCharlies() {
		
		try {
		
			String url = "https://www.charliessquare.cz/menu/";
			
			String pageContent = loadPage(url);
		    		
			//System.out.println(findContentIncludingEelements(pageContent, "<table class=\"menu-one-day\">", "</table>"));
		
			return "<h1>Charlies Square</h1>" + findContentIncludingEelements(pageContent, "<table class=\"menu-one-day\">", "</table>");
	
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Charlies");
			throw e;
		}
		
	}

	private void processGourmet() {
		
		try {
			
			String url = "http://uvankovky.gourmetrestaurant.cz/";
			
			String pageContent = loadPage(url);
			
			String result = "<h1>Gourmet</h1>" + findContentIncludingEelements(pageContent, "<tbody>", "</tbody>");
			
			//System.out.println(result);
				
			createMenuForGourmet(result);			
			
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Gourmet");
			throw e;
		}

	}
	
	private void createMenuForGourmet(String result) {
		
		RestaurantMenu menu = new RestaurantMenu("Gourmet");
		listOfMenus.add(menu);
		
		//System.out.println();
		//System.out.println("========== Gourmet ===========");
		//System.out.println(result);
		//System.out.println();
		
		result = processGourmet(menu, result, true);

		result = processGourmet(menu, result, false);
		result = processGourmet(menu, result, false);
		result = processGourmet(menu, result, false);
		result = processGourmet(menu, result, false);
		
	}

	private String processGourmet(RestaurantMenu menu, String result, boolean soup) {
		
		String subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));
		
		//System.out.println("? " + subResult);
		while (!subResult.contains("<td")) {
			subResult = result.substring(result.indexOf("<tr") + 3, result.indexOf("</tr>"));	
			result = result.substring(result.indexOf("</tr>") + 5);
			//System.out.println("? " + subResult);
		}
		//String subResult = result.substring(result.indexOf("<tr") + 4, result.indexOf("</tr>"));
		subResult = subResult.substring(subResult.indexOf(">") + 1);
		//System.out.println(subResult);
		int index = result.indexOf("</tr>") + 5;
		result = result.substring(index);
		
		String subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>"));
		//System.out.println(subSubResult);
		int tempIndex = subResult.indexOf("</td>") + 5;
		subResult = subResult.substring(tempIndex);
		
		subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>"));
		String soupName = subSubResult;
		//System.out.println(subSubResult);
		tempIndex = subResult.indexOf("</td>") + 5;
		subResult = subResult.substring(tempIndex);
		
		subSubResult = subResult.substring(subResult.indexOf("<td") + 3);
		subSubResult = subSubResult.substring(subSubResult.indexOf(">") + 1, subSubResult.indexOf("</td>")).trim();
		if (subSubResult == null || "".equals(subSubResult) || "0".equals(subSubResult) || "-".equals(subSubResult)) {
			subSubResult = "";
		}
		MenuItem menuItem = new MenuItem();
		menuItem.setName(soupName);
		menuItem.setPrice(subSubResult);
		if (soup) {
			menu.addSoup(menuItem);
		} else {
			menu.addMeal(menuItem);
		}
		//System.out.println(subSubResult);
		
		//System.out.println("RESULT: " + result);
		//System.out.println();
		
		return result;
		
	}
	
	private void processNaTahu() {
		
		try {
			
			String url = "https://www.na-tahu.cz/";
			
			String pageContent = loadPage(url);
			
			int dayOfWeek = getDayOfWeek();
			String day = null;
			switch(dayOfWeek) {
				case 1:
					day = "PONDĚL&Iacute;";
					break;
				case 2:
					day = "&Uacute;TER&Yacute;";
					break;
				case 3:
					day = "STŘEDA";
					break;
				case 4:
					day = "ČTVRTEK";
					break;
				case 5:
					day = "P&Aacute;TEK";
					break;
			}

			String result = findContent(pageContent, day, "</p>");
			
			//System.out.println("RESULT: " + result);
				
			createMenuForNaTahu(result);		
			
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("ERROR: Na Tahu");
			throw e;
		}

	}
	
	private void createMenuForNaTahu(String result) {
		
		RestaurantMenu menu = new RestaurantMenu("Na Tahu");
		listOfMenus.add(menu);
		
		//System.out.println();
		//System.out.println("========== Na Tahu ===========");
		//System.out.println(result);
		//System.out.println();
		
		result = result.substring(result.indexOf("-") + 2);
		
		result = processNaTahu(menu, result, true);

		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		result = processNaTahu(menu, result, false);
		
	}


	private String processNaTahu(RestaurantMenu menu, String result, boolean soup) {
		
		String subResult = result;
		
		if (result.contains("<br />")) {
			subResult = result.substring(0, result.indexOf("<br />")).trim();
		}
		
		if (soup || subResult.contains(")")) {
			
			if (subResult.contains(")")) {
				subResult = subResult.substring(subResult.indexOf(")") + 2);
			}
			
			String mealName = subResult;
			String price = "";
			if (!soup) {
				price = "109,- Kč";
			}
			
			if (subResult.contains("Kč")) {
				int indexOfLastDashSeparator = subResult.lastIndexOf("-");
				String tempText = subResult.substring(0, indexOfLastDashSeparator - 1);
				//System.out.println("TEMP: '" + tempText + "'");
				int indexOfLastWhiteSpace = tempText.lastIndexOf(" ");
				mealName = subResult.substring(0, indexOfLastWhiteSpace);
				//System.out.println("NAME: '" + mealName + "'");
				price = subResult.substring(indexOfLastWhiteSpace + 1, subResult.length());
				//System.out.println("PRICE: '" + price + "'");
			}
			
			MenuItem menuItem = new MenuItem();
			menuItem.setName(mealName);
			menuItem.setPrice(price);
			if (soup) {
				menu.addSoup(menuItem);
			} else {
				menu.addMeal(menuItem);
			}
			//System.out.println(subSubResult);
			
			//System.out.println("RESULT: " + result);
			//System.out.println();
			
			return result.substring(result.indexOf("<br />") + "<br />".length());
			
		} else {
			
			return "";
		
		}
		
	}
	
	private String showAnnapurna() {

		return "\n" + "<h1>Annapurna</h1>" + "\n" + "<embed src=\"http://indicka-restaurace-annapurna.cz/images/tydenijidelnicek/menu.pdf\" style=\"width:100%; height:100%;\" frameborder=\"1\">\r\n";
		
	}
	
	private String excludeAAndImgTags(String text) {
		
		text = excludeTag(text, "<a", "</a>");
		text = excludeTag(text, "<img", ">");
		return text;
		
	}

	private String excludeTag(String text, String startingTag, String endingTag) {
		while (text.indexOf(startingTag) > -1) {
			int indexStart = text.indexOf(startingTag);
			if (indexStart > -1) {
				//System.out.println(text);
				String tempText = text.substring(indexStart, text.length());
				//System.out.println(tempText);
				int indexEnd = tempText.indexOf(endingTag);
				if (indexEnd > -1) {
					String textStart = text.substring(0, indexStart);
					String textEnd = tempText.substring(indexEnd + endingTag.length(), tempText.length());
					text = textStart + textEnd;
				} else {
					return text;
				}
			}
		}
		return text;
	}
	
	static void saveToFile(String text) {
		
		createDirectoryIfNotExists(DIRECTORY_OUTPUT);
		
		try (PrintStream out = new PrintStream(new FileOutputStream(DIRECTORY_OUTPUT + "/output.htm"))) {
		    out.print(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File(DIRECTORY_OUTPUT + "/output.htm");
		System.out.println(file.getAbsolutePath());
		
	}
	
	private static void createDirectoryIfNotExists(String directoryName) {
		File directory = new File(directoryName);
	    if (! directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents use directory.mkdirs(); here instead.
	    }
	}
	
	private static String findContentIncludingEelements(String source, String startingElement, String endingElement) {
		
		System.out.println(startingElement);
		
		String result = source.substring(source.indexOf(startingElement));
		
		result = result.substring(0, result.indexOf(endingElement) + endingElement.length());
		
		// result = PreferencesHandler.processPreferences(result);
		
		return result;
			
	}
	
	private static String findContent(String source, String startingElement, String endingElement) {
			
		String result = source.substring(source.indexOf(startingElement) + startingElement.length());
		
		result = result.substring(0, result.indexOf(endingElement));
		
		return result;
			
	}
	
	private static String loadPage(String url) {
	    
		String resultPageContent = null;
		URLConnection connection;
	    
		try {
			
			connection = new URL(url).openConnection();
		    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		    connection.connect();
		
		    BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
		
		    StringBuilder sb = new StringBuilder();
		    String line;
		    
		    while ((line = r.readLine()) != null) {
		        sb.append(line);
		    }
		    
		    resultPageContent = sb.toString();
		    
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultPageContent;
    
	}
}
