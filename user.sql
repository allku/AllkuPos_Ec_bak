/*Select Users*/
--select host, user from mysql.user;
/*Delete user*/
--DROP USER ‘username’@‘localhost’;


CREATE USER 'allkupos'@'%';
ALTER USER 'allkupos'@'%'
IDENTIFIED BY 'a' ;
GRANT Alter ON allkupos.* TO 'allkupos'@'%';
GRANT Create ON allkupos.* TO 'allkupos'@'%';
GRANT Create view ON allkupos.* TO 'allkupos'@'%';
GRANT Delete ON allkupos.* TO 'allkupos'@'%';
GRANT Drop ON allkupos.* TO 'allkupos'@'%';
GRANT Grant option ON allkupos.* TO 'allkupos'@'%';
GRANT Index ON allkupos.* TO 'allkupos'@'%';
GRANT Insert ON allkupos.* TO 'allkupos'@'%';
GRANT References ON allkupos.* TO 'allkupos'@'%';
GRANT Select ON allkupos.* TO 'allkupos'@'%';
GRANT Show view ON allkupos.* TO 'allkupos'@'%';
GRANT Trigger ON allkupos.* TO 'allkupos'@'%';
GRANT Update ON allkupos.* TO 'allkupos'@'%';
GRANT Alter routine ON allkupos.* TO 'allkupos'@'%';
GRANT Create routine ON allkupos.* TO 'allkupos'@'%';
GRANT Create temporary tables ON allkupos.* TO 'allkupos'@'%';
GRANT Execute ON allkupos.* TO 'allkupos'@'%';
GRANT Lock tables ON allkupos.* TO 'allkupos'@'%';
FLUSH PRIVILEGES;
