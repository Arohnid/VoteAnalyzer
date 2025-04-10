# VoteAnalyzer
## Overview
The following repository contains an app that parses XML files using SAX parser. 
XML files contain data about voters and voting fields they visited.
App then structures information so that we can see how many times people visited voting fields.
## Guidelines
1. Clone this repository
2. Open this project with your preferred IDE
3. Navigate to DBConnection.java class and specify your database data
```java
    private static String dbName = "votersdb";
    private static String dbUser = "root";
    private static String dbPass = "password";
```
4. Navigate to Loader.java class
5. Specify which XML file you want to parse
```java
	String fileName = "res/data-1572M.xml";
```
## Result of app execution
![{928BEAC8-DFCD-4AAE-BA2F-D4D38AC5D85C}](https://github.com/user-attachments/assets/ba6e8162-4846-411d-8a30-7e40d73fab91)
