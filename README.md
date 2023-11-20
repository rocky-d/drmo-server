# **drmo-server** (GitHub: *[rocky-d/drmo-server](https://github.com/rocky-d/drmo-server)*)

## Coursework for `Programming 3`

#### Developed by `Rocky Haotian Du` (GitHub: *[rocky-d](https://github.com/rocky-d)*)

#### Last modified date: `2023-11-20`

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
  - [How to modify its configuration?](#how-to-modify-its-configuration)
- [Internal](#internal)
  - [HTTP/HTTPS](#httphttps)
  - [JSON](#json)
  - [SQL](#sql)
    - [Entity](#entity)
      - [User](#user)
      - [Coordinate](#coordinate)
      - [Comment](#comment)
    - [Relationship](#relationship)
      - [\<user\> 1 - 0...N \<coordinate\>](#user-1---0n-coordinate)
      - [\<coordinate\> 1 - 0...N \<comment\>](#coordinate-1---0n-comment)
  - [LOG](#log)
- [Exception](#exception)

------

### Introduction

#### What is it?

...

#### Why is it called "drmo-server"?

The word "drmo" in the project name is actually the abbreviation of the four supported danger types - "DEER", "REINDEER", "MOOSE", and "OTHER". And it's also an HTTP/HTTPS server. That's why it is called "drmo-server". 🎄🎄🎄🦌🦌🦌🌐🌐🌐

You can speak "Dre-Mo-Server" or simply "D-R-M-O-Server" to call it.

#### What are its functionalities?

...

#### What are its features?

...

------

### Environment

This is only the information of my own development environment:

- Windows 10 Pro
- Java 1.8.0_202
- Maven 3.9.2

------

### Usage

#### How to run it?

- Build the project with **Maven**.
- Run the main method in the `ServerLauncher` class to start the server.
- Follow the printed instructions if it's **the first run**.
  - Don't panic if you see an error message. Calm down and read the prompts carefully.
  - Choose whether to load the default configuration. (Type `yes` or `y` and then press `Enter`.)
  - Check and modify (if you need to) the config file.
  - Rerun the program.

Normal:

<img alt="screenshot" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/20231119221737.png" width="100%" height="100%"/>

The first run:

<img alt="screenshot" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/newnew20231119221641.png" width="100%" height="100%"/>

#### How to send requests to it?

...

#### How to modify its configuration?

...

------

### Internal

#### HTTP/HTTPS

...

#### JSON

...

#### SQL

**SQLite** is the RDBMS (Relational Database Management System) to store the data of users, coordinates, and comments for the server.

ERD (Entity-Relationship Diagram):

<img alt="diagram" src="https://cdn.jsdelivr.net/gh/rocky-d/picgo-img@master/img/202311201859.png" width="65%" height="65%"/>

##### Entity

###### User

DDL of the **user** table:

```sqlite
CREATE TABLE IF NOT EXISTS user (
    USR_NAME            TEXT     NOT NULL  PRIMARY KEY  UNIQUE,
    USR_HASHEDPASSWORD  INTEGER  NOT NULL,
    USR_EMAIL           TEXT,
    USR_PHONE           TEXT
);
```

###### Coordinate

DDL of the **coordinate** table:

```sqlite
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

DDL of the **comment** table:

```sqlite
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

- **one** user ***posts*** **zero to many** coordinate(s)
- **one** coordinate ***is posted by*** **one** user

###### \<coordinate\> 1 - 0...N \<comment\>

- **one** coordinate ***has*** **zero to many** comment(s)
- **one** comment ***is commented on*** **one** coordinate

#### LOG

- The *green-colored* **INFO** type indicating the normal actions.
- The *yellow-colored* **WARNING** type indicating the improper actions causing not serious consequences.
- The *red-colored* **ERROR** type indicating the exceptions that may affect the server's operations.

------

### Exception

Exit status:

- `-2301`: `LogWriter`.`append(logEntryType, String...):void`
- `-2302`: `UserAuthenticator`.`hashPassword(String):long`
- `-2303`: `UserAuthenticator`.`checkCredentials(String, String):boolean`
- `-2304`: `HttpHandlerBase`.`respondInternalServerError(HttpExchange, String):void`
