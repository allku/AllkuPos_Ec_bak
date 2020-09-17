ALTER TABLE `tickets` 
DROP FOREIGN KEY `TICKETS_CUSTOMERS_FK`;
ALTER TABLE `tickets` 
DROP INDEX `TICKETS_CUSTOMERS_FK` ;

update tickets t
inner join customers c
on t.CUSTOMER = c.ID
set t.CUSTOMER = c.TAXID
where length(c.id) > 13;


-- Query use in dbeaver
select id, SEARCHKEY, TAXID, NAME, ADDRESS, POSTAL, FIRSTNAME, LASTNAME, 
EMAIL, PHONE, VISIBLE 
from customers c;


-- Result
/*
INSERT INTO customers
(ID, SEARCHKEY, TAXID, NAME, ADDRESS, POSTAL, FIRSTNAME, LASTNAME, EMAIL, PHONE, VISIBLE)
VALUES('0102965123001', '0102965123001', '0102965123001', 'JUAN CARDENAS ', 'IBARRA', 'RUC', 'CARDENAS ', 'JUAN', '', NULL, 1);
*/

update customers
set id = taxid
where length(id)>13;

-- Query use in dbeaver
select REFERENCE as id, REFERENCE ,CODE , 'CODE128' as CODETYPE, NAME , PRICEBUY , 
PRICESELL, CATEGORY , TAXCAT, concat('<html><center>', NAME) as display
from products p ;
-- Result
/*
INSERT INTO products
(id, REFERENCE, CODE, CODETYPE, NAME, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, display)
VALUES('180', '180', '7862110544606', 'CODE128', 'CIFRUT 400ML', 0.27, 0.27, '50aa8905-fa24-4052-9bc3-d65cfbdd68eb', '001', '<html><center>CIFRUT 400ML');
*/
-- Query use in dbeaver
select REFERENCE as id, REFERENCE 
from products p ;
-- Result
/*
INSERT INTO products_cat
(product, catorder)
VALUES('1', '1');
*/

ALTER TABLE `ticketlines` 
DROP FOREIGN KEY `TICKETLINES_FK_2`;
ALTER TABLE `ticketlines` 
DROP INDEX `TICKETLINES_FK_2` ;

update ticketlines t 
inner join products p 
on p.ID = t.PRODUCT 
set t.PRODUCT = p.REFERENCE ;

delete from products_cat;
delete from stockcurrent;
delete from stockdiary;

update products
set id = reference;

ALTER TABLE `receipts` 
ADD COLUMN `person` VARCHAR(255) NULL AFTER `ATTRIBUTES`;

update receipts 
set person = '001-101';

-- mysqldump -u root -p --no-create-db --no-create-info openbravo receipts > receipts.sql
-- mysql -u root -p allkupos < receipts.sql

ALTER TABLE `tickets` 
ADD COLUMN `documento` VARCHAR(90) NULL AFTER `STATUS`;

update tickets
set documento = concat('001101', lpad(TICKETID, 9, '0'));

-- mysqldump -u root -p --no-create-db --no-create-info openbravo tickets > tickets.sql
-- mysql -u root -p allkupos < tickets.sql

-- mysqldump -u root -p --no-create-db --no-create-info openbravo ticketlines > ticketlines.sql
-- mysql -u root -p allkupos < ticketlines.sql

ALTER TABLE `payments` 
ADD COLUMN `tip` DOUBLE NULL DEFAULT 0 AFTER `TOTAL`,
ADD COLUMN `isprocessed` BIT(1) NULL DEFAULT b'0' AFTER `TRANSID`,
ADD COLUMN `notes` VARCHAR(255) NULL AFTER `RETURNMSG`,
ADD COLUMN `tendered` DOUBLE NULL AFTER `notes`,
ADD COLUMN `cardname` VARCHAR(255) NULL AFTER `tendered`,
ADD COLUMN `voucher` VARCHAR(255) NULL AFTER `cardname`;

-- mysqldump -u root -p --no-create-db --no-create-info openbravo payments > payments.sql
-- mysql -u root -p allkupos < payments.sql

UPDATE tickets 
set person = '001-101';