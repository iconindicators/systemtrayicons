;--------------------------------
;Constants

  !define APPLICATION_NAME "World Time System Tray"
  !define LAUNCHER_NAME "WorldTimeSystemTray.exe"

;--------------------------------
;Includes

  !addplugindir "."
  !include MUI2.nsh
  !include WordFunc.nsh


;--------------------------------
;General

  Name "${APPLICATION_NAME}"
  OutFile "WorldTimeSystemTraySetup.exe"
  InstallDir "$PROGRAMFILES\${APPLICATION_NAME}"
  RequestExecutionLevel admin
  SetCompressor /SOLID lzma


;--------------------------------
;Variables

  Var StartMenuFolder


;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_LICENSEPAGE_RADIOBUTTONS
  !define MUI_STARTMENUPAGE_NODISABLE
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU"
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${APPLICATION_NAME}"
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  !define MUI_UNABORTWARNING


;--------------------------------
;Pages

  Page custom CheckJRE
  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_LICENSE "..\..\License.txt"
  !insertmacro MUI_PAGE_DIRECTORY
  
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

  SetShellVarContext all

  SetOutPath "$INSTDIR"

  File ..\..\ReleaseNotes.txt
  File ..\..\TODO.txt
  File ..\..\License.txt
  File ..\..\img\worldtimesystemtray.ico		
  File ..\..\release\worldtimesystemtray.jar
  File ..\..\packaging\win32\WorldTimeSystemTray.exe	

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Run" "${APPLICATION_NAME}" "$INSTDIR\${LAUNCHER_NAME}"

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}" "DisplayName" "${APPLICATION_NAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}" "NoRepair" 1

  WriteUninstaller "$INSTDIR\Uninstall.exe"

  !insertmacro MUI_STARTMENU_WRITE_BEGIN "Application"
    CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
    CreateShortCut "$SMPROGRAMS\$StartMenuFolder\${APPLICATION_NAME}.lnk" "$INSTDIR\${LAUNCHER_NAME}" "" "$INSTDIR\worldtimesystemtray.ico"
    CreateShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\Uninstall.exe"  
  !insertmacro MUI_STARTMENU_WRITE_END

SectionEnd


;--------------------------------
;Uninstaller Section

Section "Uninstall"

  SetShellVarContext all

  RMDir /r /REBOOTOK "$INSTDIR"

  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $StartMenuFolder
  RMDir /r "$SMPROGRAMS\$StartMenuFolder"

  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPLICATION_NAME}"
  DeleteRegValue HKLM "Software\Microsoft\Windows\CurrentVersion\Run" "${APPLICATION_NAME}"

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
