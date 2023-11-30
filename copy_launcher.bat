robocopy . target\FAREC launcher.vbs
cd target
powershell -Command Compress-Archive -Update -Path .\FAREC\launcher.vbs -DestinationPath FAREC.zip