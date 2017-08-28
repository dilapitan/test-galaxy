package irri.ojt;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.JavascriptExecutor;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.awt.Desktop.Action;
import java.io.*;
import javax.xml.xpath.*; 
import org.w3c.dom.Attr;
import java.util.*;

public class Galaxy {

	public static int globalTestTagCount;
	public static int globalValuesPerTestTag;

	public static void main(String[] args) {
		
		// set up of Selenium Web Driver with Firefox
		//System.setProperty("webdriver.gecko.driver", "/home/dom/Downloads/geckodriver");
		//FirefoxOptions options = new FirefoxOptions();
		//options.setBinary("/usr/bin/firefox");
		//WebDriver driver = new FirefoxDriver(options);
		
		System.setProperty("webdriver.chrome.driver", "/home/dom/Downloads/chromedriver");
		driver = new ChromeDriver();
		
		driver.get("http://localhost:8080"); // ip address of Galaxy Project  
		
		System.exit(0);
		
		
		/*// clicks 'Get Data'
		WebElement getDataLabel = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/div[2]/div[1]/a"));
		getDataLabel.click();
		
		// clicks 'Upload File'
		WebElement uploadFileLabel = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/div[2]/div[2]/div[3]/div/a"));
		uploadFileLabel.click();
	
		
		// sets the type of the data that will be uploaded to 'tabular'
		driver.findElement(By.xpath("//*[@id='s2id_autogen1']/a")).click();
		WebElement field = driver.findElement(By.xpath("//*[@id='s2id_autogen2_search']"));
		field.sendKeys("tabular");
		field.sendKeys(Keys.DOWN);
		field.sendKeys(Keys.ENTER);
			
		System.out.println("a");
		// upload a file
		//driver.findElement(By.xpath("//*[@id='btn-local']/span")).sendKeys("/home/dom/Desktop/1.bed");
		//driver.findElement(By.cssSelector("#btn-local")).sendKeys("/home/dom/Desktop/1.bed");
		
		
		System.out.println("b");
		System.exit(0);
		// click 'Start'
		WebElement startButton = driver.findElement(By.xpath("//*[@id='btn-start']"));
		startButton.click();
		
		sleep(10000);
		
		// click 'Close'
		WebElement closeButton = driver.findElement(By.xpath("//*[@id='btn-close']"));
		closeButton.click();
		

		// automation of values 
		
		sleep(10000);
		
		// reclicks 'Get Data'
		getDataLabel.click();
		
		
		
		// 'Text Manipulation' label
		WebElement textManipulationLabel = driver.findElement(By.xpath("//*[@id='title_textutil']/a"));
		textManipulationLabel.click();
		
		ArrayList<String> selectorList = new ArrayList<String>(Arrays.asList("cat1", "ChangeCase", "Convert", "createInterval", "Cut1", "addValue", "Show beginning1", "mergeCols1", "Paste1", "random_lines1", "Remove beginning1", "secure_hash_message_digest", "Show tail1", "trimmer", "wc_gnu"));
		
		
		
		
		// 'Filter and Sort' label
			WebElement filterAndSortLabel = driver.findElement(By.xpath("//*[@id='title_filter']/a"));
		filterAndSortLabel.click();
		System.exit(0);
		
		
		//ArrayList<String> selectorList = new ArrayList<String>(Arrays.asList("secure_hash_message_digest"));
		
		// parsing of XML files and getting the values
		try {
			System.out.println("\nStarting to read xml files...");
			
			File dir = new File("/home/dom/Desktop/all-tools/text-man-xml-files/");	// getting the directory
			File[] listOfFiles = dir.listFiles();				// getting the files in the directory
			Arrays.sort(listOfFiles); 							// sort the files in the directory alphabetically
				
			int toolCount = 1;
			int listOfFilesSize = selectorList.size(); 
			
			ArrayList<WebElement> historyPanel = new ArrayList<WebElement>(); //driver.findElements(By.cssSelector("ul.list-items")); // container of the history panel (results/right side of Galaxy website)
			
			// set up for file writing
			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

			writer.println("\n---RESULTS--- \n");
			for (int a = 0; a < listOfFilesSize; a++) { // traverse the whole directory
				File path = listOfFiles[a];
				if (path.isFile() && path.getName().endsWith(".xml")) { 	// only reads files having .xml as extension
					String preSelector = selectorList.get(a); 				// gets the selector in the list of selectors
					String replacedSpace = preSelector.replace(" ", ".");	
					String mainSelector = "a." + replacedSpace; 			// include <a> and dot (.) as for using cssSelector in Selenium WebDriver
					
					WebElement randomLabel = driver.findElement(By.cssSelector(mainSelector)); 	// getting the label with dynamic selector
				
					ArrayList<Inputs> inputsContainer = new ArrayList<Inputs>();
					inputsContainer = retrieveValues(path, toolCount);
					
					String testType;
					String testValue;
					int i = 0;
					int gt = globalTestTagCount; 
					int gtCounter = 1;
					
					writer.println("TOOL NUMBER: " + toolCount);
					String toolName = randomLabel.getText();
					writer.println("TOOL NAME: " + toolName);
					
					while (gt != 0) { 	// for those xml files with multiple <test>
						
						int testTagCountTemp = 1;
						int temp = globalValuesPerTestTag/2;
						System.out.println("test no: " + gtCounter);
						
						
						writer.println("\ttest no: " + testTagCountTemp);
						randomLabel.click();
						while (temp != 0) {	
							
							testType = inputsContainer.get(i).type;
							testValue = inputsContainer.get(i).expectedValue;

							//	Dynamic automation of 'values based on 'text' (note: this can also be applied for Integer, Float, since all are only <input> that will receive a 'text'
					
							if (testType.equals("text")) { 			// input field 	| xpath has: field-uid (may change "text" to "Integer"/"Float" depending on the <inputs> in the xml file
								
								 * 	For Input fields
								 
								
								try {
									WebElement inputField = driver.findElement(By.xpath("//*[starts-with(@id, 'field-uid')]"));
									inputField.sendKeys(Keys.CONTROL + "a"); // selecting all the values
									inputField.sendKeys(Keys.DELETE); 		 // clearing the values
									inputField.sendKeys(testValue); 		 // putting the test value
									inputField.sendKeys(Keys.TAB);
								}
								catch (NoSuchElementException e) {
									System.out.println("No element.");
								}
							}
							
							// Dynamic automation of 'values based on 'data'
							
							else if (testType.equals("data")) { 	// upload data 	| ignore since file is already uploaded in GET DATA
								
								 * 	For 'Upload data' fields
								 
								
								System.out.println("");	
							}
							else if (testType.equals("select")) { 	
								try {
									
									
									 *	for Checkboxes fields
									 
									List<WebElement> list = driver.findElements(By.cssSelector("div.ui-option")); // retrieving the values of the whole checkboxes | has this kind of xpath	
									int indexPath = 0; // to be used later for xpath
									int listSize = list.size(); 
									
									// finding the given test value in the list of texts from checkboxes
									for (int t = 0; t < listSize; t++) {
										WebElement wb = list.get(t);
										if (wb.getText().equals(testValue)) { 
											indexPath = (t+1); // retrieving the index to be passed in the xpath
											break;
										}
									}
									
									String origPath = "//*[starts-with(@id, 'field-uid')]/div[3]/div[xxx]/label"; 	// 'xxx' is the variable that will be replaced by the indexPath
									String ii = Integer.toString(indexPath); 										// making the indexPath, String for the replace()
									String newPath = origPath.replace("xxx", ii); 									// replacing 'xxx' with the actual index of the testValue
									
									WebElement wb = driver.findElement(By.xpath(newPath)); // checking the checkbox with the given test value
									wb.click(); 				
									
									
								}
								catch (NoSuchElementException e) {
									
									 * 	For Dropdown fields
									 
									WebElement dropDown = driver.findElement(By.xpath("//*[starts-with(@id, 'field-uid')]/div[3]")); 	// finding the dropdown element | has this kind of xpath
									dropDown.sendKeys(testValue); 																		// providing the test value
									dropDown.sendKeys(Keys.TAB);
								}
								
							}
							else if (testType.equals("boolean")) { // yes or no 	| xpath has: /label[1] or /label[2]
								try {
									
									 * 	For yes/no fields
									 
									if (testValue.equals("true")) { 		// yes
										WebElement booleanButtonYes = driver.findElement(By.xpath("//*[starts-with(@id, 'field-uid')]/div[3]/label[1]"));
										booleanButtonYes.click();
									}
									else if (testValue.equals("false")) {  	// no
										WebElement booleanButtonNo = driver.findElement(By.xpath("//*[starts-with(@id, 'field-uid')]/div[3]/label[2]"));
										booleanButtonNo.click();
									}
								}
								catch (NoSuchElementException e) {
									System.out.println("No element.");
								}
							}

							// other conditions here (for other input fields)

							temp--;
							i++;
						}
						
						// click 'Execute' after all the automation
						//System.out.println("Executing the tool...");
						WebElement executeButton = driver.findElement(By.xpath("//*[@id='execute']"));
						executeButton.click();
						
						
						 * 		Checking of result
						 
						
						sleep(7000);
						
						try {
							WebElement outputDiv = driver.findElement(By.xpath("//*[starts-with(@id, 'dataset-')]"));
							historyPanel.add(outputDiv);
						}
						catch (NoSuchElementException e) {
							System.out.println("No element.");
						}
						
						
						int historyPanelSize = historyPanel.size();
						
						// accessing the last element ("top of stack")
						WebElement currentOutputDiv = historyPanel.get(historyPanelSize - 1);
						String backgroundColor = currentOutputDiv.getCssValue("background-color");
						//System.out.println("color: " + "'" + backgroundColor + "'");
						
						
						 * 		COLORS:
						 * 	green color (okay) 	:	rgb(176, 241, 176)
						 * 	red color	(error)	:	rgb(249, 199, 197)
						 
						
						WebElement divTitle = driver.findElement(By.cssSelector("span.name"));
						if (backgroundColor.equals("rgb(176, 241, 176)")) {
							try {
								sleep(5000);
								divTitle.click();
							
								WebElement result = driver.findElement(By.cssSelector("span.value"));
								String resultText = result.getText();
								
								if (resultText.equals("error") || resultText.equals("no peek")) {
									System.out.println("note: " + resultText);
									writer.println("\tnote: " + resultText + "\n\n");
								}
								else {
									System.out.println("okay");
									writer.println("\tstatus: okay\n\n");
								}

							}
							catch (NoSuchElementException e){
								System.out.println("No element.");
							}
						}
						else if (backgroundColor.equals("rgb(249, 199, 197)")) {
							System.out.println("error");
							writer.println("\tstatus: error");	
							try {
								// retrieving the error
								
								sleep(5000);
								divTitle.click();
								
									
								WebElement iIcon = driver.findElement(By.cssSelector("a.params-btn"));
								Actions actions = new Actions(driver);
								actions.moveToElement(iIcon).click().perform();
								
								
								WebElement errorDiv = driver.findElement(By.cssSelector("div.job-error-text"));								
								String errorMessage = errorDiv.getText();
								System.out.println("Error message: " + "\n" + errorMessage);
								writer.println("\tnotes: " + errorMessage + "\n\n");
								
								
							}
							catch (NoSuchElementException e) {
								System.out.println("No element.");
							}
							catch (ElementNotInteractableException e) {
								System.out.println("Element not interactable.");
							}
						}

						sleep(10000);
						gtCounter++;
						gt--;
						testTagCountTemp++;
					}

				}
				System.out.println("Tool: " + toolCount + " ended.");
				toolCount++;
				sleep(10000);
			}
			System.out.println("All Text Manipulation tools tested.");			
			writer.close(); // close output file
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

	public static void sleep(int time) { 		// for sleeping/pausing program | sleep() is used to ensure that the data is already uploaded for use
		try {
			Thread.sleep(time);
		}
		catch (Exception e) {}
	}
	
	public static ArrayList<Inputs> retrieveValues(File fileName, int toolCount) {

		ArrayList<String> valueTestContainer   = new ArrayList<String>();	// value attributes under <test>
		ArrayList<String> valueInputsContainer = new ArrayList<String>();	// value attributes under <inputs>
		ArrayList<Inputs> inputsContainer = new ArrayList<Inputs>(); 		// container of Input classes

		try {
			
			/*
			 * 		SOURCES: 
			 * 	https://stackoverflow.com/questions/2460592/xpath-how-to-get-all-the-attribute-names-and-values-of-an-element
			 * 	https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
			 * 	https://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java
			 */
			
			// set up
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileName);
			doc.getDocumentElement().normalize();
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
	       	// <TEST>

