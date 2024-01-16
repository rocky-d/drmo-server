# **drmo-server** (GitHub: *[rocky-d/drmo-server](https://github.com/rocky-d/drmo-server)*)

## Coursework for `Programming 3`

#### Developed by `Rocky Haotian Du` (GitHub: *[rocky-d](https://github.com/rocky-d)*)

#### Last modified date: `2023-11-30`

------

### Outline

- [Introduction](#introduction)
    - [What is it?](#what-is-it)
    - [Why is it called "drmo-server"?](#why-is-it-called-drmo-server)
    - [What are its features?](#what-are-its-features)
- [Environment](#environment)
- [Usage](#usage)
    - [How to run it?](#how-to-run-it)
    - [How to send requests to it?](#how-to-send-requests-to-it)
        - [/registration](#registration)
        - [/coordinates](#coordinates)
        - [/comment](#comment)
    - [How to modify its configuration?](#how-to-modify-its-configuration)
- [Internal](#internal)
    - [HTTP/HTTPS](#httphttps)
    - [JSON](#json)
    - [SQL](#sql)
        - [Entity](#entity)
        - [Relationship](#relationship)
    - [LOG](#log)
- [Exception](#exception)
- [Statistic](#statistic)

------

### Introduction

#### What is it?

**drmo-server** serves as a pivotal tool tailored for the Finnish locals navigating their unique encounters with various deer species. Situated in the heart of Finland's wildlife-rich landscapes, this service is designed as a communal platform enabling the sharing and discovery of coordinates pinpointing sightings of diverse wild deer species. Whether it's the elusive reindeer, majestic moose, or other indigenous deer types, this server streamlines the tracking and reporting of these animals' locations.

In Finland, encounters with wild deer are commonplace, each species carrying its own allure and significance. **drmo-server** acts as a unifying hub, fostering community collaboration to map out these encounters, offering an avenue to record observations, and creating a comprehensive repository of wildlife coordinates. Residents, enthusiasts, and conservationists alike rely on this service to document and access vital information about these awe-inspiring creatures, contributing to both conservation efforts and public awareness.

Driven by a commitment to environmental stewardship and a shared passion for wildlife preservation, **drmo-server** stands as a testament to the Finnish spirit of coexistence with nature. Its functionalities empower individuals to participate actively in safeguarding and appreciating the country's diverse deer population while fostering a sense of community engagement in the realms of conservation and wildlife observation. More importantly, it helps protect people from the harm of wild animals.

#### Why is it called "drmo-server"?

The word **"drmo"** in the project name is actually the abbreviation of the four supported danger types — **DEER**, **REINDEER**, **MOOSE**, and **OTHER**. And it's also an **HTTP/HTTPS server**. That's why it is called **"drmo-server"**. 🎄🎄🎄🦌🦌🦌🌐🌐🌐

You can speak **"Dre-Mo-Server"** or simply **"D-R-M-O-Server"** to call it.

#### What are its functionalities?

- Request Handling
- JSON Data Handling and Responding
- Data Storing and Querying
- User Registration and Authentication
- Password Encryption
- Configuration Customization
- Exception Handling
- Log Record

#### What are its features?

- Interactive Launching
- Multiple Query Modes
- Ordered JSON Data Responding
- Flexible Configuration
- Relational Data Model
- Distributed Modular Architecture
- High Cohesion & Low Coupling
- Clean and Well Commented Code
- Well Maintainability
- Multilevel Log Entry
- Colored Log Printing

------

### Environment

This is only the information of my own development environment:

- Windows 10 Pro
- Java 1.8.0_202
- Maven 3.9.2

------

### Usage

#### How to run it?

Steps:

- Build the project with **Maven**.
- Run the main method in the `ServerLauncher` class to start the server.
- Follow the printed instructions if it's **the first run**.
    - Don't panic if you see an error message. Calm down and read the prompts carefully.
    - Choose whether to load the default configuration. (Type `yes` or `y` and then press `Enter`.)
    - Check and modify (if you need to) the config file.
    - Rerun the program.

Normal:

<p align="left"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231210152412.png" width="60%" height="60%"/></p>

The first run:

<p align="left"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231210151425.png" width="60%" height="60%"/></p>

#### How to send requests to it?

Recommended tools:

- curl: *[link](https://curl.se/)*
- Postman: *[link](https://www.postman.com/)*

Notes (a few important details you need to know):

- The response body can be ***data in JSON array format***, ***plain text***, or ***empty***.
- All characters in the response body are ***`UTF-8` encoded***.
- All terminal/leaf values in the JSON data of the response body from the server are ***`String` type***.
- All password values are ***hashed ciphertexts*** in both the server and database. The encryption process is ***irreversible***.
- The longitude and latitude values queried after being posted to the server may have ***very teeny errors (under ±1e-15 = ±1×10⁻¹⁵)*** due to the characteristics of `double` type.
- All dangertype values queried after being posted to the server are ***all-letters-capitalized***.

Request headers (if it has a request body):

- `Content-Type`: `application/json; charset=utf-8`

An example **curl** command to query coordinate data since 2023 by setting parameters in the request URI (HTTP mode):

```shell
curl.exe -X GET "http://localhost:8001/coordinates?query=sent&downsent=2023-01-01T00:00:00.000Z"
```

##### /registration

###### GET

Send **GET** requests to `/registration` and set parameters according to the specification table below in whether the request URI or JSON-formatted request body (choosing the parameters in request URI first if both options are available) to query **user** data.

Parameter specification table for querying **user** data (fields with `*` are required):

|     query      | other param(s)  | description               |
|:--------------:|:---------------:|---------------------------|
|    username    |    username*    | select by username        |
| hashedpassword | hashedpassword* | select by hashed password |
|     email      |     email*      | select by email           |
|     phone      |     phone*      | select by phone           |
|      all       |    \<none\>     | select all                |
|    \<none\>    |    \<none\>     | select all                |

###### HEAD

**HEAD** requests can be handled in `/registration`.

###### POST

Send **POST** requests to `/registration` and set parameters according to the specification table below in the JSON-formatted request body to insert **user** data.

Parameter specification table for inserting **user** data (fields with `*` are required):

|   field   | description                   |
|:---------:|-------------------------------|
| username* | the username of the user      |
| password* | the password of the user      |
|   email   | the email address of the user |
|   phone   | the phone number of the user  |

##### /coordinates

###### GET

Send **GET** requests to `/coordinates` and set parameters according to the specification table below in whether the request URI or JSON-formatted request body (choosing the parameters in request URI first if both options are available) to query **coordinate** data.

Parameter specification table for querying **coordinate** data (fields with `*` are required):

|  query   |                        other param(s)                         | description                                                                                                                                                                  |
|:--------:|:-------------------------------------------------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|    id    |                              id*                              | select by coordinate id                                                                                                                                                      |
| location | downlongitude<br/>uplongitude<br/>downlatitude<br/>uplatitude | select by longitude range and latitude range<br/>---<br/>default downlongitude: -180<br/>default uplongitude: +180<br/>default downlatitude: -90<br/>default uplatitude: +90 |
|   sent   |                      downsent<br/>upsent                      | select by sent datetime range<br/>---<br/>default downsent: 0001-01-01T00:00:00.000Z<br/>default upsent: 9999-12-31T23:59:59.999Z                                            |
|   user   |                           username*                           | select by username                                                                                                                                                           |
|   all    |                           \<none\>                            | select all                                                                                                                                                                   |
| \<none\> |                           \<none\>                            | select all                                                                                                                                                                   |

###### HEAD

**HEAD** requests can be handled in `/coordinates`.

###### POST

Send **POST** requests to `/coordinates` and set parameters according to the specification table below in the JSON-formatted request body to insert **coordinate** data.

Parameter specification table for inserting **coordinate** data (fields with `*` are required):

|    field    | description                                           |
|:-----------:|-------------------------------------------------------|
|  username*  | the sender's username                                 |
| longitude*  | the longitude value of the coordinate                 |
|  latitude*  | the latitude value of the coordinate                  |
|    sent*    | the sent datetime (yyyy-MM-dd'T'HH:mm:ss.SSSX)        |
| dangertype* | the type of the danger (DEER, REINDEER, MOOSE, OTHER) |
| description | the description of the record                         |

##### /comment

###### GET

Send **GET** requests to `/comment` and set parameters according to the specification table below in whether the request URI or JSON-formatted request body (choosing the parameters in request URI first if both options are available) to query **comment** data.

Parameter specification table for querying **comment** data (fields with `*` are required):

|   query   |   other param(s)    | description                                                                                                                       |
|:---------:|:-------------------:|-----------------------------------------------------------------------------------------------------------------------------------|
| commentid |     commentid*      | select by comment id                                                                                                              |
|   sent    | downsent<br/>upsent | select by sent datetime range<br/>---<br/>default downsent: 0001-01-01T00:00:00.000Z<br/>default upsent: 9999-12-31T23:59:59.999Z |
|    id     |         id*         | select by coordinate id                                                                                                           |
|    all    |      \<none\>       | select all                                                                                                                        |
| \<none\>  |      \<none\>       | select all                                                                                                                        |

###### HEAD

**HEAD** requests can be handled in `/comment`.

###### POST

Send **POST** requests to `/comment` and set parameters according to the specification table below in the JSON-formatted request body to insert **comment** data.

Parameter specification table for querying **comment** data (fields with `*` are required):

|  field   | description                                    |
|:--------:|------------------------------------------------|
|   id*    | the coordinate's id                            |
| comment* | the content of the comment                     |
|  sent*   | the sent datetime (yyyy-MM-dd'T'HH:mm:ss.SSSX) |

#### How to modify its configuration?

The path to the config file `serverLauncher.config.json` is under the root directory of the project. Properly edit it to modify the configuration of the server.

Default configuration with annotations for explanation:

```json
{
  "?LOG": "log config",
  "LOG": {
    "?PATH": "path to log file",
    "PATH": "drmo.server.log"
  },

  "?SQL": "SQLite config",
  "SQL": {
    "?PATH": "path to database file",
    "PATH": "drmo.sqlite.db"
  },

  "?SERVER": "server config",
  "SERVER": {
    "?MODE": "[\"HTTP\"/\"HTTPS\"] whether to run in HTTP mode or HTTPS mode",
    "MODE": "HTTP",

    "?HTTP": "server config when running in HTTP mode",
    "HTTP": {
      "?PORT": "port number to listen on",
      "PORT": 8001,

      "?HOST": "IP address of remote hosts to control access",
      "HOST": "0.0.0.0",

      "?AUTHENTICATION": "[true/false] whether to enable authentication",
      "AUTHENTICATION": false
    },

    "?HTTPS": "server config when running in HTTPS mode",
    "HTTPS": {
      "?PORT": "port number to listen on",
      "PORT": 8001,

      "?HOST": "IP address of remote hosts to control access",
      "HOST": "0.0.0.0",

      "?AUTHENTICATION": "[true/false] whether to enable authentication",
      "AUTHENTICATION": true,

      "?KEYSTORE": "KeyStore config",
      "KEYSTORE": {
        "?TYPE": "type of KeyStore",
        "TYPE": "JKS",

        "?PATH": "path to KeyStore file",
        "PATH": "<path>",

        "?PASSWORD": "password of KeyStore",
        "PASSWORD": "<password>"
      }
    }
  }
}
```

------

### Internal

Source tree:

```
src
├─main
│  ├─java
│  │  └─uo
│  │      └─rocky
│  │          ├─entity
│  │          │  ├─Comment.java
│  │          │  ├─Coordinate.java
│  │          │  ├─EntityBase.java
│  │          │  ├─EntityRelatesToJSON.java
│  │          │  ├─EntityRelatesToSQL.java
│  │          │  ├─EntitySQLConnection.java
│  │          │  ├─QueryParamException.java
│  │          │  └─User.java
│  │          ├─httphandler
│  │          │  ├─CommentHttpHandler.java
│  │          │  ├─CoordinatesHttpHandler.java
│  │          │  ├─HttpHandlerBase.java
│  │          │  ├─RegistrationHttpHandler.java
│  │          │  ├─RequestMethod.java
│  │          │  ├─ResponseHeader.java
│  │          │  └─StatusCode.java
│  │          ├─LogWriter.java
│  │          ├─ServerLauncher.java
│  │          └─UserAuthenticator.java
│  └─resources
└─test
    ├─java
    │  └─uo
    │      └─rocky
    └─resources
```

UML of `uo.rocky` package:

<p align="center"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231123190001rocky.png" width="40%" height="40%"/></p>

UML of `uo.rocky.httphandler` package:

<p align="center"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231123190002httphandler.png" width="70%" height="70%"/></p>

UML of `uo.rocky.entity` package:

<p align="center"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231123191800entity.png" width="100%" height="100%"/></p>

#### HTTP/HTTPS

`drmo-server` can operate in both **HTTP** and **HTTPS** modes, offering flexibility in communication protocols. **HTTP** is suitable for basic communication, while **HTTPS** provides secure encrypted connections.

#### JSON

**JSON** is the data format for exchanging information between clients and the server due to its simplicity and readability.

#### SQL

**SQLite** is the RDBMS (Relational Database Management System) to store the data of users, coordinates, and comments for the server.

ERD of the relational data model:

<p align="center"><img alt="[picture]" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/202311201859.png" width="60%" height="60%"/></p>

##### Entity

###### User

DDL of `user` table:

```sql
CREATE TABLE IF NOT EXISTS user (
    USR_NAME            TEXT     NOT NULL  PRIMARY KEY  UNIQUE,
    USR_HASHEDPASSWORD  INTEGER  NOT NULL,
    USR_EMAIL           TEXT,
    USR_PHONE           TEXT
);
```

###### Coordinate

DDL of `coordinate` table:

```sql
CREATE TABLE IF NOT EXISTS coordinate (
    CDT_ID              INTEGER  NOT NULL  PRIMARY KEY  UNIQUE,
    CDT_LONGITUDE       REAL     NOT NULL,
    CDT_LATITUDE        REAL     NOT NULL,
    CDT_LOCALDATETIME   NUMERIC  NOT NULL,
    CDT_DATETIMEOFFSET  TEXT     NOT NULL,
    CDT_DANGERTYPE      TEXT     NOT NULL,
    CDT_DESCRIPTION     TEXT,
    CDT_USR_NAME        TEXT     NOT NULL,
    FOREIGN KEY (CDT_USR_NAME) REFERENCES user(USR_NAME)
);
```

###### Comment

DDL of `comment` table:

```sql
CREATE TABLE IF NOT EXISTS comment (
    CMT_ID              INTEGER  NOT NULL  PRIMARY KEY  UNIQUE,
    CMT_CONTENT         TEXT     NOT NULL,
    CMT_LOCALDATETIME   NUMERIC  NOT NULL,
    CMT_DATETIMEOFFSET  TEXT     NOT NULL,
    CMT_CDT_ID          INTEGER  NOT NULL,
    FOREIGN KEY (CMT_CDT_ID) REFERENCES coordinate(CDT_ID)
);
```

##### Relationship

###### \<user\> 1 - 0...N \<coordinate\>

- ***one*** `user` posts ***zero to many*** `coordinate`(s)
- ***one*** `coordinate` is posted by ***one*** `user`

###### \<coordinate\> 1 - 0...N \<comment\>

- ***one*** `coordinate` has ***zero to many*** `comment`(s)
- ***one*** `comment` is commented on ***one*** `coordinate`

#### LOG

Log entry types:

- The ***green-colored*** `INFO` type indicating **the normal actions**.
- The ***yellow-colored*** `WARNING` type indicating **the improper actions causing not serious consequences**.
- The ***red-colored*** `ERROR` type indicating **the exceptions that may affect the server's operations**.

------

### Exception

Exit status:

- `-2301`: `LogWriter`.`append(logEntryType, String...):void`
- `-2302`: `UserAuthenticator`.`hashPassword(String):long`
- `-2303`: `UserAuthenticator`.`checkCredentials(String, String):boolean`
- `-2304`: `HttpHandlerBase`.`respondInternalServerError(HttpExchange, String):void`

------

### Statistic

Until `2023-11-22 19:30 (UTC+08:00)`:

- **597** commits
- **1,937** lines of code
