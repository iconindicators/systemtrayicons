;--------------------------------
;Based in part on 
;  http://www.klopfenstein.net/lorenz.aspx/simple-nsis-installer-with-user-execution-level


;--------------------------------
;Constants

  !define APPLICATION_NAME "Stardate System Tray"
  !define LAUNCHER_NAME "StardateSystemTray.exe"
  !define REGISTRY_UNINSTALL_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}"
  !define START_MENU "$STARTMENU\Programs\${APPLICATION_NAME}"
  !define UNINSTALLER_NAME "Uninstall.exe"


;--------------------------------
;Includes

  !addplugindir "."
  !include MUI2.nsh
  !include WordFunc.nsh


;--------------------------------
;General

  Name "${APPLICATION_NAME}"
  OutFile "StardateSystemTraySetup.exe"
  InstallDir "$LOCALAPPDATA\${APPLICATION_NAME}"
  RequestExecutionLevel user
  SetCompressor /SOLID lzma


;--------------------------------
;Variables

  Var StartMenuFolder


;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_FINISHPAGE_NOAUTOCLOSE
  !define MUI_FINISHPAGE_RUN "$INSTDIR\${LAUNCHER_NAME}"
  !define MUI_LICENSEPAGE_RADIOBUTTONS
  !define MUI_STARTMENUPAGE_NODISABLE
  !define MUI_UNABORTWARNING
  !define MUI_UNFINISHPAGE_NOAUTOCLOSE


;--------------------------------
;Pages

  Page custom CheckJRE
  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "..\..\License.txt"
  
  !insertmacro MUI_PAGE_STARTMENU "Application" $StartMenuFolder
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH


;--------------------------------
;Languages

  !insertmacro MUI_LANGUAGE "English"


;--------------------------------
;Installer Section

Section "Install" 

  SetOutPath "$INSTDIR"
  SetOverwrite on

  File ..\..\ReleaseNotes.txt
  File ..\..\License.txt
  File ..\..\img\stardatesystemtray48x48.ico
  File ..\..\release\stardate-4.0.jar
  File ..\..\release\stardatesystemtray.jar
  File ..\..\lib\joda-time-2.9.9.jar
  File ..\..\packaging\win32\StardateSystemTray.exe

  WriteUninstaller "$INSTDIR\${UNINSTALLER_NAME}"

  WriteRegStr HKCU "${REGISTRY_UNINSTALL_KEY}" "DisplayName" "${APPLICATION_NAME}"
  WriteRegStr HKCU "${REGISTRY_UNINSTALL_KEY}" "UninstallString" "$\"$INSTDIR\${UNINSTALLER_NAME}$\""
  WriteRegDWORD HKCU "${REGISTRY_UNINSTALL_KEY}" "NoModify" 1
  WriteRegDWORD HKCU "${REGISTRY_UNINSTALL_KEY}" "NoRepair" 1

  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Run" "${APPLICATION_NAME}" "$INSTDIR\${LAUNCHER_NAME}"

  SetShellVarContext current
  CreateDirectory "${START_MENU}"
  CreateShortCut "${START_MENU}\${APPLICATION_NAME}.lnk" "$INSTDIR\${LAUNCHER_NAME}" "" "$INSTDIR\stardatesystemtray48x48.ico"
  CreateShortCut "${START_MENU}\Uninstall.lnk" "$INSTDIR\${UNINSTALLER_NAME}"

SectionEnd


;--------------------------------
;Uninstaller Section

Section "Uninstall"

  RMDir /r /REBOOTOK "$INSTDIR"

  DeleteRegKey HKCU "${REGISTRY_UNINSTALL_KEY}"

  SetShellVarContext current
  RMDir /r "${START_MENU}"

SectionEnd


Function .onInit
  FindProcDLL::FindProc "${LAUNCHER_NAME}"
  IntCmp $R0 1 0 notRunning
  MessageBox MB_OK|MB_ICONEXCLAMATION "${APPLICATION_NAME} is running - please close it first." /SD IDOK
  Abort
  notRunning:
FunctionEnd


