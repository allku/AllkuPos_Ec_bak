# AllkuPos_Ec

### 1. Sql file to create database
src/main/resources/com/openbravo/pos/scripts/MySQL-create.sql

### 2. Template files to ticket data company information
* Empresa.Ambiente.txt
* Empresa.Direccion1.txt
* Empresa.Direccion2.txt
* Empresa.Establecimiento.txt
* Empresa.NombreComercial.txt
* Empresa.PuntoEmision.txt
* Empresa.RUC.txt
* Empresa.RazonSocial.txt

### 3. Template printer ticket and printer preview
src/main/resources/com/openbravo/pos/templates
* Printer.Ticket.xml
* Printer.TicketPreview.xml

### 4. Printer data ticket
package: com.openbrevo.pos_ticket

class: TicketInfo

methods: get & set

Added method getters and setters:
* ruc
* razonSocial
* nombreComercial
* direccion1
* direccion2
* establecimiento
* puntoEmision
* ambiente

and methods:

* printClaveAcceso
* getSecuencial
* printSecuencial

### 5. Added Modulo11 
class: expert.allku.electronico

### 6. Load variables of Printer data ticket

* package: com.openbravo.pos.sales

class: JPanelTicket

method: closeTicket

* package: com.openbravo.pos.sales

class: JTicketsBagTicket

method: readTicket

### 7. Customer management screen
Update for EC.

src/main/java/com/openbravo/pos/customers/CustomersView.java

### 8. Tickect information to print
src/main/java/com/openbravo/pos/customers/CustomerInfoExt.java

src/main/java/com/openbravo/pos/ticket/TicketLineInfo.java

src/main/java/com/openbravo/pos/ticket/TicketTaxInfo.java

### 9. Database configurations and others configurations
package: com.openbravo.pos.forms

class: AppConfig

### 10. Screen main
package: com.openbravo.pos.forms

class: JRootApp

### 11. Compile file

Linux & Mac: compile.AllkuPos_Ec

### 12. Payment select and input client information
package: com.openbravo.pos.payment

class: JPaymentSelect

When save the tickect 

method: m_jButtonOKActionPerformed

In this method, the customer and their commercial information are put to print on the ticket

package: com.openbravo.pos.sales

class: JPanelTicket

method: closeTicket

Added methods get & set m_jtxtTaxID to save client select in JPaymentSelect

package: com.openbravo.pos.customers

class:JCustomersFinder

### 13. Method to save invoice

package: com.openbravo.pos.forms

class: DataLogicSales

method: saveTicket

### 14. Disable ticket print unicentaPOS
Comment line to disable executeEvent

package: com.openbravo.pos.sales

class: jPanelTicket

method: closeticket

disable: executeEvent

### 15. Connection database example

package: com.unicenta.orderpop

class: OrderPop

### 16. Configuration screens

In the section here all initial configuration

package: com.openbravo.pos.config

### 17. Screen to refound the invoice
Validate select items detail refund

package: com.openbravo.pos.sales

class: JRefund

File properties: 

src/main/resources/pos_messages_es.properties

### 18. Autoincrement product reference

Change the textbox m_jRef that is refence column of products table. Now is auto-increment and no editable.

Change label label.prodbarcodem in file pos_messages_es.properties.

When select item, go to Inventario tab and push "tener almacen" for functionality correctly when push button is necesary configure variable

package: com.bravo.pos.inventory

class: ProductsEditor 

@@sql_mode

Remove option ONLY_FULL_GROUP_BY

### 19. Price product set with dot and comma
Added instruction replace(".",",")

package: com.openbravo.pos.inventory

class: ProductsEditor

method: calculatePriceSellfromPST

### 20. Buy price product set with dot and comma
Added instruccion replace(".",",")

package: com.openbravo.pos.inventory

class: ProductsEditor

methods: 
* create value
* calculateMargin
* calculateGP
* calculatePriceSellfromMargin

### 21. Inactive button client in split products
Add client selected in payment after split the products

package: com.openbravo.pos.sales

class: JPanelTicket

Inactive button select client

class: SimpleReceipt

Reason: is best select the client in payment

### 22. Interchange of customers betwen JPanelTicket and JPaymentselect
Remove create client in jBtnCustomerActionPerformed

package: com.openbravo.pos.sales

class: JPanelTicket

method: stateTransition 

method: jBtnCustomerActionPerformed

package: com.openbravo.pos.peyment

class: JPaymentSelect

method: showDialog

### 23. Report sales taxes
Added base values and base total

path folder: src/main/resources/com/openbravo/reports/

files: 
* sales_taxes.jrxml
* sales_taxes_messages.properties
*sales_taxes_messages_es.properties

### 24. Added capu.png as logo
Change in code and swing desing.

file: src/main/resources/com/openbravo/images/capu.png

package: com.openbravo.pos.forms

class: JRootApp

### 25. Fix change sql statement to lower case
package: com.openbravo.pos.payment

class: JPaymentSelect

methods: 
* getClient()
* saveClient()

### 26. Update get next ticket index
This methods convert return value of BigInteger to Integer

