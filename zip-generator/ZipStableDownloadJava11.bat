@echo off
setlocal enabledelayedexpansion
title ZipStableDownload
cd ..
echo enter the path to your java 11 installation (or leave blank to assume it is the default): 
SET /P "JAVA11PATH="
if "!JAVA11PATH!" neq "" (
  set "JAVA11PATH=!JAVA11PATH:\=/!"
  if "!JAVA11PATH:~-1!" neq "/" (
    set "JAVA11PATH=!JAVA11PATH!/"
  )
  if "!JAVA11PATH:~-5!" neq "/bin/" (
    set "JAVA11PATH=!JAVA11PATH!bin/"
  )
)
echo Using java at: "!JAVA11PATH!java"
"!JAVA11PATH!java" -cp zip-generator/deps/*;zip-generator/zipGenerator.jar net.lax1dude.eaglercraft.zip_generator.ZipGenerator
endlocal
pause