# Stardate System Tray Icon changelog

## v6.0 (2025-10-15)

- Simplified the popup menu, moving some options to a new `Preferences` dialog
and dropping others.

- Added user defined date format.

- Dropped Gregorian-Julian and ISO8601 chronologies as they are redundant.

- Dropped `Show Calendars` dialog.  All chronologies (calendars) are displayed
in the message bubble (including the Stardate).

- Dropped Stardate Converter.

- Java Stardate API now accepts an `Instant` when converting to either
`classic stardate` or `2009 revised stardate`.

- Removed JavaDoc warnings for '<' and '>'.

- Java 8 or better now required.

- NSIS launcher/installer now target Unicode rather than ANSI.

- Updated Joda library.


## v5.0 (2019-11-01)

- Bug Fix: Java 11+ has changed the first parameter from int to long for
accessing Windows registry.


## v4.0 (2019-10-07)

- Minor changes to Stardate API.

- Stardate System Tray and Stardate Converter updated to reflect changes to the
Stardate API.

- Removed ',' separator from Stardate Converter.

- Updated Joda library.


## v3.0 (2017-04-29)

- Overhaul of Stardate API.

- Stardate System Tray and Stardate Converter updated to reflect changes to the
Stardate API.

- Fixed a bug when converting from '2009 revised' stardate to Gregorian
(was out by one day).

- Updated Joda library.


## v2.0 (2014-08-25)

- Implemented stardate '2009 revised' version to bring into line with the
Python version of the Stardate API. Some API changes have occurred.

- The Debian files have been removed from the repository as they are obsolete.

- Uses UTC rather than the local date/time.

- Fixed a bug when calculating present day stardates.

- Fixed a bug when padding the INTEGER part of classic stardates.

- Removed the sample applet.

- Updated Joda library.


## v1.9 (2012-06-11)

- Implemented a new Python version of the Stardate API including an indicator
applet for Ubuntu, Lubuntu and Xubuntu 12.04.  This may work on other Linuxes -
your mileage may vary! Refer to[http://launchpad.net/~thebernmeister/+archive/ppa](http://launchpad.net/~thebernmeister/+archive/ppa).

  The Python indicator applet supercedes the Java system tray indicator for
  Debian and will no longer built or supported.  The Debian-related files for
  building, icons, etc will be kept in the repository for posterity.

- Additions to the Java API: `getStardateFractionalPeriod()`. There is a one to
one match with the Python API.

- Corrected calculations such that the Python and Java stardates match.

- Updated Joda library.


## v1.8 (2011-10-19)

- Changed the installer to install as local user; less issues with UAC.


## v1.7 (2011-10-08)

- Added URL to About box.

- Windows installer/launcher now handles UAC better.

- Windows registry access implemented in pure Java.

- Updated Joda library.


## v1.6 (2009-11-13)

- Fixed aggressive rounding of the fractional part of a Stardate, which resulted
in its value only being a 0 or 5.

- Clean up of the sample Converter program.


## v1.5 (2008-10-27)

- New Debian/Ubuntu release!  NOTE: The tooltip and balloon text do not observe
the new line character. See Sun bug [http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6440297](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6440297).

- Now supports multiple users. The user preferences are now saved to the
`user home` directory.  

  On Microsoft Windows this is typically the directory
  `Documents and Settings/{UserName}/.stardatesystemtray` or
  `Users/{UserName}/.stardatesystemtray`.  

  On Linux this is /home/{UserName}. NOTE for Microsoft Windows users: Your user
  preferences from the previous version will NOT be loaded...sorry about that.  

  However, you can copy the `Program Files/Stardate System Tray/properties`
  file to your "user home" directory.

- Added option to show selected calendars in a new window.

- Icons redesigned for multiple platforms and size requirements.

- Dialogs now show in the task bar.

- Only shows the menu item "Run on system start" on Microsoft Windows.

- Updated to use jRegistryKey 1.4.5.0

- Added an option to pad out the integer part of the stardate. For example,
  the stardate [-28] 43.7 would be padded out to [-28] 0043.7

- Cleaned up various compiler warnings.


## v1.4 (2008-04-20)

- Updated to use latest Joda release.

- Now uses correct icon (rather than Java default) in the About dialogue.


## v1.3 (2007-11-07)

- To execute Stardate System Tray from the command line use
    ```
    java -jar stardate-system-tray.jar
    ```
  Ensure that `jRegistryKey.jar`, `jRegistryKey.dll`, `joda-time-1.4.jar` and
  `stardate.jar` are on the path if executing on Microsoft Windows.  

  Ensure that joda-time-1.4.jar and stardate.jar are on the path if executing
  on any platform other than Microsoft Windows.

  Ensure the files `jRegistryKey.jar`, `jRegistryKey.dll`, `joda-time-1.4.jar`
  are downloaded from their respective sites. Has not been tested on any
  platform other than Microsoft Windows.

* To execute the sample application from the command line as
    ```
    java -jar stardate-converter.jar
    ```
  Ensure that stardate.jar is on the path for all platforms. Has not been
  tested on any platform other than Microsoft Windows.

* To execute the sample applet, load stardate.html into a browser. Ensure
  `stardate.jar` and `stardate-applet.jar` are in the same directory as
  `stardate.html`.

* Tray icon is now included in the `stardate-system-tray.jar` file.  

* Tray icon is now included in the `stardate-converter.jar` file.  

* Selects a default of LONG date format on invalid property value.

* Empty lists are now correctly saved in properties.


## v1.2 (2007-07-24)

* Better handling for absence of system tray support.

* Added internationalisation capability.


## v1.1 (2007-06-22)

* Disabled the pop up menu when the About dialogue is displayed.

* Now uses system Look and Feel.


## v1.0 (2007-06-13)

* First release.
