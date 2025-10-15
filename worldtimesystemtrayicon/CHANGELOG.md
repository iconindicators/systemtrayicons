# World Time System Tray Icon changelog

## v2.2 (2025-10-10)     TODO Fix date.

- Simplified the popup menu, moving some options to a new `Preferences` dialog
and dropping others.

- Combined `Add/Remove` and `Rename` of time zones into a single dialog.

- Dropped `Show Date/Time` dialog.  The date/time for selected time zones is
always shown in the tooltip and message bubble.

- Dropped `Show all Time Zones` and `Include local Time Zone` options from
`Time Travel`.

- Dropped `Column Alignments` from `Message Layout` as `HTML` is unsupported
in the tooltip and message bubble.

- Java 8 or better now required.

- NSIS launcher/installer now target Unicode rather than ANSI.


## v2.1 (2019-11-01)

- Bug Fix: Java 11+ has changed the first parameter from int to long for  
  accessing Windows registry.

- Revert to building for Java 1.6 as it makes life easier when testing!


## v1.8 (2017-05-01)

- Removed Debian target.


## v1.7 (2011-10-19)

- Changed the installer to install as local user; less issues with UAC.


## v1.6 (2011-10-08)

- Added URL to About box.

- Windows installer/launcher now handles UAC better.

- Windows registry access implemented in pure Java.


## v1.5 (2010-04-14)

- Fixed bug which incorrectly read in escaped commas. Refer to bug  
  `Can't use commas in user-defined time zone names` ID 2935102.
  
- Enhanced the layout option allowing the user to specify the alignment of the  
  message components.  Also allows message components to be combined. Refer  
  to feature request `Line up date/time column in balloon display` ID 2935106.

  The end result is that the message is now rendered as HTML in all dialogs.  
  Unfortunately it's not possible to use HTML in the bubble text nor the hover  
  text.

- Removed the translation of properties. This may effect your layout and so will  
  need to be reset.

- The message layout now supports whitespace.

- New translation for en_US added to support the spelling of "center" versus  
  "centre".


## v1.4 (2008-10-27)

- First Debian/Ubuntu release.
  
  NOTE: The tooltip and balloon text do not observe the new line character.
  See Sun bug [http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6440297](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6440297).

- Now supports multiple users. The user preferences are now saved to the  
  "user home" directory. On Microsoft Windows this is typically the directory  
  `Documents and Settings/{UserName}/.worldtimesystemtray` or   
  `Users/{UserName}/.worldtimesystemtray`. On Linux this is `/home/{UserName}`.
  
  NOTE for Microsoft Windows users: Your user preferences from the previous 
  version will NOT be loaded...sorry about that.  However, you can copy the
  'Program Files/World Time System Tray/properties' file to your "user home"  
  directory.

- Icons redesigned for multiple platforms and size requirements.

- Dialogs now show in the task bar.

- Updated to use jRegistryKey 1.4.5.0

- Removed the option to combine when date/time and time zones matched.    
  Java does not provide a way to compare time zones without resorting to using  
  user formatted strings.  
  For example, for Rome, Java will not give `CEST` or `+0200` as the time zone,  
  either of which is universally comparable to other time zones.  

  Therefore, the combine option has been changed to simply on or off.  

  You may have to select/unselect this option as a result of the menu change.

- Slight reorganisation of the popup menu to group common functionality.

- Cleaned up various compiler warnings.


## v1.3 (2007-11-07)

- Removed forced selection of Time Zone in Add/Remove Time Zone dialog.

- Handle exception when an invalid Time Zone is processed.

- Empty lists now correctly saved in properties.


## v1.2 (2007-10-12)

- Included GMT Time Zones.  This is what Java supplies out of the box.

- Tray icon is now included in the JAR file.  Execute the JAR from the command  
  line as
    ```
    java -jar world-time-system-tray.jar
    ```
  Ensure that both jRegistryKey.jar and jRegistryKey.dll are present in the path  
  if executing on Microsoft Windows.  Has not been tested on any platform other  
  than Microsoft Windows.

- Added menu option to show tool tip (hover/bubble) text in a separate window.


## v1.1 (2007-10-10)

- Rename dialog now resizes properly.

- New option for combining Time Zones 
    - Exact match on Date/Time and Time Zone 
    - Exact match on Date/Time only
    - Never (does not combine Time Zones)

  A side effect of the option "match on date/time only" is when some date/time  
  formats (typically FULL) are selected AND two (or more) time zones selected  
  which have the same date/time but different time zone, the time zone will be  
  erroneous for all but the first.  For example:
    ```
    Cape Town: Wednesday, 3 October 2007 04:43:06 AM SAST
    Rome: Wednesday, 3 October 2007 04:43:06 AM CEST
    ```
  would be combined to be:
    ```
    Cape Town | Rome: Wednesday, 3 October 2007 04:43:06 AM SAST
    ````
  where the time zone "SAST" is clearly wrong for Rome.  Unfortunately   
  stripping the "SAST" off won't work for i18n.  Suggestions for a fix are most  
  welcome!
  
  Option to allow the separator to also be specified.  You could have:
    ```
    Cape Town | Rome: 4:43 AM
    ```
  or
    ```
    Cape Town, Rome: 4:43 AM
    ```
  or whatever.

- Fixed icon in About dialog.

- Found Joda Time was not using the most recent tz database, so Auckland  
  (New Zealand) was the wrong time when it went into daylight savings.  

  Removed dependence on Joda Time and now uses only Java.  Found the following  
  URLs very useful in doing this:

    [http://www.exampledepot.com/egs/java.util/GetTimeOtherZone2.html](http://www.exampledepot.com/egs/java.util/GetTimeOtherZone2.html)
    [http://www.icu-project.org/docs/papers/international_calendars_in_java.html](http://www.icu-project.org/docs/papers/international_calendars_in_java.html)
    [http://www.xmission.com/~goodhill/dates/FAQ-PVL/faq_b2.htm](http://www.xmission.com/~goodhill/dates/FAQ-PVL/faq_b2.htm)
    [http://www.rgagnon.com/javadetails/java-0102.html](http://www.rgagnon.com/javadetails/java-0102.html)
    [http://www.velocityreviews.com/forums/t140900-conversion-of-date-from-one-timezone-to-another.html](http://www.velocityreviews.com/forums/t140900-conversion-of-date-from-one-timezone-to-another.html)
    [http://www.odi.ch/prog/design/datetime.php](http://www.odi.ch/prog/design/datetime.php)

- New option to specify the layout of Date/Times.

- New option to specify a separator between Time Zones when combining.

- The "Run on System Start" option only displays for Microsoft Windows.


## v1.0 (2007-07-24)

- First release.