	       	// counting the <test>
			NodeList testTagList = doc.getElementsByTagName("test");  // from: https://www.mkyong.com/java/how-to-count-xml-elements-in-java-dom-parser/
			int testTagCount = testTagList.getLength();
			globalTestTagCount = testTagCount;

	       	// using xpath to get to <param> via traversing the nodes
			XPathExpression expr = xpath.compile("/tool/tests/test/param/@*");
			NodeList nlTest = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

	       	// traversing the <param> under <test> and retrieving its attributes
	        int nlTestLength = nlTest.getLength();
	        for (int i = 0; i < nlTestLength; i++) {
	        	Attr atTest = (Attr) nlTest.item(i);
	        	String valueTest = atTest.getValue();

	        	valueTestContainer.add(valueTest);
	        }

	        // <INPUTS>
	        XPathExpression expr2 = xpath.compile("/tool/inputs/param/@*");
			NodeList nlInputs = (NodeList) expr2.evaluate(doc, XPathConstants.NODESET);

	        //traversing the <param> under <input> and cross checking its attributes in the value container under <test>
	        int nlInputsLength = nlInputs.getLength();
		    for (int t = 0; t < testTagCount; t++) {
		        for (int i = 0; i < nlInputsLength; i++) {
		        	Attr atInputs = (Attr) nlInputs.item(i);
		        	String nameInputs = atInputs.getName();
		        	String valueInputs = atInputs.getValue();
		        	if (nameInputs.equals("name")) {		// checking the 'name' attribute
		        		valueInputsContainer.add(valueInputs);
		        	}
		        	else if (nameInputs.equals("type")) { 	// checking the 'type' attribute
		        		valueInputsContainer.add(valueInputs);
		        	}
		        }
		    }

	        /* 		Just a test. Actual is automation in the input fields using Selenium Web Driver.
	         *		Data that will be automated will come from the <test> / valueTestContainer 
	         *		via driver.sendKeys()
	         */		

	        int valuesPerTestTag = nlTestLength / testTagCount;
	        globalValuesPerTestTag = valuesPerTestTag; 
	       	int i = 0;
       		int ntl = nlTestLength;
   			int testTagCount2 = 1;
       		
       		while (ntl != 0) {

       			int paramCount = 1;
       			int temp = valuesPerTestTag;
       			int temp2 = temp;

       			//System.out.println("--- Test tag: " + testTagCount2 + "\n");
       			while (temp != 0) {

       				Inputs in = new Inputs();
       				if ( (valueInputsContainer.get(i)).equals((valueTestContainer.get(i))) ) {
		        		in.name = valueTestContainer.get(i);
		        		in.expectedValue = valueTestContainer.get(i+1);
		        		in.type = valueInputsContainer.get(i+1);
	
			        	paramCount++;
			        
			        	inputsContainer.add(in);
		        	}

		        	i += 2;
		        	temp -= 2;
       			}

       			ntl -= temp2;
       			testTagCount2++;
       		}
 		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return inputsContainer;
	}
}
