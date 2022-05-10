## To add New SSL Certificate into Java Keystore

##### Execute the following command from C:\Program Files\Java\jdk-17.0.1\bin

`keytool -keystore ..\lib\security\cacerts -import -alias wso2 -file <file_name>.crt`
