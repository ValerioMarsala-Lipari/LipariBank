# LipariBank Core Engine

Backend Java per la gestione delle principali operazioni di un sistema bancario.

Il progetto nasce come esercizio pratico per approfondire la programmazione Java, la persistenza su database relazionale e l'accesso ai dati tramite JDBC.

## Tech Stack

* Java 21
* Maven
* JDBC
* H2 Database
* Git / GitHub

## Funzionalità

Il progetto comprende attualmente:

* gestione dei clienti
* gestione dei conti correnti
* gestione delle transazioni
* gestione delle polizze
* calcolo del rischio cliente
* strategie di Risk Scoring per clienti privati e aziende
* rilevazione di anomalie tramite regole AML
* generazione di alert di compliance
* generazione di report Risk & Compliance
* calcolo parallelo dei profili di rischio
* persistenza dei dati tramite JDBC
* database H2 locale
* DAO per l'accesso ai dati
* query SQL con `PreparedStatement`
* gestione delle risorse JDBC tramite try-with-resources
* eccezioni di dominio per la gestione degli errori

## Risk & Compliance

Il progetto include un motore di Risk & Compliance per valutare il profilo di rischio dei clienti e individuare operazioni potenzialmente anomale.

### Risk Scoring

Il rischio viene rappresentato tramite uno score compreso tra `0` e `100`.

Gli score vengono associati ai seguenti livelli:

* `LOW` — 0-25
* `MEDIUM` — 26-50
* `HIGH` — 51-75
* `CRITICAL` — 76-100

Il calcolo utilizza lo Strategy Pattern, con strategie differenti per:

* clienti privati
* clienti business

Il risultato del calcolo è rappresentato da `RiskScore`.

### AML Compliance

Il motore di compliance utilizza regole configurabili per individuare attività potenzialmente sospette.

Le regole attualmente implementate verificano:

* frequenza elevata di trasferimenti superiori a €5.000 nelle ultime 24 ore
* singole transazioni superiori a €15.000
* volume giornaliero complessivo superiore a €50.000
* prima transazione superiore a €3.000 per clienti con meno di sei mesi

Gli alert possono avere i seguenti livelli:

* `INFO`
* `WARNING`
* `HIGH`
* `CRITICAL`

Il `ComplianceEngine` applica le regole configurate e produce gli alert rilevati.

### Compliance Report

Il report Risk & Compliance comprende:

* distribuzione dei clienti per livello di rischio
* alert ordinati per severità
* rischio medio per tipologia di cliente
* clienti con score superiore a 80

La generazione del report utilizza Stream API e `Collectors`.

### Parallel Risk Calculation

Il calcolo del rischio può essere eseguito in parallelo per più clienti utilizzando `ExecutorService` e Virtual Threads di Java 21.

## Persistence Layer

Il database H2 viene utilizzato come database relazionale locale.

Lo schema comprende le seguenti tabelle:

* `customers`
* `accounts`
* `transactions`
* `policies`
* `risk_scores`
* `alerts`

### DAO

Il persistence layer contiene DAO per le principali entità persistenti.

I DAO utilizzano JDBC e `PreparedStatement` per eseguire le operazioni di lettura e scrittura sul database.

Sono presenti, tra gli altri:

* `CustomerDao`
* `AccountDao`
* `TransactionDao`
* `RiskScoreDao`
* `AlertDao`

## Come eseguire il progetto

### Prerequisiti

* Java 21
* Maven

Verificare le versioni installate:

```bash
java -version
mvn -version
```

### Build

Dalla root del progetto eseguire:

```bash
mvn clean package
```

### Esecuzione

Il progetto contiene diverse classi `main` utilizzate per eseguire e verificare le funzionalità durante lo sviluppo.

Il database H2 viene creato automaticamente quando viene inizializzato il `DatabaseManager`.

La funzionalità principale può essere utilizzata anche tramite la console interattiva `BankConsole`, che include la sezione:

```text
11. Risk & Compliance
```

## Struttura del progetto

```text
src/
└── main/
    └── java/
        └── com/
            └── lipari/
                └── bank/
                    ├── cli/
                    ├── compliance/
                    │   ├── alert/
                    │   └── rules/
                    ├── exception/
                    ├── model/
                    ├── persistence/
                    ├── repository/
                    ├── reporting/
                    ├── risk/
                    ├── service/
                    └── pattern/
```

### Package principali

* `cli` — interfaccia console
* `model` — classi del dominio bancario
* `persistence` — DAO e accesso al database
* `repository` — repository in-memory
* `service` — servizi applicativi
* `pattern` — implementazioni dei principali design pattern
* `risk` — calcolo e gestione del rischio
* `compliance` — motore e regole di compliance
* `reporting` — generazione dei report
* `exception` — eccezioni applicative e di dominio

## Design Pattern

Nel progetto vengono utilizzati diversi design pattern, tra cui:

* Strategy Pattern per il Risk Scoring
* Factory Pattern per la creazione dei conti
* Builder Pattern per la configurazione delle policy
* Singleton Pattern per la configurazione della banca

## Java 21

Il progetto utilizza alcune funzionalità introdotte o consolidate nelle versioni moderne di Java, tra cui:

* Records
* Sealed Classes
* Pattern Matching
* Switch Expressions
* Virtual Threads
* Stream API e Collectors

## Database

Il database utilizzato durante lo sviluppo è H2.

La connessione viene gestita tramite `DatabaseManager`, che si occupa anche dell'inizializzazione dello schema.

Le operazioni di accesso ai dati utilizzano:

* `Connection`
* `PreparedStatement`
* `ResultSet`
* try-with-resources

## Gestione degli errori

Il progetto utilizza eccezioni di dominio per rappresentare specifici errori applicativi.

Tra queste:

* `RiskCalculationException`
* `AlertGenerationException`
* `AccountNotFoundException`
* `InsufficientFundsException`

Le eccezioni vengono utilizzate per evitare di esporre dettagli tecnici dell'implementazione ai servizi o alla console.

## Stato del progetto

Il progetto è attualmente in fase di sviluppo e viene utilizzato come percorso pratico per approfondire Java, SQL, JDBC, design pattern, concorrenza e sistemi di Risk & Compliance.
