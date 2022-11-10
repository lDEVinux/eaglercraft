@echo off
title ZipStableDownload
java -cp zip-generator/deps/*;zip-generator/zipGenerator.jar net.lax1dude.eaglercraft.zip_generator.ZipGenerator
pause