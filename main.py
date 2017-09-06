from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

import xml.etree.ElementTree as ET
import os

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
	for testCount in range(0, testLength): # for catching multiple <test>
		for inputsParam in inputsParams:
			inputsNameText = inputsParam.get('name')
			inputsTypeText = inputsParam.get('type')

			data = inputsParam.get('format')
			if (data != None):
				dataFormat = data

			inputsParamValues.append(inputsNameText)
			inputsParamValues.append(inputsTypeText)
		
	print(testParamValues)
	print(inputsParamValues)

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
			testObject = Values(testParamValues[c], testParamValues[c+1], inputsParamValues[c+1])
		
			v.append(testObject)
			c += 2
		d += paramDouble
		wholeValues.append(v)
	
	return wholeValues, toolName, dataFormat # values for automation

retrieveValues("/home/dom/Desktop/test-galaxy/fixedValueColumn.xml")


# chromePath = "/home/dom/Downloads/chromedriver"

# driver = webdriver.Chrome(chromePath)
# driver.get('http://localhost:8080/')

# # reading multiple xml files in order
# toolsPath = "all-tools" # whole directory of all tools
# toolTypePath = "text-man-xml-files" #specific type of tools
# #directory = toolsPath + "/" + toolTypePath

# directory = "/home/dom/Desktop/test-galaxy/"

# items = os.listdir(directory)
# for file in sorted(items):
# 	if file.endswith(".xml"):
# 		v, toolName, data = retrieveValues(file)

# 		print(toolName)
# 		# for checking
# 		for i in range(0, len(v)):
# 			print(i+1)
# 			for j in range(0, len(v[i])):
# 				print("\n")
# 				print("test name: ", v[i][j].n)
# 				print("test data type: ", v[i][j].dt)
# 				print("test value: ", v[i][j].v)
	
# 		''' 
# 			Automation / mimicking user
# 		'''

# 		break






