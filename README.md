# AllkuPos_Ec
Software POS for Ecuador, based on OpenBravo and UniCenta (POS -> Point of Sale)

## Requirements
1. Configure Java 8 https://www.allku.expert/configurar-java-home/
 , openJDK 8 not recomended
2. Configure Maven https://www.allku.expert/instalar-apache-maven/
3. Install MySQL 8 https://dev.mysql.com/downloads/

## Instruction to install
1. Clone repository
2. Get into folder
3. mysql> CREATE SCHEMA `allkupos`;
4. $ mvn clean
5. $ mvn install
6. Get into target folder and execute start.bat (windows)

# Manual
## Printer (Epson tmu-220, Epson tm-t20 or any model) for text mode (RECOMMENDED)
### Windows
1. Install printer and share, then execute in cmd:
* C:\> net use LPT1: /delete
* C:\> net use LPT1: \\computer-name\name-shared-printer /PERSISTENT:YES
2. In Allku Pos Ec, in System -> Configuration -> Peripherals, set:
* Printer 1 -> your-printer-model
* Mode -> file
* Port -> LPT1
***
## Fonts
### GNU/Linux
* Install ttf-mscorefonts-installer package

## Changes list
* [Es](updates.es.md)
* [En](updates.md)