package: com.openbravo.pos.forms

class: DataLogicSales
methods:
* getNextPickupIndex()
* getNextTicketIndex()
* getNextTicketRefundIndex()
* getNextTicketPaymentIndex()

Update return value from INT to OBJECT

package: com.openbravo.data.loader

class: SerializerReadInteger

method: readValues()

### 27. Added sequence by user (People)
Added where sql instruction in secuence

package: com.openbravo.pos.forms

class: DataLogicSales

method: getNextTicketIndex (String person)

package: com.openbravo.data.loader

class: SessionDB

method: getSequenceSentence(Sessions, String sequence, String person);

package: com.openbravo.data.loader

class: SequenceForMySQL

method: SequenceForMySQL(Session s, String sSeqTable, String person);

### 28. Added load ticket with username (Name or Person)
Added new parameter for query, now search additional for person (user)

package: com.openbravo.pos.forms

class: DataLogicSales

method: loadTicket(final int tickettype, final int ticketid, final String person)

Added in loadTicket user name parameter default

package: com.openbravo.pos.sales

class: JPanelTicket

method: btnReprint1ActionPerformed

Added name parameter for call loadTicket function

package: com.openbravo.pos.sales

class: JTicketsBagticket

method: readTicket(int iTicketid, int iTickettype, String name)

Added default user for call read Ticket function

method: jButton1ActionPerformed

method: m_jkeysActionPerformed

Added user of the selected Ticket for call readTicket function.

method: jButton2ActionPerformed

The name is the username.

### 29. Update user screen for input user code manually
Added input for code user.

package: com.openbravo.pos.admin

class: PeopleView

methods:
* writeValueEOF()
* writeValueInsert()
* writeValueDelete(Object value)
* writeValueEdit(Object value)
* createValue()

### 30. Remove Establecimiento and Punto de emision by resources parameters and added code user as serie(Establecimiento and Punto de Emisión)

Remove establecimiento and punto de emisión and added user id.

file: src/main/resources/com/openbravo/pos/scripts/MySQL-create.sql

package: com.openbravo.pos.ticket

class: TicketInfo

method: printSecuencial()

Remove resources parameters of establecimiento and punto de emisión.

package: com.openbravo.pos.sales

class: JPanelTicket

method closeTicket(TicketInfo ticket, Object ticketext)

class: JTicketsBagTicket

method: readTicket(int iTicketid, int iTickettype, String name)



### 31. Added screen for managet ticket sequences
Added screen to gui managed sequences sales.

package: com.openbravo.pos.admin

class: SecuencesPanel

class: SecuencesEditor

Added method to get ticketsnum table information.

package: com.openbravo.pos.forms

class: DataLogicSales

method: getTableTicketsNum()

Added lines for add menu to gui.

file: src/main/resources/com/openbravo/pos/templates/Menu.Root.txt

file: src/main/resources/com/openbravo/pos/templates/Role.Administrator.xml

file: src/main/resources/com/openbravo/pos/templates/Role.Manager.xml

Added for show in table

package: com.openbravo.pos.admin

class: PeopleNotSequence

### 32. Selected default client in split ticket detail
Added getClient to call Consumidor Final.

package: com.openbravo.pos.sales

class: JPanelTicket

method: btnSplitActionPerformed

### 33. Add new screen in blank for future config options
Added for future config utils.

package: com.openbravo.pos.config

class: ConfigAllkuPos

### 34. Added new column **documento** for save invoice number and serial in a only column

Concat person Id and leftpad the ticketid.

Assing unique key to table tickets.

package: com.openbravo.pos.forms

class: DataLogicSales

method: saveTicket

Added DML in: 
src/main/resources/com/openbravo/pos/scripts/MySQL-create.sql

### 35. Added sequence by user (Person) in refund
Affter table ticketsnum_refund add column person, this column reference to people table.
People is the user POS.

This update make refund with independent sequence.

package: com.openbravo.pos.forms

class: DataLogicSales

method: getNextTicketRefundIndex(String person)

method: saveTicket

### 36. AllkuPOS Ec Version

Update static final variables

APP_NAME ="AllkuPOSEc"

APP_VERSION = "0.1.0"

To change version AllkuPOS Ec

package: com.openbravo.pos.forms

class: AppLocal

### 37. When created customer in administration Clientes Managed, assign customers in sales.
When created customer show message to redirect invoice screen and assign customer and payment option

package: com.openbravo.data.user

class: BrowsableEditableData

method: saveData

### 38. Added screen for managed tickets refund sequence

Added screen gui to sequence refund

package: com.openbravo.pos.admin

class: SequenceRefundPanel

class: SequenceRefundEditor

Added lines for add menu to gui

Path: src/main/resources/com/openbravo/pos/templates/

Files: 
* Menu.Root.txt
* Role.Administrator.xml
* Role.Manager.xml

Added parameter, the parameter is the table name ticketsnum or ticketsnum_refund

package: com.openbravo.pos.forms

class: DataLogicsales

method: getUserWithOutSequenceList

Added message property MenuSequenceRefund

Files: src/main/resources/pos_messages*


























