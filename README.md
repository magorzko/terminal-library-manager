# Terminalowy Menedżer Biblioteki

Wymagania: **Java 21**, **Maven 3.9+**

## Uruchomienie

```bash
mvn -q clean package
java -jar target/terminal-library-manager-1.0-SNAPSHOT.jar
```

## Dane startowe

- **admin / admin** (rola ADMIN)
- **user / user** (rola USER)

## Funkcje

- Logowanie (hasła hashowane SHA-256 + losowy salt)
- USER:
  - przeglądanie listy książek
  - wyszukiwanie po tytule / autorze
- ADMIN:
  - dodawanie / usuwanie / edycja książek

## Architektura (skrót)

- `repository` – interfejsy + implementacje in-memory
- `service` – logika aplikacyjna (autoryzacja, książki)
- `security` – hashowanie haseł (interfejs + implementacja)
- `gui` – terminalowe menu
- `configuration` – konfiguracja Spring DI
