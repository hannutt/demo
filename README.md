Project keywords: JavaFX, CMD- ,Netsh- & WMIC-commands, MySQL, File Manager, Process Manager.

My first Java/JavaFX application. The main idea of ​​this application is to collect hardware and software information
the device where the program is running and manage the running processes. You can also run cmd, wmic and netsh
commands using the functions of this application and manage files, applications and file searches / file deletions.

![sysinfo.png](src%2Fmain%2Fresources%2Fimg%2Fsysinfo.png)

MAIN FEATURES:

Automatic functions at startup:

The logged-in Windows user is automatically displayed when the program is run.
The status of the Windows firewall is also checked automatically and the status is shown on the label (Firewall ON/OFF)


Graphical user interface made with Gluon's Scene Builder. UI contains a dropdown menu, where you can select an option.
Options are:

These are executed when you select them:

OS -> shows information about your operating system.

Memory -> shows information about your device's RAM memory.

Processor -> shows information about your device's processor.

Java -> shows your Java version and installation directory.

File system -> shows the total status of the hard disk and the free space.

Registry -> access to the Windows registry.

WLAN report -> Create a new wlan report  (netsh wlan show wlanreport -command) or open the latest report in a web browser

Printers -> displays a list of available printers.

Running applications-> shows running applications and their process ID.
The process IDs can be found on the tabs of the menu button, which you can click to close the application.

Network -> shows your IP address, available wlan networks, hostname and checks your Internet connection status by pinging an external web page.
the network feature also has a hidden text field and button that appear when the feature is selected.
You can ping any web page and the program will tell you if the page is available or not.
If the ping is ok, the program will show the WebView check box. By clicking on it, you will
see the webpage you are looking for in javaFX webView.

After selecting a wlan network, you can also run the "netsh wlan show profile name=?" command, which will display. 
more detailed information about the selected wlan network in the program's text area.

DIR FEATURE

Displays a list of files and hidden files and subdirectories of directories in the user-entered path.
You can also use the Bare format option in the file list. This option only shows the names and file 
extensions of the files in the folder. (dir /B cmd command.)
Sort by Size displays files by size from smallest to largest (dir /o:s cmd command).
The list appears in the program's text area component.
This is done with cmd commands and Java Runtime & BufferReader classes.

You can also perform more detailed searches, such as searching only for hidden files, 
subfolders, or searching for files by file type.
For example, select the Advanced dir option from the menu, enter a file extension and press TAB,
and the program will search for files with the extension you entered.

Click and drop feature. you can select this option from the advanced dir menu. 
The program searches for folders on the C-drive and displays the found folders. 
Click on one of the results and the program will automatically display the contents of the folder.

DELETE FEATURE

in connection with the delete feature, the program uses the JavaFx ListView component.
So you can delete files with a mouse click. Because deleting files can cause damage, you must log in first. 
The login information is stored in the mysql database. In the login window, the user has 3 login attempts.
If all three go wrong, the login button is disabled and the program must be restarted.

Delete feature can also use without log in if CMD command whoami return right value.
in this case it works only with my own windows account name. this feature is maded with 
public boolean wich run  whoami command using Runtime,Process and BufferReader classes.

In the Delete window, you can select an item from the drop-down menu that shows all the 
directories on the C drive. you can click to select a folder and it will go to the input 
field and click the dir button and the folder files will be listed in the list view component.
(this feature is maded with Files.walk method and event handlers )
you can also enter the name of the hard drive and the folder directly in the input field.

QUICK OPENER

You can open common windows applications with one click from the drop down menu.
Quick Opener uses the Java ProcessBuilder class to launch applications, except for the browser, which is launched using
the java.awt.desktop class.
You can also open the desired program or file. These features are made with Java File Chooser and Runtime classes.
The program recognizes the file type and offers a few different options from which you can choose what you want to
open the file with.

So far, the program recognizes text, audio, PDF and image files and offers a few options from common Windows programs to
open them.

For example, when opening a text file, the program offers Wordpad and Notepad applications in a dialog box 
and waits until the user selects the application.

GRAPHICS

With the Graphics option, you can draw graphical pie charts of your ram memory and hard disk space.
you can also save the pie charts to your device in png format by clicking the save button.
Graphic charts are made with the javafx.scene.chart library.

On the hard disk option program searches all the hard disks of your device and finally asks you to select the hard disk whose data you want to see in the pie chart.
The memory and hard disk values ​​can also be saved in the MySQL database with a mouse click.
with checkboxes, you get information from the database to the program's listView component. 
You can also filter data, for example remove the recording date from the results.

CUSTOMIZATION

You can make some customizations, such as hide or show part of the interface, reduce or increase font sizes
change background colors. You can also change the colors of the text in the text area and make the text bold or italic.
The user interface is in English by default, but you can change the language to Finnish.

