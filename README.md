 Announcer
===========


Description:
--------------

Announcer loads simple text files and broadcasts them in a configured interval.

 Usage:
--------

Simply create some plain text files (*.txt) inside the announcements folder of the plugin.

With the configuration file (config.yml) you can specify which of those announcements should be automaticly announced and in which order.

You can also specify the time between two announcment and whether it should start delayed.


The imgame command '/announce' can be used to manually announce a message.


 Features:
-----------

* Announce txt files automaticly to the server
* Announce files manually
* Supports color codes (&0, &1, &3, ..., &e, &f)
* Interval can be set in Ticks (t), Seconds, (s), Minutes (m), Hours (h), Days (d)


 Installation:
---------------

* Just put the Announcer.jar into you plugins folder
* create you messages as *.txt files in the specific folders
* restart/reload your server
* The configuration file will be auto-generated on the first start.


 Permissions:
--------------

* announcer.*
    * Announcer.announce - allows the player to announce messages
    * Announcer.reloadannouncer - allows the player to reload the plugin

Operators and the console have all permissions


Configuration:
--------------
Self-explanatory

***[Source on Github](https://github.com/quickwango/gmChat)***

Plugin developed by Quick_Wango - [Minecraft Portal](http://mc-portal.de)
