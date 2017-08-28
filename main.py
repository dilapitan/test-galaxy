import xml.etree.ElementTree as ET
from selenium import webdriver
from selenium.webdriver.common.keys import Keys


import os

'''
# Selenium Part
chromePath = "/home/dom/Downloads/chromedriver"

browser = webdriver.Chrome(chromePath)
browser.get('http://localhost:8080/')
'''

# XML parsing and retrieving values
def retrieveValues(file):
	root = ET.parse(file).getroot() 
	
	'''
		Getting the values from <tests>
	'''
	
	testLength = len(root.findall('.//test')) # getting the number of <test>

	testParamValues = [] # array of values under <param> of <test>
	
	testParams = root.findall('.//test//param')
	testParamLength = len(testParams) # getting the number of <param> under <test>

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
	
	inputsParamValues = []
	for testCount in range(0, testLength): # for catching multiple <test>
		for inputsParam in inputsParams:
			inputsNameText = inputsParam.get('name')
			inputsTypeText = inputsParam.get('type')

			inputsParamValues.append(inputsNameText)
			inputsParamValues.append(inputsTypeText)
		
	#print(testParamValues)
	#print(inputsParamValues)

	''' 
		Object that will be passed for automation 
	'''

	class Values:
		def __init__(self, name, datatype, value):
			self.n = name
			self.dt = datatype
			self.v = value

	c = 0
	paramDouble = testParamLength * 2
	wholeV = [] # values of all v
	for i in range(0, testLength):
		v = [] # values of single <test> 
		while (c != paramDouble):
			# object creation
			testObject = Values(testParamValues[c], testParamValues[c+1], inputsParamValues[c+1])
			
			# for checking
			# print("test name: ", testObject.n)
			# print("test data type: ", testObject.dt)
			# print("test value: ", testObject.v)
			# print("\n")
			
			v.append(testObject)
			c += 2
		wholeV.append(v)

	return wholeV # values for automation

# reading multiple xml files in order
toolsPath = "all-tools" # whole directory of all tools
toolTypePath = "text-man-xml-files" #specific type of tools
#directory = toolsPath + "/" + toolTypePath

directory = "/home/dom/Desktop/test-galaxy/"

items = os.listdir(directory)
for file in sorted(items):
	if file.endswith(".xml"):
		retrieveValues(file)
		break




