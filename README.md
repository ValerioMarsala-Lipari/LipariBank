# LipariBank Day 01 — Backend Bootstrap

Benvenuto nel progetto **liparibank-day01-broken**.

Questa è un'applicazione Spring Boot 3.3.4 (Java 21) che simula il bootstrap iniziale
del backend bancario LipariBank. Il progetto compila correttamente, ma contiene
**3 bug intenzionali** che impediscono al sistema di avviarsi o funzionare come previsto.

Il tuo obiettivo è trovarli tutti e tre.

---

## Come avviare il progetto

```bash
./mvnw spring-boot:run
```

Su Windows:

```cmd
mvnw.cmd spring-boot:run
```

---

## Le 3 Missioni

### Missione 1 — Il valore non arriva

L'applicazione non riesce ad avviarsi. Il sistema di binding delle proprietà
fallisce con un errore durante il caricamento della configurazione. Il valore
configurato in `application.yml` per l'importo massimo di trasferimento non è
compatibile con il tipo del campo corrispondente nel record Java.

**Sintomo:** errore di startup legato al binding di `maxTransferAmount`.
L'applicazione non parte.

---

### Missione 2 — Il contesto non si forma

Spring non riesce a costruire il contesto applicativo. Al momento dell'iniezione
delle dipendenze, il container si trova di fronte a una situazione ambigua che
non sa risolvere autonomamente.

**Sintomo:** `NoUniqueBeanDefinitionException` o `NoSuchBeanDefinitionException`
nella fase di avvio. Il contesto Spring fallisce prima che qualsiasi endpoint
sia raggiungibile.

---

### Missione 3 — Il silenzio del lifecycle

L'applicazione sembra avviarsi senza errori, ma un componente che dovrebbe
produrre output nei log durante la fase di inizializzazione non si manifesta mai.
Nessun messaggio di startup bancario appare nella console.

**Sintomo:** assenza totale dei log di inizializzazione attesi, pur non essendo
presente alcun errore esplicito.

---

## Hint generale

> Usa `./mvnw spring-boot:run` e osserva attentamente i log di startup.
> Confronta il comportamento atteso con quello reale.
> Leggi il codice come se fosse corretto — poi chiediti: *lo è davvero?*

---

## Struttura del progetto

```
src/main/java/com/lipari/bank/
├── LipariBankApplication.java
├── shared/
│   └── config/
│       └── LipariBankProperties.java
└── account/
    ├── NotificationService.java
    ├── LogNotificationService.java
    ├── EmailNotificationService.java
    └── AlertService.java
```

---

## Bonus Mission — Feature da Implementare (opzionale, ~1 ora)

Una volta risolti i 3 bug, implementa la seguente feature per consolidare i concetti del giorno.

### Endpoint di configurazione con Prototype scope

Il progetto carica la configurazione bancaria tramite `LipariBankProperties`, ma non esiste nessun modo per consultarla a runtime via HTTP.

**Cosa implementare:**

Aggiungi un `@RestController` chiamato `ConfigController` che espone un endpoint `GET /api/v1/config`. La risposta deve restituire in formato JSON tutte le proprietà caricate da `LipariBankProperties`.

In più, crea un nuovo bean con scope `prototype` chiamato `ConfigAuditEntry`. Questo bean deve, nel momento in cui viene istanziato, acquisire automaticamente il timestamp corrente e un identificatore univoco. Il `ConfigController`, a ogni invocazione dell'endpoint, deve ottenere una **nuova istanza** di questo bean e includerne i dati nella risposta JSON.

**Criteri di accettazione:**

- `GET /api/v1/config` risponde `200 OK` con un body JSON che contiene i valori di configurazione dell'applicazione.
- Ogni risposta include anche un oggetto `auditEntry` con un campo `id` (stringa univoca) e un campo `timestamp`.
- Effettuando due chiamate consecutive, il campo `auditEntry.id` è **diverso** in ognuna — a dimostrazione che Spring crea un'istanza fresca del bean a ogni invocazione.
- L'applicazione si avvia senza errori dopo l'aggiunta della feature.

---

*LipariBank Prompt Bootcamp — Spring Boot Internals & DI — Day 01*
