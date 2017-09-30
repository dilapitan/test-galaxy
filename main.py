from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException

import xml.etree.ElementTree as ET
import os
import glob
import time
import re

# XML parsing and retrieving values
def retrieveValues(file):
	root = ET.parse(file).getroot() 
	toolName = root.get('name') # getting the tool name

	'''
		Getting the values from <tests>
	'''
	
	testLength = len(root.findall('.//test')) # getting the number of <test>

	testParamValues = [] # array of values under <param> of <test>
	
	testParams = root.findall('.//test//param')
	
	for testParam in testParams:
		testNameText = testParam.get('name')	# get the value of 'name' attribute
		testValueText = testParam.get('value')	# get the value of 'value' attribute

		testParamValues.append(testNameText)
		testParamValues.append(testValueText)



	'''
		Getting the values from <input>
	'''

	inputsElement = root.findall('inputs') 
	inputsParams = root.findall('inputs//param') # getting the <param> under the <inputs>
	
	inputsParamsLength = len(inputsParams)

	inputsParamValues = []
	dataFormat = ""
	for testCount in range(0, testLength): # for catching multiple <test>
		for inputsParam in inputsParams:
			inputsNameText = inputsParam.get('name')
			inputsTypeText = inputsParam.get('type')

			data = inputsParam.get('format')
			if (data != None):
				dataFormat = data

			inputsParamValues.append(inputsNameText)
			inputsParamValues.append(inputsTypeText)
		
	# print(testParamValues)
	# print(inputsParamValues)

	''' 
		Object that will be passed for automation 
	'''

	class Values:
		def __init__(self, name, datatype, value):
			self.n = name
			self.dt = datatype
			self.v = value

	c = 0
	paramDouble = inputsParamsLength * 2
	d = paramDouble

	wholeValues = [] # values of all v
	for i in range(0, testLength):
		v = [] # values of single <test> 
		while (c != d):
			# object creation
			testObject = Values(testParamValues[c], inputsParamValues[c+1], testParamValues[c+1])
		
			v.append(testObject)
			c += 2
		d += paramDouble
		wholeValues.append(v)
	
	#print("retrieved!")
	return wholeValues, toolName, dataFormat # values for automation

def printMenu():
	print("--- TOOL TYPES FOR TESTING ---")
	print("[1] Convert formats")
	print("[2] Extract features")
	print("[3] Fetch Alignments")
	print("[4] Fetch Sequence")
	print("[5] Filter and Sort")
	print("[6] Graph Display Data")
	print("[7] Join Subtract and Group")
	print("[8] Statistics")
	print("[9] Text Man XML Files")
	print("[0] Exit Program")

	i = int(input("Choice: "))
	return i


# choice = printMenu()
# if (choice == 1):
# 	toolTypePath = "convert-formats"
# elif (choice == 2):
# 	toolTypePath = "extract-features"
# elif (choice == 3):
# 	toolTypePath = "fetch-alignments"
# elif (choice == 4):
# 	toolTypePath = "fetch-sequence"
# elif (choice == 5):
# 	toolTypePath = "filter-and-sort"
# elif (choice == 6):
# 	toolTypePath = "graph-display-data"
# elif (choice == 7):
# 	toolTypePath = "join-subtract-and-group"
# elif (choice == 8):
# 	toolTypePath = "statistics"
# elif (choice == 9):
# 	toolTypePath = "text-man-xml-files"
# elif (choice == 0):
# 	print("Program now exiting...")
# 	exit()
# else:
# 	print("Input not in choice. Program will now exit...")
# 	exit()

chromePath = "/home/dom/Downloads/chromedriver"

driver = webdriver.Chrome(chromePath)
driver.maximize_window()
driver.get('http://localhost:8080/')
#driver.get('http://localhost:8080/?tool_id=secure_hash_message_digest&version=0.0.1&__identifer=h7ktcro3v0t')

# reading multiple xml files in order
toolsPath = "all-tools" # whole directory of all tools
toolTypePath = "text-man-xml-files"
directory = toolsPath + "/" + toolTypePath

items = glob.glob(directory + "/*.xml")
items = sorted(items)
testDataList = []

'''
	uploads ALL the necessary data
'''

