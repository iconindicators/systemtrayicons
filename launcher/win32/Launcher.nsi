; NOTE: this script attempts to determine the current Java version by going to the registry.
; I have seen instances where the version returned from the registry is not actually installed!
; Maybe instead of going to the registry, just use javaw.exe and tell the user to ensure JAVA_HOME 
; needs to be set.

; Java Launcher
;--------------
 
Name "Java Launcher"
Caption "Java Launcher"
Icon "..\..\img\application.ico"
OutFile "StardateSystemTray.exe"
 
SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow
 
Section ""
  Call GetJRE
  Pop $R0
 
  ; change for your purpose (-jar etc.)
  StrCpy $0 'javaw.exe -jar stardate-system-tray.jar'
;  StrCpy $0 '"$R0" -jar stardate-system-tray.jar'

  SetOutPath $EXEDIR
  Exec $0
SectionEnd

 
Function GetJRE
;
;  Find JRE (Java.exe)
;  1 - in .\jre directory (JRE Installed with application)
;  2 - in JAVA_HOME environment variable
;  3 - in the registry
;  4 - assume java.exe in current dir or PATH
 
  Push $R0
  Push $R1
 
  ClearErrors
  StrCpy $R0 "$EXEDIR\jre\bin\javaw.exe"
  IfFileExists $R0 JreFound
  StrCpy $R0 ""

  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\javaw.exe"
  IfErrors 0 JreFound
 
  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCpy $R0 "$R0\bin\javaw.exe"
 
  IfErrors 0 JreFound
  StrCpy $R0 "javaw.exe"
        
 JreFound:
  Pop $R1
  Exch $R0
FunctionEnd
