;--------------------------------
;Constants

  !define APPLICATION_NAME "World Time System Tray"
  !define LAUNCHER_NAME "WorldTimeSystemTray.exe"
  !define JAR_NAME "worldtimesystemtray.jar"


;--------------------------------
;General

  Name "${APPLICATION_NAME}"
  Icon "..\..\img\worldtimesystemtray.ico"
  OutFile "${LAUNCHER_NAME}"
  RequestExecutionLevel user
  SetCompressor /SOLID lzma
  SilentInstall silent
  AutoCloseWindow true
  ShowInstDetails nevershow


;--------------------------------
;Main Section

  Section ""
    Call GetJRE
    Pop $R0
    StrCpy $0 '"$R0" -jar ${JAR_NAME}'
    SetOutPath $EXEDIR
    ExecWait $0
  SectionEnd


;--------------------------------
;Locate the Java Runtime Environment

  Function GetJRE
  ;  http://nsis.sourceforge.net/A_slightly_better_Java_Launcher
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
