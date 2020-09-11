# AllkuPos_Ec

### 1. Archivo Sql para crear la base de datos
src/main/resources/com/openbravo/pos/scripts/MySQL-create.sql

### 2. Archivos de plantilla para información de la compañía 
* Empresa.Ambiente.txt
* Empresa.Direccion1.txt
* Empresa.Direccion2.txt
* Empresa.Establecimiento.txt
* Empresa.NombreComercial.txt
* Empresa.PuntoEmision.txt
* Empresa.RUC.txt
* Empresa.RazonSocial.txt

### 3. Plantilla de ticket de impresora y vista previa de la impresora
src/main/resources/com/openbravo/pos/templates
* Printer.Ticket.xml
* Printer.TicketPreview.xml

### 4. Datos de ticket de la impresora
paquete: com.openbrevo.pos_ticket

clase: TicketInfo.java

métodos: get & set

Añadidos métodos getters y setters:
* ruc
* razonSocial
* nombreComercial
* direccion1
* direccion2
* establecimiento
* puntoEmision
* ambiente

y métodos:

* printClaveAcceso
* getSecuencial
* printSecuencial

### 5. Añadido Módulo 11 
class: expert.allku.electronico

### 6. Variables cargadas de los datos de ticket de la impresora

* package: com.openbravo.pos.sales

class: JPanelTicket

method: closeTicket

* package: com.openbravo.pos.sales

class: JTicketsBagTicket

method: readTicket

### 7. Pantalla de Gestión de clientes

Actualizado para clientes de Ecuador.

src/main/java/com/openbravo/pos/customers/CustomersView.java

### 8. Información de ticket para imprimir

src/main/java/com/openbravo/pos/customers/CustomerInfoExt.java

src/main/java/com/openbravo/pos/ticket/TicketLineInfo.java

src/main/java/com/openbravo/pos/ticket/TicketTaxInfo.java

### 9. Configuración de la base de datos y otras configuraciones
paquete: com.openbravo.pos.forms

clase: AppConfig

### 10. Pantalla principal
paquete: com.openbravo.pos.forms

clase: JRootApp

### 11. Archivo compilado

Linux & Mac: compile.AllkuPos_Ec

### 12. Selección del pago e ingreso de la información del cliente
paquete: com.openbravo.pos.payment

clase: JPaymentSelect

Cuando guarde el ticket

método: m_jButtonOKActionPerformed

En este método, se pone el cliente y su información comercial para imprimir en el ticket de venta

paquete: com.openbravo.pos.sales

clase: JPanelTicket

método: closeTicket

Añadidos los métodos get & set m_jtxtTaxID para guardar la selección del cliente en JPaymentSelect

paquete: com.openbravo.pos.customers

clase: JCustomersFinder

### 13. Método para guardar la factura: saveTicket

paquete: com.openbravo.pos.forms

clase: DataLogicSales

### 14. Desactivar la impresión de tickets unicentaPOS
Comenté la Línea para deshabilitar executeEvent

paquete: com.openbravo.pos.sales

clase: jPanelTicket

método: closeticket

deshabilitar: executeEvent

### 15. Ejemplo de conexión de base de datos

paquete: com.unicenta.orderpop

clase: OrderPop

### 16. Pantallas de Configuración
En esta sección aquí toda la configuración inicial

paquete: com.openbravo.pos.config

### 17. Pantalla para devolución de venta
Validar la devolución del detalle de artículos seleccionados.

paquete: com.openbravo.pos.sales

clase: JRefund

Archivo de propiedades:

src/main/resources/pos_messages_es.properties

### 18. Referencia de producto de autoincremento

Se cambio el cuadro de texto m_jRef que es la columna de referencia de la tabla de productos. Ahora se incrementa automáticamente y no se puede editar.

Se cambio la etiqueta label.prodbarcodem en el archivo pos_messages_es.properties.

Cuando seleccione el elemento, vaya a la pestaña Inventario y presione "tener almacen" para que funcione correctamente cuando sea necesario presionar el botón Configurar variable

paquete: com.bravo.pos.inventory

clase: ProductsEditor 

@@sql_mode

Eliminar la opción ONLY_FULL_GROUP_BY