for file in items:
	v, toolName, dformat = retrieveValues(file)
	
	print(toolName)

	for i in range(0, len(v)):			# each test tag
		
		for j in range(0, len(v[i])): 	# each param tag
			# for checking

			# print("\n")
			# print("test name: ", v[i][j].n)
			# print("test data type: ", v[i][j].dt)
			# print("test value: ", v[i][j].v)
		
			# Uploading of file given a certain dformat
			getDataLabel = driver.find_element_by_xpath("//*[@id='title_getext']/a")

			inputPattern = re.match(r'^input(\d)*?$', v[i][j].n) # matching only the word "input" plus 0 or more integer 

			if ((inputPattern) and (v[i][j].dt not in testDataList)): # preventing multiple uploads of same test data

				testDataDirectory = "/home/dom/Desktop/test-galaxy/test-data/"
				testFile = v[i][j].v
				testData = testDataDirectory + testFile

				# Get Data	
				getDataLabel.click()

				# Upload File
				uploadFileLabel = driver.find_element_by_xpath("//*[@id='getext']/div[3]/div/a")
				uploadFileLabel.click()

				driver.find_element_by_xpath("//*[@id='s2id_autogen1']/a").click()
				field = driver.find_element_by_xpath("//*[@id='s2id_autogen2_search']")
				
				if (dformat == "tabular"):
					field.send_keys(dformat)
					field.send_keys(Keys.DOWN)
					field.send_keys(Keys.ENTER)
				elif (dformat == "txt"):
					field.send_keys(dformat)
					field.send_keys(Keys.ENTER)


				uploadTestData = driver.find_element_by_xpath("//*[@id='regular']/div/div[2]/input")
				uploadTestData.send_keys(testData)

				break

				testDataList.append(testFile) # preventing multiple uploads of same test data

				startButton = driver.find_element_by_xpath("//*[@id='btn-start']")
				startButton.click()

				time.sleep(5) # wait for full upload

				closeButton = driver.find_element_by_xpath("//*[@id='btn-close']")
				closeButton.click()

				time.sleep(5)

				getDataLabel.click() # reclicks Get Data label
	
print("Done uploading test data!")	

'''
	automation of values after all data tests have been uploaded
'''

# time.sleep(3)

# historyPanel = [] # container of the history panel (results/right side of Galaxy)

# for file in items:	# whole xml directory under a specific category of tool
# 	v, toolName, dformat = retrieveValues(file) # per xml file

# 	# automation

# 	for i in range(0, len(v)): 			# whole test tags
# 		for j in range(0, len(v[i])): 	# per test tag

# 			# different conditions for automation
# 			# based on data type (e.g. text, data, select, etc.)

# 			dataType = v[i][j].dt
# 			testValue = v[i][j].v 
# 			if (dataType == "text"): 
# 				try:
# 					inputField = driver.find_element_by_xpath("//*[starts-with(@id, 'field-uid')]")
# 					inputField.send_keys(Keys.CONTROL + "a")
# 					inputField.send_keys(Keys.DELETE)
# 					inputField.send_keys(testValue)
# 					inputField.send_keys(Keys.TAB)
# 				except NoSuchElementException:
# 					print("")				

# 			elif (dataType == "select"): # for select

# 				try: # for check boxes
# 					lists = driver.find_elements_by_xpath("//*[starts-with(@id, 'field-uid')]/div[3]/div")
# 					indexPath = 0 # to be used later for xpath
# 					listSize = len(lists)

# 					for t in range(0, listSize):
# 						wb = lists[t]
# 						if ((wb.text) == testValue):
# 							indexPath = t+1
# 							break

# 					origPath = "//*[starts-with(@id, 'field-uid')]/div[3]/div[xxx]/label" 	# 'xxx' is the variable that will be replaced by the indexPath
# 					ii = str(indexPath)
# 					newPath = origPath.replace("xxx", ii)
					
# 					wb = driver.find_element_by_xpath(newPath)
# 					wb.click()

# 				except NoSuchElementException: # for dropdown
					
# 					dropDown = driver.find_element_by_xpath("//*[starts-with(@id, 'field-uid')]/div[3]") 
# 					dropDown.click()
# 					dropDownField = driver.find_element_by_xpath("//*[@id='s2id_autogen5_search']")
# 					dropDownField.send_keys(testValue)
# 					dropDownField.send_keys(Keys.ENTER)

# 			elif (dataType == "boolean"):
# 				try:
# 					if (testValue == "true"):
# 						booleanButton = driver.find_element_by_xpath("//*[starts-with(@id, 'field-uid')]/div[3]/label[1]")
# 						booleanButton.click()
# 					elif (testValue == "false"):
# 						booleanButton = driver.find_element_by_xpath("//*[starts-with(@id, 'field-uid')]/div[3]/label[2]")
# 						booleanButton.click()
# 				except NoSuchElementException:
# 					print("")
	
	
# 		# clicking of button
# 		print("Executing the tool...")
# 		executeButton =  driver.find_element_by_xpath("//*[@id='execute']")
# 		executeButton.click()

# 		time.sleep(5) # wait for the changes to render

# 		# checking of result

# 		try:
# 			outputDiv = driver.find_element_by_xpath("//*[starts-with(@id, 'dataset-')]")
# 			historyPanel.append(outputDiv)
# 		except NoSuchElementException:
# 			print("No element.")









