# Ficha Técnica de ALNILAM 

Proyecto generado para la configuración automática de microservicios con OpenJdk 17 + Spring Boot 3.2.5

## Requisitos

* [Instalar NodeJs LTS](https://nodejs.org/es/)
* [Instalar Java JDK 17 o Superior](https://www.oracle.com/mx/java/technologies/javase/jdk11-archive-downloads.html)

## Configuración del Servicio

Para realizar la configuración del servicio, se utilizan los siguientes archivos:

* Esquema de Base de Datos: [generators/app/templates/sql/schema.sql](generators/app/templates/sql/schema.sql)

* Datos de ejemplo usados en los Casos de Uso: [generators/app/templates/sql/data.sql](generators/app/templates/sql/data.sql)

* Contenido: Para realizar la configuración del servicio y los casos de uso que se deben generar, se  realiza en el archivo [generators/app/config.json](generators/app/config.json)

Se indica:
* El nombre de la compañía.
* El nombre del servicio. 
* La versión de maven del proyecto.
* El puerto sobre el que se levanta el microservicio.
* El nombre del usuario (autor).
* La bandera lombok para emplear las anotaciones @Getter/@Setter en los DTOs y entidades.
* La bandera de redis para indicar si se habilita el servicio de cache de redis.
* La configuración de 1 o más controladores (microservicios).

```json
{
    "company": "Axity",
    "name": "Office",
    "version": "1.0.0",
    "port": 9090,
    "username": "username@axity.com",
    "lombok": true,
    "redis": false,
    "controllers": [ ... ]
}
```

Los controladores es un arreglo de json que configura cada microservicio, su DTO y entity correspondiente.

* El nombre lógico de la entidad (en PascalCase).
* El nombre o ruta de la api.
* El nombre de la entidad (en PascalCase).
* El nombre de la entidad (en PascalCase).
* El nombre físico de la tabla.
* Las propiedades (atributos) de la entidad.

```json
{
    "name": "Office",
    "apiName": "/api/offices",
    "nameCamel": "Office",
    "entity": "Office",
    "entityLower": "office",
    "table": "TBL_Office",
    "properties": [...]
}
```

Las propiedades o atributos es un arreglo de json que configura cada DTO y entidad.

* El nombre del atributo.
* El nombre del atributo en camel case.
* El tipo del atributo, se deben emplear primitivos, wrappers, String, Date, Entidades o List<Entidad>.
* El tipo del atributo del DTO, se deben emplear primitivos, wrappers, String, Date, Entidades o List<Dto>.
* La descripción para swagger.
* El nombre físico de la columna, puede ser null en el caso de mapeos de @OneToMany, @ManyToOne, @ManyToMany. 
* Bandera **primaryKey**, indica si se empleará la anotación @Id.
* Bandera **manyToOne**, indica si se empleará la anotación @ManyToOne. En caso de que sea true es requerido indicar la configuración **manyToOneConfig**.
* Bandera **oneToMany**, indica si se empleará la anotación @OneToMany. En caso de que sea true es requerido indicar la configuración **oneToManyConfig**.
* Bandera **manyToMany**, indica si se empleará la anotación @ManyToMany. En caso de que sea true es requerido indicar la configuración **manyToManyConfig**.
* Bandera **timestamp**, indica si se empleará la anotación @Temporal(TemporalType.TIMESTAMP).
* Bandera **date**, indica si se empleará la anotación @Temporal(TemporalType.DATE).
* Bandera **time**, indica si se empleará la anotación @Temporal(TemporalType.TIME).
* Bandera **update**, indica si llamará el atributo a actualizar en el método Update del microservicio. Importante, sólo puede usarse en primitivos, wrapper o String.
* Bandera **transient**, indica si se omite la serialización del atributo para la librería Gson. Importante, esto debe activarse en las llamadas bidireccionales entre las entidades para evitar anidación cíclica.
* Bandera **jsonIgnore**,indica si se omite la serialización del atributo para la librería Jackson. Importante, esto debe activarse en las llamadas bidireccionales entre las entidades para evitar anidación cíclica.

```json
{
    "name" : "Id",
    "nameCamel" : "id",
    "type" : "[int|Integer|String|Date|EntityDO]",
    "typeDto" : "[int|Integer|String|Date|EntityDto]",
    "description": "The id",
    "column": "cd_id" | null,
    "primaryKey": true,
    "manyToOne": false,
    "manyToOneConfig" : null | {...},
    "oneToMany": false,
    "oneToManyConfig": null  | {...},
    "manyToMany" : false,
    "manyToManyConfig": null  | {...},
    "timestamp": false,
    "date": false,
    "time": false,
    "update": false,
    "transient": false,
    "jsonIgnore": false
}
```

La configuración de @ManyToOne, requiere indicar:
* la columna de la tabla de la entidad (**joinColumn**) y 
* la columna de la tabla de la entidad referenciada (**joinReferenceColumn**)

```json
{
    "manyToOneConfig":{
        "joinColumn": "cd_country",
        "joinReferenceColumn": "cd_id"
    }
}
```   

La configuración @OneToMany, requiere indicar:
* el nombre lógico de la entidad.

```json
{
    "oneToManyConfig": {
        "mappedBy": "country"
    }
}
``` 

La configuración @ManyToMany permite emplear una tabla relacional MxN, hay dos posibles configuraciones:

* Indicando que la anotación está *mapeada* en la relación @ManyToMany de la otra entidad.
* Requiere indicar el nombre de la entidad *mapeada*.

```json
{
    "manyToManyConfig": {
        "mapped": true,
        "mappedBy": "roles"
    }
}
``` 
La otra configuración posible es indicando:
* El nombre de la tabla de relación (jointTable).
* El nombre de la columna la tabla de relación (joinColumn) y su nombre físico en la tabla (joinReferenceColumn).
* El nombre de la columna en la tabla de relación hacia la otra entidad (inverseJoinColumn) y su nombre físico en la tabla relacionada (inverseJoinReferenceColumn).
* La bandera mapped debe ser falsa.
* El atributo mappedBy no es requerido, puede ser nulo.

```json
{
"manyToManyConfig": {
                    "joinTable": "TRL_UserRole",
                    "joinColumn": "cd_user",
                    "joinReferenceColumn": "cd_id",
                    "inverseJoinColumn": "cd_role",
                    "inverseJoinReferenceColumn": "cd_id",
                    "mapped": false,
                    "mappedBy": null
                }
}
``` 

## Generar Servicio

Para generar el servicio e instalación de los paquetes utilizados por el mismo, se deben seguir los siguientes pasos:

* Extraer el repositorio en una ruta adecuada destinada para él.
* Abrir la carpeta con un editor de texto, se recomienda el IDE de Visual Studio Code.
* Instalar el Yeoman, ejecutando el siguiente comando:

```bash
npm install -g yo
```

Tal como se visualiza:

![Audience](../../assets/VisualNpmInstallYo.png)

* Ejecutar el siguiente comando dentro de la ruta .../jdk17/generator/

```bash
npm link
```

Tal como se visualiza:

![Audience](../../assets/VisualNpmLink.png)

* Ejecutar el siguiente comando para descargar los paquetes y dependencias, tal como se visualiza:

```bash
npm install
```

![Audience](../../assets/VisualNpmInstall.png)

* Ejecutar el siguiente comando para generar el servicio, tal como se visualiza:

```bash
yo jdk17-springboot3
```
--- 

* SI EXISTE AULGUN PROBLEMA A LA HORA DE GENERAR EL ARQUETIPO *

    Revisar que la version de yeoman en el archivo package.json sea una version valida (5.0.0 hasta el momento), y ejecutar despues de la actualización del archivo el siguiente comando :

    ```bash
    npm update
    ```

---



* Cuándo se genera el nuevo servicio, podemos visualizar la estructura de los siguientes módulos:

1. Application
2. API
3. Facade
4. Services
5. Persistence
6. Model
7. Commons

## Configuración Local

Para realizar el despliegue del Servicio de manera local, se deben realizar los siguientes pasos:

* En pasos anteriores, al ejecutar el comando *yo jdk17-springboot3*, se crea una estructura de carpetas en la ruta: *jdk17\generator* con el nombre del servicio especificado en el archivo de configuración *config.json* (Por default el servicio se crea como office-services), acceder a la carpeta y ejecutar el siguiente comando para descargar y compilar los paquetes necesarios desde una terminal:

```bash
mvn install
```

Tal como se visualiza a continuación:

![Audience](../../assets/MvnInstall.png)

* Ejecutar el siguiente comando:

```bash
mvn package
```

Tal como se visualiza:

![Audience](../../assets/MvnPackage.png)

* Adicional, podemos ejecutar las pruebas unitarias integradas al proyecto generado, se puede realizar ejecutando el siguiente comando:

```bash
mvn test
```

Tal como se visualiza:

![Audience](../../assets/MvnTest.png)

* Acceder desde la ruta: *\axity.office-api\target* y ejecutar el siguiente comando (office-api-1.0.0.jar es el nombre del archivo generado con la ejecución de los comandos anteriores):

```bash
java -jar office-api-1.0.0.jar
```

Tal como se visualiza:

![Audience](../../assets//java%20jar.png)

Después de la ejecución del comando anterior, el Servicio se despliega y ejecuta en el localhost, por lo que se puede acceder a él mediante: http://localhost:9090/swagger-ui/index.html#/.

## Casos de Uso de Ejemplo

ALNILAM incorpora varios casos de uso de ejemplo, si se toma la configuración del puerto por default (9090) del archivo de configuración, al momento de correr el servicio, se puede acceder a la url: http://localhost:9090/swagger-ui/index.html#/, donde se observan:

![Audience](../../assets/DemoUseCases.png)

A continuación, se especifica la funcionalidad que ejemplifican los casos de uso, así como las peticiones GET y POST de cada uno de ellos:

## Manage User

**ALNILAM** dispone de la arquitectura de componentes diseñadas para implementar los Methods HTTP CRUD que serán expuestos a través del servicio. Para lo cual ejemplifica los siguientes métodos:

![Audience](../../assets/MethodsAdministratorUsers.png)

**Descripción del Methods - GET**

* **Funcionalidad:** Obtener los usuarios de forma paginada.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** 

    -	Limit – integer
    -	Offset - integer

* **Ejemplo:** http://localhost:9090/api/users?limit=2&offset=1.


* **Http Verb:** GET 

* **Response:** 200 OK

```json
{
   {"header":{"code":0,"message":"OK"},"page":0,"size":2,"pages":10,"registries":20,"data":[{"id":1,"username":"guy.stark","name":"Guy","lastName":"Stark","roles":[{"id":1,"name":"Ventas"}]},{"id":2,"username":"denise.alford","name":"Denise","lastName":"Alford","roles":[{"id":1,"name":"Ventas"}]}]}
}
``` 

**Descripción del Methods - POST**

* **Funcionalidad:** Método que permite agregar un nuevo Usuario.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** N/A

* **Body:**

```json
{
  "id": 0,
  "username": "string",
  "name": "string",
  "lastName": "string",
  "roles": [
    {
      "id": 0,
      "name": "string"
    }
  ]
}
``` 

* **Ejemplo:** http://localhost:9090/api/users.

* **Http Verb:** POST 

* **Response:** 201 CREATED

```json
{
  "header": {
    "code": 0,
    "message": "string",
    "detail": "string"
  },
  "body": {
    "id": 0,
    "username": "string",
    "name": "string",
    "lastName": "string",
    "roles": [
      {
        "id": 0,
        "name": "string"
      }
    ]
  }
}
``` 

## Manage Territorys

**ALNILAM** dispone de la arquitectura de componentes diseñadas para implementar los Methods HTTP CRUD que serán expuestos a través del servicio. Para lo cual ejemplifica los siguientes métodos:

![Audience](../../assets/TerritoryManagerMethods.png)

**Descripción del Methods - GET**

* **Funcionalidad:** Obtener los territorios de forma paginada.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:**

    - Limit – integer
    - Offset - integer

* **Ejemplo:** http://localhost:9090/api/territories?limit=2&offset=1.

* **Http Verb:** GET 

* **Response:** 200 OK 

```json
{"header":{"code":0,"message":"OK"},"page":0,"size":2,"pages":2,"registries":4,"data":[{"id":1,"name":"NA"},{"id":2,"name":"EMEA"}]}
```

**Descripción del Methods - POST**

* **Funcionalidad:** Método que permite agregar un nuevo Territorio.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** N/A

* **Body:**

```json
{
  "id": 0,
  "name": "México"
}
```

* **Ejemplo:** http://localhost:9090/api/territories.

* **Http Verb:** POST 

* **Response:** 201 CREATED

```json
{"header":{"code":0,"message":"OK"},"body":{"id":102,"name":"México"}}
```

## Manage Roles

**ALNILAM** dispone de la arquitectura de componentes diseñadas para implementar los Methods HTTP CRUD que serán expuestos a través del servicio. Para lo cual ejemplifica los siguientes métodos:

![Audience](../../assets/MethodsAdministratorRoles.png)

**Descripción del Methods - GET**

* **Funcionalidad:** Obtener los roles de forma paginada.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:**

    - Limit – integer
    - Offset - integer

* **Ejemplo:** http://localhost:9090/api/roles?limit=2&offset=1.

* **Http Verb:** GET 
	
* **Response:** 200 OK 

```json
{"header":{"code":0,"message":"OK"},"page":0,"size":2,"pages":2,"registries":4,"data":[{"id":1,"name":"Ventas"},{"id":2,"name":"AlmacÃ©n"}]}
```

**Descripción del Methods - POST**

* **Funcionalidad** Método que permite agregar un nuevo Rol.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** N/A

* **Body:**

```json
{
  "id": 0,
  "name": "Inventarios"
}
```

* **Ejemplo:** http://localhost:9090/api/roles.

* **Http Verb:** POST 

* **Response:** 201 CREATED

```json
{"header":{"code":0,"message":"OK"},"body":{"id":102,"name":"Inventarios"}}

```

## Manage Offices

**ALNILAM** dispone de la arquitectura de componentes diseñadas para implementar los Methods HTTP CRUD que serán expuestos a través del servicio. Para lo cual ejemplifica los siguientes métodos:

![Audience](../../assets/OfficeAdministratorMethods.png)

**Descripción del Methods - GET**

* **Funcionalidad:** Obtener las oficinas de forma paginada.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:**

    - Limit – integer
    - Offset - integer

* **Ejemplo:** http://localhost:9090/api/offices?limit=2&offset=1.

* **Http Verb:** GET

* **Response:** 200 OK 

```json
{"header":{"code":0,"message":"OK"},"page":0,"size":2,"pages":4,"registries":7,"data":[{"id":1,"city":"San Francisco","phone":"+1 650 219 4782","addressLine1":"100 Market Street","addressLine2":"Suite 300","state":"CA","country":{"id":1,"name":"USA","territory":{"id":1,"name":"NA"}},"postalCode":"94080"},{"id":2,"city":"Boston","phone":"+1 215 837 0825","addressLine1":"1550 Court Place","addressLine2":"Suite 102","state":"MA","country":{"id":1,"name":"USA","territory":{"id":1,"name":"NA"}},"postalCode":"02107"}]}
```

**Descripción del Methods - POST**

* **Funcionalidad:** Método que permite agregar una nueva Oficina.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** N/A

* **Body:**

```json
{
  "id": 0,
  "city": "México",
  "phone": "5555555555",
  "addressLine1": "Privada",
  "addressLine2": "Privada",
  "state": "México",
  "country": {
    "id": 1,
    "name": "México",
    "territory": {
      "id": 102,
      "name": "México"
    }
  },
  "postalCode": "86000"
}
```

* **Ejemplo:** http://localhost:9090/api/offices.

* **Http Verb:** POST 

* **Response:** 201 CREATED

```json
{"header":{"code":0,"message":"OK"},"body":{"id":100,"city":"México","phone":"5555555555","addressLine1":"Privada","addressLine2":"Privada","state":"México","country":{"id":1,"name":"USA","territory":{"id":102,"name":"México"}},"postalCode":"86000"}}
```

## Manage Countrys

**ALNILAM** dispone de la arquitectura de componentes diseñadas para implementar los Methods HTTP CRUD que serán expuestos a través del servicio. Para lo cual ejemplifica los siguientes métodos:

![Audience](../../assets/CityManagerMethods.png)

**Descripción del Methods - GET**

* **Funcionalidad:** Obtener los países de forma paginada.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:**

    - Limit – integer
    - Offset - integer

* **Ejemplo:** http://localhost:9090/api/countries?limit=2&offset=1.

* **Http Verb:** GET 

* **Response:** 200 OK 

```json
{"header":{"code":0,"message":"OK"},"page":0,"size":2,"pages":3,"registries":5,"data":[{"id":1,"name":"USA","territory":{"id":1,"name":"NA"}},{"id":2,"name":"France","territory":{"id":2,"name":"EMEA"}}]}
```

**Descripción del Methods - POST**

* **Funcionalidad:** Método que permite agregar un nuevo país.

* **Url:** Dirección Ip donde se encuentre publicado el servicio (Se utiliza localhost de prueba).

* **Puerto:** Puerto donde se encuentre publicado el servicio (Se utiliza el puerto 9090 de prueba).

* **Parámetros:** N/A

* **Body:**

```json
{
  "id": 0,
  "name": "México",
  "territory": {
    "id": 1,
    "name": "México"
  }
}
```

* **Ejemplo:** http://localhost:9090/api/countries.

* **Http Verb:** POST 

* **Response:** 201 CREATED

```json
{"header":{"code":0,"message":"OK"},"body":{"id":100,"name":"México","territory":{"id":1,"name":"México"}}}
```

## Coverage-module
Se añadió un módulo adicional, con la finalidad de poder concentrar todos los reportes generados por JaCoCo. Este módulo se llama *coverage-module* y para ejecutarlo basta con ejecutar:
```bash
mvn verify -DskipTests
```
Se sugiere anteriormente haber hecho un ```mvn install``` o ```mvn package```, con las pruebas unitarias, y no volver a ejecutarlas en el *verify*.

## Contributors

Javier Rodríguez [francisco.rodriguez@axity.com]  

Hugo Meraz [hugo.meraz@axity.com]

Guillermo Segura Olivera [guillermo.segura@axity.com]  

Cesar Cruz Arredondo [cesar.cruz@axity.com]  

José Alejandro Barbosa Casillas [jose.barbosa@axity.com]  

## License

[MIT](https://opensource.org/licenses/MIT)

## CReA

<div align="center">
  <img src="assets/CReA.png" alt="CReA - Componentes Reutilizables Aceleradores">
</div>