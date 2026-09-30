# LipariBank Core Engine

Backend Java per la gestione delle principali operazioni di un sistema bancario.

Il progetto nasce come esercizio pratico per approfondire la programmazione Java, la persistenza su database relazionale e l'accesso ai dati tramite JDBC.

## Tech Stack

- Java 21
- Maven
- JDBC
- H2 Database
- Git / GitHub

## Funzionalità

Il progetto comprende attualmente:

- gestione dei clienti
- gestione dei conti correnti
- gestione delle transazioni
- gestione delle polizze
- persistenza dei dati tramite JDBC
- database H2 locale
- DAO per l'accesso ai dati
- query SQL con `PreparedStatement`
- gestione delle risorse JDBC tramite try-with-resources

## Persistence Layer

Il database H2 viene utilizzato come database relazionale locale.

Lo schema comprende le seguenti tabelle:

- `customers`
- `accounts`
- `transactions`
- `policies`

### DAO

Il persistence layer contiene:

- `CustomerDao`
- `AccountDao`
- `TransactionDao`

I DAO utilizzano JDBC e `PreparedStatement` per eseguire le operazioni di lettura e scrittura sul database.

## Come eseguire il progetto

### Prerequisiti

- Java 21
- Maven

Verificare le versioni installate:

```bash
java -version
mvn -version
```

### Build

Dalla root del progetto eseguire:

    mvn clean package

### Esecuzione

Il progetto contiene diverse classi `main` utilizzate per eseguire e verificare le funzionalità durante lo sviluppo.

Il database H2 viene creato automaticamente quando viene inizializzato il `DatabaseManager`.

## Struttura del progetto

    src/
    └── main/
        └── java/
            └── com/
                └── lipari/
                    └── bank/
                        ├── exception/
                        ├── model/
                        └── persistence/

### Package principali

- `model` — classi del dominio bancario
- `persistence` — DAO e accesso al database
- `exception` — eccezioni applicative

## Database

Il database utilizzato durante lo sviluppo è H2.

La connessione viene gestita tramite `DatabaseManager`, che si occupa anche dell'inizializzazione dello schema.

Le operazioni di accesso ai dati utilizzano:

- `Connection`
- `PreparedStatement`
- `ResultSet`
- try-with-resources

## Stato del progetto

Il progetto è attualmente in fase di sviluppo e viene utilizzato come percorso pratico per approfondire Java, SQL e JDBC.