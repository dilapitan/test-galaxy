from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

chromePath = "/home/dom/Downloads/chromedriver"
driver = webdriver.Chrome(chromePath)
driver.get('http://www.facebook.com')

value = "domlapitan"

username = driver.find_element(By.XPATH, '//*[@id="email"]')
username.send_keys(value)