Function un.onInit
  FindProcDLL::FindProc "${LAUNCHER_NAME}"
  IntCmp $R0 1 0 notRunning
  MessageBox MB_OK|MB_ICONEXCLAMATION "${APPLICATION_NAME} is running - please close it first." /SD IDOK
  Abort
  notRunning:
FunctionEnd


Function CheckJRE
  !insertmacro VersionCompare
  call GetJavaVersion
  pop $0 ; major version
  pop $1 ; minor version
  strcmp $0 "no" BadJava
  ${VersionCompare} "$0.$1" "1.6" $2
  strcmp $2 "2" BadJava
  Return

BadJava:
  MessageBox MB_OK "You require Java Runtime Environment 1.6 or better installed."
  Quit

FunctionEnd


;--------------------------------
; Find installed java version and return major, minor, micro and build/update version
; For some reason v1.2.1_004 did not give a build version, but it's the only one of its kind.
; There are 3 ways to get the build version:
;   1) from the UpdateVersion key
;   2) or from the MicroVersion key
;   3) or from the JavaHome key
;example
;  call GetJavaVersion
;  pop $0 ; major version
;  pop $1 ; minor version
;  pop $2 ; micro version
;  pop $3 ; build/update version
;  strcmp $0 "no" JavaNotInstalled
;  strcmp $3 "" nobuild
;  DetailPrint "$0.$1.$2_$3"
;  goto fin
;nobuild:
;  DetailPrint "$0.$1.$2"
;  goto fin
;JavaNotInstalled:
;  DetailPrint "Java Not Installed"
;fin:
Function GetJavaVersion
  push $R0
  push $R1
  push $2
  push $0
  push $3
  push $4
  
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $2 "" DetectTry2
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "MicroVersion"
  StrCmp $3 "" DetectTry2
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "UpdateVersion"
  StrCmp $4 "" 0 GotFromUpdate
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$2" "JavaHome"
  Goto GotJRE
DetectTry2:
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  StrCmp $2 "" NoFound
  ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "MicroVersion"
  StrCmp $3 "" NoFound
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "UpdateVersion"
  StrCmp $4 "" 0 GotFromUpdate
  ReadRegStr $4 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$2" "JavaHome"
GotJRE:
  ; calc build version
  strlen $0 $3
  intcmp $0 1 0 0 GetFromMicro
  ; get it from the path
GetFromPath:
  strlen $R0 $4
  intop $R0 $R0 - 1
  StrCpy $0 ""
loopP:
  StrCpy $R1 $4 1 $R0
  StrCmp $R1 "" DotFoundP
  StrCmp $R1 "_" UScoreFound
  StrCmp $R1 "." DotFoundP
  StrCpy $0 "$R1$0"
  Goto GoLoopingP
DotFoundP:
  push ""
  Exch 6
  goto CalcMicro
UScoreFound:
  push $0
  Exch 6
  goto CalcMicro
GoLoopingP:
  intcmp $R0 0 DotFoundP DotFoundP
  IntOp $R0 $R0 - 1
  Goto loopP
GetFromMicro:
  strcpy $4 $3
  goto GetFromPath
GotFromUpdate:
  push $4
  Exch 6
 
CalcMicro:
  Push $3 ; micro
  Exch 6
  ; break version into major and minor
  StrCpy $R0 0
  StrCpy $0 ""
loop:
  StrCpy $R1 $2 1 $R0
  StrCmp $R1 "" done
  StrCmp $R1 "." DotFound
  StrCpy $0 "$0$R1"
  Goto GoLooping
DotFound:
  Push $0 ; major
  Exch 5
  StrCpy $0 ""
GoLooping:
  IntOp $R0 $R0 + 1
  Goto loop
 
done:
  Push $0 ; minor
  Exch 7
  ; restore register values
  pop $0
  pop $2
  pop $R1
  pop $R0
  pop $3
  pop $4
  return
NoFound:
  pop $4
  pop $3
  pop $0
  pop $2
  pop $R1
  pop $R0
  push ""
  push "installed"
  push "java"
  push "no"
FunctionEnd