from selenium import webdriver
from selenium.webdriver.common.keys import Keys

chrome_path = "/home/dom/Downloads/chromedriver"

browser = webdriver.Chrome(chrome_path)
browser.get('http://localhost:8080/')