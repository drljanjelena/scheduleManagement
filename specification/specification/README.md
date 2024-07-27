# Specifikacija Menadžera Rasporeda

- [Pozadina](#pozadina)
- [Karakteristike](#karakteristike)
- [Instalacija](#instalacija)
- [Korišćenje](#korišćenje)
- [Pregled API-ja](#pregled-api-ja)

## Pozadina

Specifikacija Menadžera Rasporeda je dizajnirana da pruži sveobuhvatan interfejs za upravljanje i manipulaciju rasporedima, sa posebnim fokusom na upravljanje sobama i terminima. Idealna je za aplikacije koje zahtevaju detaljne sposobnosti planiranja, kao što su obrazovne institucije, centri za konferencije ili upravljanje kancelarijskim prostorom. Specifikacija opisuje metode za rukovanje svojstvima soba, planiranje termina i upravljanje rasporedima zasnovanim na datotekama.

## Karakteristike

- Apstraktna klasa `AbstractScheduleManager` koja služi kao interfejs za upravljanje rasporedima.
- Upravljanje podrazumevanim svojstvima soba i termina.
- Sveobuhvatno upravljanje sobama, uključujući dodavanje, brisanje i upite o sobama, kao i postavljanje i dobijanje specifičnih svojstava soba.
- Detaljno upravljanje terminima, uključujući dodavanje, brisanje, premještanje i upite o terminima, sa podrškom za specifičnosti datuma i vremena.
- Napredne mogućnosti filtriranja za sobe i termine na osnovu različitih kriterijuma.
- Upravljanje rasporedima zasnovano na datotekama, omogućava učitavanje i čuvanje rasporeda i detalja o sobama u i iz datoteka.

## Instalacija

Da integrišete Specifikaciju Menadžera Rasporeda u vaš projekat implementirajte metode koje su navedene u `AbstractScheduleManager`. Pogledajte [pregled API-ja](#pregled-api-ja) za detalje o svakoj metodi.
Alternativno, možete iskoristiti i jednu od dve već kreirane implementacije.

## Korišćenje

1. **Implementacija Specifikacije**: Kreirajte novu klasu u vašem projektu koja proširuje `AbstractScheduleManager`. Implementirajte sve apstraktne metode sa logikom specifičnom za potrebe vaše aplikacije.
2. **Upravljanje Sobama**: Koristite metode poput `addRoom`, `deleteRoom` i `setRoomProperty` za upravljanje sobama u vašem rasporedu.
3. **Upravljanje Terminima**: Koristite metode kao što su `addTerm`, `deleteTerm` i `moveTerm` za rukovanje terminima planiranja.
4. **Operacije sa Datotekama**: Implementirajte `loadScheduleFromFile` i `saveScheduleToFile` za upravljanje rasporedima zasnovanim na datotekama.

## Pregled API-ja

- **Inicijalizacija**
    - `AbstractScheduleManager()`
    - `initializeDefaultValues()`

- **Upravljanje Podrazumevanim Svojstvima**
    - `setRoomDefaultPropertyValue(String, Object)`
    - `getRoomDefaultPropertyValue(String)`
    - `setTermDefaultPropertyValue(String, Object)`
    - `getTermDefaultPropertyValue(String)`

- **Upravljanje Sobama**
    - `addRoom(String)`
    - `addRooms(List<String>)`
    - `getRoom(String)`
    - `deleteRoom(String)`
    - `setRoomProperty(String, String, Object)`
    - `getRoomProperty(String, String)`
    - `hasRoomProperty(String, String)`
    - `filterRoomsByProperties(Map<String, Object>)`

- **Upravljanje Terminima**
    - `addTerm(String, Date, Date)`
    - `addTerm1(String, Date, Date, String, String)`
    - `addTerms(String, List<Map<String, Object>>)`
    - `deleteTerm(String, Date, Date)`
    - `deleteTerm1(String, Date, Date, String, String)`
    - `moveTerm(String, Date, Date, Date, Date)`
    - `moveTerm1(String, Date, Date, Date, Date, String, String, String, String)`
    - `setTermProperty(String, Date, Date, String, Object)`
    - `isRoomOccupiedDuringTerm(String, Date, Date)`
    - `isRoomOccupiedDuringTerm2(String, Date, Date, String, String)`
    - `filterTermsByProperties(Map<String, Object>)`
    - `filterTermsByComparison(String, Comparable<?>, int)`
    - `getFreeTerms(String, Date, Date)`
    - `getFreeTerms2(Map<String, Object>, Date, Date)`

- **Upravljanje Datotekama**
    - `loadScheduleFromFile(String, Date, Date, List<Date>)`
    - `saveScheduleToFile(String)`
    - `saveSpecificScheduleToFile(String, Map<String, Object>)`
    - `loadRoomsFromFile(String)`
    - `saveRoomsToFile(String)`