### 19. Fijado el precio del producto con punto y coma
Se agregó la instrucción replace (".", ",")

paquete: com.openbravo.pos.inventory

clase: ProductsEditor

método: calculatePriceSellfromPST


### 20. Precio de compra de producto establecido con punto y coma
Se agregó la instrucción replace (".", ",")

paquete: com.openbravo.pos.inventory

clase: ProductsEditor

métodos:
* crear valor
* calcularMargen
* calcular GP
* CalculatePriceSellfromMargin


### 21. Inactivo botón Cliente en productos divididos
Añadido cliente seleccionado en pago después de dividir los productos

paquete: com.openbravo.pos.sales

clase: JPanelTicket

Inactivo botón  seleccionar cliente.

clase: SimpleReceipt

Razón: es mejor seleccionar al cliente en el pago

### 22. Intercambio de clientes entre JPanelTicket y JPaymentselect

paquete: com.openbravo.pos.sales

clase: JPanelTicket

método: stateTransition 

método: jBtnCustomerActionPerformed

paquete: com.openbravo.pos.peyment

clase: JPaymentSelect

método: showDialog

Eliminado, crear cliente en jBtnCustomerActionPerformed

### 23. Reporte de impuestos de ventas
Añadido valores base y total base

carpeta de ruta: src/main/resources/com/openbravo/reports/

archivos:
* sales_taxes.jrxml
* sales_taxes_messages.properties
* sales_taxes_messages_es.properties

### 24. Se agregó capu.png como logotipo
Cambio de código y en el diseño de la pantalla.

archivo: src/main/resources/com/openbravo/images/capu.png

paquete: com.openbravo.pos.forms

clase: JRootApp

### 25. Se corrigió el cambio de instrucción SQL a minúsculas
paquete: com.openbravo.pos.payment

clase: JPaymentSelect

métodos:
* getClient ()
* saveClient ()

### 26. Actualizar el índice de secuencia de tickets
Estos métodos convierten el valor de retorno de BigInteger en Integer

paquete: com.openbravo.pos.forms

clase: DataLogicSales
métodos:
* getNextPickupIndex ()
* getNextTicketIndex ()
* getNextTicketRefundIndex ()
* getNextTicketPaymentIndex ()

Actualizado el valor de retorno de INT a OBJECT

paquete: com.openbravo.data.loader

clase: SerializerReadInteger

método: readValues()

### 27. Agregada Secuencia por usuario (Persona)
Añadida la instrucción sql where en secuencia

paquete: com.openbravo.pos.forms

clase: DataLogicSales

método: getNextTicketIndex (String person)

paquete: com.openbravo.data.loader

clase: SessionDB

método: getSequenceSentence (Sessions, String sequence, String person);

paquete: com.openbravo.data.loader

clase: SequenceForMySQL

método: SequenceForMySQL (Session s, String sSeqTable, String person);

### 28. Añadida carga de ticket con nombre de usuario (nombre o persona)
Se agregó un nuevo parámetro para la consulta, ahora se busca adicionalmente por persona (usuario)

paquete: com.openbravo.pos.forms

clase: DataLogicSales

método: loadTicket(final int tickettype, final int ticketid, final String person) 
    
Añadido como predeterminado el parámetro de nombre de usuario en loadTicket 

paquete: com.openbravo.pos.sales

clase: JPanelTicket

método: btnReprint1ActionPerformed

Añadido parámetro name para la llamada de la función loadTicket

paquete: com.openbravo.pos.sales

clase: JTicketsBagticket

método: readTicket (int iTicketid, int iTickettype, String name)

Añadido usuario como predeterminado para la llamada de la función readTicket. 

método: jButton1ActionPerformed

método: m_jkeysActionPerformed

Añadido Usuario del Ticket seleccionado para llamar a la función readTicket

método: jButton2ActionPerformed

El name es el nombre de usuario.

### 29. Se actualizó la pantalla de usuario para ingresar el código de usuario manualmente
Añadido input  para el código del usuario.

paquete: com.openbravo.pos.admin

clase: PeopleView

métodos:
* writeValueEOF()
* writeValueInsert()
* writeValueDelete(Object value)
* writeValueEdit(Object value)
* createValue()

