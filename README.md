[@DIMAXER ( https://github.com/DIMAXER )]
B.Sc. Software Engineering 3rd year project - ONGOING.

Connecting to remote Cloud DB:
phpAdmin: https://remotemysql.com/phpmyadmin/index.php?db=ZZUehgwcK6
username: ZZUehgwcK6  password: hfwrGUeVv7

[@RAZMALKA ( https://github.com/RAZMALKA )] - ICM on Steroids v1
committed 28/12/19 - 02:46 AM, see details

### DO NOT CONTINUE WORKING ON THE MAIN BEFORE TALKING TO MALKA ###

RECENT UPDATE NOTES:
	Date of Changes:	25/12/19 - 28/12/19
	Author of Changes:	Raz Malka
	Details of Changes:
1. Client:
1.1 customized according to Singleton Design Pattern
1.2 getInstance() function added
1.3 initialize() function added
1.4 deprecated functions removed
1.5 all of the previous static functions turned non-static

2. ClientUI:
2.1 customized according to Singleton Design Pattern
2.2 getInstance() function added
2.3 clientUI() constructor changed
2.4 start() function changed to fit into Singleton accessibility
2.5 client property removed
2.6 deprecated functions removed
2.7 all of the previous static functions turned non-static

3. ServerUI:
3.1 Java Class ServerUI added
3.2 customized according to Singleton Design Pattern
3.3 getInstance() function added
3.3 moved onto - functions main() & start() from ServerController
3.4 extensions added: Application

4. ServerController:
4.1 cleaned - main() & start functions moved to ServerUI
4.2 extensions removed: Application

5. DBServer:
5.1 customized according to Singleton Design Pattern
5.2 cleaned - handler functions moved to RequestHandler
5.3 all of the previous static functions turned non-static
5.4 getInstance() function added

6. RequestHandler:
6.1 Java Class RequestHandler added
6.2 customized according to Singleton Design Pattern
6.3 moved onto - handler functions from DBServer
6.4 getInstance() function added

7. mysqlConnection:
7.1 customized according to Singleton Design Pattern
7.2 all of the previous static functions turned non-static
7.3 getInstance() function added

8. ScreenManager:
8.1 customized according to Singleton Design Pattern
8.2 client & clientUI properties removed
8.3 getInstance() function added
8.4 initialize() function added
8.5 deprecated function injectLogicController removed
8.6 functions altered to be accessible only after initialization

9. LoginFX:
9.1 deprecated function setLogicController removed
9.2 initialize function changed

10. ConnectServerManualyFX:
10.1 deprecated function setLogicController removed
10.2 initialize function changed

11. PanelFX:
11.1 deprecated function setLogicController removed
11.2 initialize function changed

12. RequestFormFX:
12.1 deprecated function setLogicController removed
12.2 initialize function changed

13. SearchRequestFX:
13.1 deprecated function setLogicController removed
13.2 initialize function changed

14. Shoham's Changes from 27/12/19 Merged successfully.

15. Fixed fatal bugs in ViewAllRequestsFX and ViewAllRequestsController.

16. Added accelerating functionality to ScreenManager & PanelFX:
16.1 sceneExists function added to ScreenManager
16.2 clearScreenMap function added to ScreenManager
16.3 normalizeAppearance function added to ScreenManager

17. Design made to be identical:
17.1 AcademicUserPanel
17.2 ConnectServerManualy
17.3 ViewAllRequests

18. BaseFX cleaned and made to implement Initializable

19. ViewAllRequestsFX made to respond to double click on row.

20. Linguistic errors in cloud db fixed in both code and tables.

21. Screens are now centered, unresizable and fixed to boundaries.

22. Fatal error in Server GUI's mySQL connection fixed.
22.1 whenever mysqlConnection.con is null and close() was initiated.
22.2 whenever db connection was successfull but server timed out.
22.3 whenever server failed to maintain socket connection.

TODO:
-  Need to always keep in mind to make the code readable and friendly for every one else - USE JAVADOCS!
1. Merge Afriat's parts.
2. Add the remaining RequestDetails fxmls that were made for UI/UX Assignment 1
3. Remake RequestDetails fxmls - change TextFields to a better component.
