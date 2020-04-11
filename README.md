# AllkuPos_Ec
Software POS for Ecuador, based on OpenBravo and UniCenta (POS -> Point of Sale)

## Requirements
1. Configure Java 8 https://www.allku.expert/configurar-java-home/
 also is valid openJDK 8 https://adoptopenjdk.net/
2. Configure Maven https://www.allku.expert/instalar-apache-maven/
3. Install MySQL 8 https://dev.mysql.com/downloads/

## Instruction to install
1. Clone repository
2. Get into folder
3. mysql> CREATE SCHEMA `allkupos` DEFAULT CHARACTER SET utf8;
4. $> mvn clean
5. $> mvn install
6. Get into target folder and execute start.bat
