My first Java/JavaFX application. The main idea of ​​this application is to collect hardware and software information about
the device where the program is running and manage the running processes.

MAIN FEATURES:

Graphical user interface made with Gluon's Scene Builder. UI contains a dropdown menu, where you can select an option.
Options are:
windows user -> The logged-in Windows user is automatically displayed when the program is run.

These are executed when you select them:
OS -> shows information about your operating system.
Memory -> shows information about your device's RAM memory.
Processor -> shows information about your device's processor.
Java -> shows your Java version and installation directory.
File system -> shows the total status of the hard disk and the free space.

Running applications-> shows running applications and their process ID.
when you select the running apps option, a hidden text field and button will appear. 
With these you can shut down the running process by entering the process id number in the text field and clicking the close button.

QUICK OPENER
You can open common windows applications with one click from the drop down menu.
Quick Opener uses the Java ProcessBuilder class to launch applications, except for the browser, which is launched using
the java.awt.desktop class.

GRAPHICS
With the Graphics option, you can draw graphical pie charts of your ram memory and hard disk space.
you can also save the pie charts to your device in png format by clicking the save button.
Graphic charts are made with the javafx.scene.chart library.
On the hard disk option program searches all the hard disks of your device and finally asks you to select the hard disk whose data you want to see in the pie chart.
