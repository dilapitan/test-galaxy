from selenium import webdriver
from selenium.webdriver.common.keys import Keys

import os

chromePath = "/home/dom/Downloads/chromedriver"

#browser = webdriver.Chrome(chromePath)
#browser.get('http://localhost:8080/')

# reading multiple xml files in order
toolsPath = "all-tools" # whole directory of all tools
toolTypePath = "text-man-xml-files" #specific type of tools
directory = toolsPath + "/" + toolTypePath

items = os.listdir(directory)
for path in sorted(items):
	if path.endswith(".xml"):
		# insert retrieveValues() here
		print(path)

