CD /D C:\pleiades\workspace\Test02\src\_bat
IF NOT EXIST _Test02_bat GOTO END

COPY C:\Factory\Satellite\extern\WinAPITools.exe ..\charlotte\satellite\res\WinAPITools.exe_

C:\Factory\SubTools\EmbedConfig.exe --factory-dir-disabled ..\charlotte\satellite\res\WinAPITools.exe_

:END
PAUSE
