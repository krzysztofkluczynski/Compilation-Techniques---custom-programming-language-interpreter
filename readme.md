# **PROJEKT TKOM**

**Autor**: Krzysztof Kluczyński 318672<br>
**Prowadząca**: Agnieszka Malanowska<br>
**Temat nr 8**: Język z wbudowanym typem słownika, którego zawartość można sortować.<br>

#### **<br>Opis:<br>**

Tematem projektu jest realizacja interpreta języka z wbudowanym typem słownika. 
Zawartość struktury można sortować wykonując dedykowaną do tego metodę, przyjmującą wyrażenie w stylu _lambda_ jako parametr określający sposób sortowania.
Wykonanie tej metody powoduje zmiane kolejności iterowania po elementach struktury.
Możliwe są także wszystkie podstawowe operacje na słowniku: 
* Dodawnia elementów
* Usuwanie elementów
* Modyfikowanie elementów 
* Wyszukiwanie elementów według klucza
* Sprawdzanie czy klucz znajduje się w słowniku
* Iterowanie po słowniku w celu modyfikacji wartości 

Język pozwala także na wykonywanie zapytań w stylu LINQ, 
umożliwiających wyszukiwanie, filtrowanie oraz przejrzyste zwrócenie danych w sposób określony przez użytkownika
<br>
<br>
Do implementacji wykorzystano język Java 17.
Decyzja o wykorzystaniu tego języka została podjęta głównie ze względu na fakt,
że jest to jedna z nowszych wersji języka Java o długoterminowym wsparciu (LTS).
Korzystanie z LTS wersji Java zapewnia stabilność, bezpieczeństwo oraz długoterminowe wsparcie ze strony producenta.


#### **<br>Cechy języka:**
* **Statyczne typowanie**, wszystkie typy danych muszą być jasno zadeklarowane zgodnie ze składnią języka
* **Silne typowani**e, automatyczne konwersje międzu typami nie są obsługiwane, takie operacje wymagają jasnego zdefiniowania za pomocą wbudowanych w język mechanizmów 
* **Referencje**, argumenty są przekazywane do funkcji oraz metod przez referencję(podobnie jak w języku Python)
* **Mutowalność**, zmienne są mutowalne


#### **<br>Instrukcja uruchomienia:**

Aby zbudować projekt należy wykonać komendę:

```
mvn clean install
```

Aby uruchomić interpreter koniczna jest zainstalowana Java 17. Uruchamiamy go przy użyciu wcześniej spakowanego pliku jar, za pomocą polecenia zawierającego jako argumenty ścieżki do pliku jar oraz pliku z naszym kodem:

```
java -jar <jar file path> <file path>
```

Wynik działania naszego programu powinien wyświetlić się w konsoli.


#### **<br>Najważniejsze konstrukcje językowe wraz z przykładami:**
* **Komenatrze**
  * Komentarz jednoliniowy
    ```
    //This is comment
    ``` 
    <br>
* **Typy danych:**
  * bool
  * int
  * float
  * string
  * Dictionary
  * List
  
    <br>
* **Operatory logiczne:**
  * and
  * or
  * no

    <br>
* **Operatory arytmetyczne:**
  * \*
  * /
  * \+
  * \-

    <br>
* **Pętle:**
    * while
    * for

    <br>
* **Instrukcje warunkowe:**
    * if
        ```
      int a = 2;
      
      if a == 2 {
            a = a + 2
      }
        ```
    * if-else
    * if-elseif

    <br>
* **Funkcje:**
    * definicja funkcji

    <br>
* **Klasy:**
    * definicja klasy

    <br>
* **Funkcje wbudowane:**
    * print

    
#### **<br>Wymagania funkcjonalne:**

#### **<br>Wymagania niefunkcjonalne:**

#### **<br>Sposób testowania:**