### 30. Se eliminó Establecimiento y Punto de emisión por parámetros de recursos y agregado código de usuario  como serie (Establecimiento y Punto de Emisión)
Eliminadodo establecimiento y punto de emisión y agregado id de usuario.

archivo: src/main/resources/com/openbravo/pos/scripts/MySQL-create.sql

paquete: com.openbravo.pos.ticket

clase: TicketInfo

método: printSecuencial()

Eliminados recursos de parámetros de establecimiento y punto de emisión.

paquete: com.openbravo.pos.sales

clase: JPanelTicket

método: closeTicket (TicketInfo ticket, Object ticketext)

clase: JTicketsBagTicket

método: readTicket (int iTicketid, int iTickettype, String name)

### 31. Añadida pantalla  para administrar secuencias de tickets
Añadida pantalla gráfica para administrar  secuencias de ventas.

paquete: com.openbravo.pos.admin

clase: SecuencesPanel

clase: SecuencesEditor

Añadido método para obtener información de la tabla ticketsnum.

paquete: com.openbravo.pos.forms

clase: DataLogicSales

método: getTableTicketsNum ()

Añadidas líneas para agregar menú a la interfaz gráfica de usuario.

archivo: src/ main/resources/com/openbravo/pos/templates/Menu.Root.txt

archivo: src/main/resources/com/openbravo/pos/templates/Role.Administrator.xml

archivo: src/main/resources/com/openbravo/pos/templates/Role.Manager.xml

Añadido para mostrar en la tabla.

paquete: com.openbravo.pos.admin

clase: PeopleNotSequence

### 32. Seleccionado Cliente predeterminado  en detalle de ticket dividido
Se agregó getClient para llamar a Consumidor Final.

paquete: com.openbravo.pos.sales

clase: JPanelTicket

método: btnSplitActionPerformed

### 33. Añadida una nueva pantalla en blanco para futuras opciones de configuración
Añadido para futuras utilidades de configuración.

paquete: com.openbravo.pos.config

clase: ConfigAllkuPos

### 34. Añadido una nueva columna **documento** para guardar el número de factura y el número de serie en una única columna
Concat person Id y leftpad el ticketid.

Asigne clave única para la tabla tickets.

paquete: com.openbravo.pos.forms

clase: DataLogicSales

método: saveTicket

DML añadido en:
src / main / resources / com / openbravo / pos / scripts / MySQL-create.sql

### 35. Añadida Secuencia por usuario (Persona) en devolución
Después de la tabla ticketsnum_refund agregada la columna person, esta columna hace referencia a la tabla people.
Personas es el usuario POS.

Esta actualización hace una devolución con secuencia independiente.

paquete: com.openbravo.pos.forms

clase: DataLogicSales

método: getNextTicketRefundIndex (String person)

método: saveTicket

### 36. Versión AllkuPOS Ec

Actualizar variables estáticas finales 

APP_NAME = "AllkuPOSEc"

APP_VERSION = "0.1.0"

Para cambiar la versión AllkuPOS Ec

paquete: com.openbravo.pos.forms

clase: AppLocal

### 37. Cuando se crea cliente en administración -> Clientes -> Managed, se asigna clientes en ventas.
Cuando se crea el cliente muestra un mensaje para redirigirse a la pantalla de la factura y asignar el cliente y la opción de pago

paquete: com.openbravo.data.user

clase: BrowsableEditableData

método: saveData

### 38. Añadida pantalla para administrar la secuencia de tickets de devolución

Añadida pantalla gráfica para la secuencia de devolución 

paquete: com.openbravo.pos.admin

clase: SequenceRefundPanel

clase: SequenceRefundEditor

Añadidas líneas para agregar menú a la interfaz gráfica de usuario

Ruta: src/main/resources/com/openbravo/pos/templates/

Archivos:
* Menu.Root.txt
* Role.Administrator.xml
* Role.Manager.xml

Añadido parámetro, el parámetro es el nombre de la tabla ticketsnum o ticketsnum_refund

paquete: com.openbravo.pos.forms

clase: DataLogicsales

método: getUserWithOutSequenceList

Añadida propiedad de mensaje MenuSequenceRefund

Archivos: src / main / resources / pos_messages *








