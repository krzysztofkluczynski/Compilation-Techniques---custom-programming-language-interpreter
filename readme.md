# **PROJEKT TKOM**

**Autor**: Krzysztof Kluczyński 318672<br>
**Prowadząca**: Agnieszka Malanowska<br>
**Temat nr 8**: Język z wbudowanym typem słownika, którego zawartość można sortować.<br>

### **<br>Opis:**
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
umożliwiających wyszukiwanie, filtrowanie oraz przejrzyste zwrócenie danych w sposób określony przez użytkownika.
<br>
<br>
Do implementacji wykorzystano język Java 17.
Decyzja o wykorzystaniu tego języka została podjęta głównie ze względu na fakt,
że jest to jedna z nowszych wersji języka Java o długoterminowym wsparciu (LTS).
Korzystanie z LTS wersji Java zapewnia stabilność, bezpieczeństwo oraz długoterminowe wsparcie ze strony producenta.


### **<br>Cechy języka:**
* **Statyczne typowanie**, wszystkie typy danych muszą być jasno zadeklarowane zgodnie ze składnią języka
* **Silne typowani**e, automatyczne konwersje międzu typami nie są obsługiwane, takie operacje wymagają jasnego zdefiniowania za pomocą wbudowanych w język mechanizmów 
* **Referencje**, argumenty są przekazywane do funkcji oraz metod przez referencję(podobnie jak w języku Python)
* **Mutowalność**, zmienne są mutowalne
* **Funkcja main**, plik źródłowy musi zawierać jedną bezargumentową funkcję main zwracającą int
* **Zakres zmiennych**, zmienne są widoczne jedynie w bloku kodu między nawiasami klamrowymi {}, gdy zostały w nim zainicjowane lub przekazane jako parametr w przypadku funkcji.


### **<br>Instrukcja uruchomienia:**

Aby zbudować projekt należy wykonać komendę:

```
mvn clean install
```

Aby uruchomić interpreter koniczna jest zainstalowana Java 17. Uruchamiamy go przy użyciu wcześniej spakowanego pliku jar, za pomocą polecenia zawierającego jako argumenty ścieżki do pliku jar oraz pliku z naszym kodem:

```
java -jar <jar file path> <file path>
```

Wynik działania naszego programu powinien wyświetlić się w konsoli.


### **<br>Najważniejsze konstrukcje językowe wraz z przykładami:**
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
  * not
  
    Przykładowe operacje:

    ```
    bool var_true = true;
    bool var_false = false;
    
    bool a = var_true and (not var_false);
    bool b = var_true and (var_true or var_false);
    ```

    <br>
* **Operatory arytmetyczne:**
  * \*
  * /
  * \+
  * \-

    <br>
* **Operatory porównania:**
    * ==
    * !=
    * <
    * <=
    * \>
    * \>=

    <br>
* **Pętle:**
    * while
    * for

    <br>
* **Instrukcje warunkowe:**
    * if
        <br> Przykład konstruckji if (TEST MARKDOWN):
      ```
      int a = 2;
      
      if a == 2 {
            a = a + 2;
      }
      ```
    * if-else
      ```
      int a = 2;
      int b;
      
      if a == 2 {
            b = 2;
      } else {
            b = 0;  
      }
      ```
    * if-elseif
      ```
      int a = 2;
      int b;
      
      if a == 2 {
            b = 2;
      } elseif a == 3 {
            b = 3;  
      } else {
            b = 0;
      }
      ```

  <br>
* **Funkcje:**
    * definicja funkcji

    <br>
* **Klasy:**
    * definicja klasy

    <br>
* **Funkcje wbudowane:**
    * print

    <br>
* **Operatory rzutowania:**
    * int na float
    * float na int
    * int na string
    * float na string
    * string na int
    * string na float

    <br>
* **Zapytania na słownikach:**
    * zapytania deklaratywne
  

### **<br>EBNF:**

### **<br>Obsługa błędów:**

### **<br>Wymagania funkcjonalne:**
* Interpreter pozwala na uruchomienie kodu zapisanego w pliku tekstowym
* Język obsługuje podstawowe typy danych(int, float, bool) oraz konstrukcje językowe (pętle, instrukcje warunkowe)
* Język pozwala na wykonywanie podstawowych operacji arytmetycznych i logicznych na zmiennych
* Język posiada kolekcje - listy i słowniki
* Na słownikach możliwe jest wywołanie metody sort(), która zmieni kolejność elementów w słowniku zgodnie z wyrażeniem, które poda użytkownik jako parametr
* Na słownikach możliwe jest wykonanie zapytania w stylu LINQ(deklaratywnie), które zwróci przefiltrowane wartości we wskazanej kolejności
* język umożlwia tworzenie własnych funkcji
* język umożliwia tworzenie własnych klas
* Język jest statycznie typowany
* Język jest silnie typowany
* Zmienne są mutowalne
* Zmienne są przekazywane do funkcji przez referencję

### **<br>Wymagania niefunkcjonalne:**
* Interpreter powinien zapewniać deterministyczne działanie, co oznacza, że ten sam kod źródłowy zawsze produkuje te same wyniki przy identycznych warunkach wejściowych, zapewniając stabilność działania aplikacji.
* Interpreter powinien być łatwy do rozszerzania o dodatkowe funkcje i biblioteki, co umożliwia tworzenie bardziej zaawansowanych programów w oparciu o ten język.
* Interpreter powinien być odpowiednio udokumentowany, aby ułatwić użytkownikom korzystanie z niego, opisując składnię języka, dostępne funkcje, typy danych itp.
* Język zawiera jedynie podstawowe konstrukcje przez co jest prosty do nauki
* Mechanizm sortowania słownika powinien być zoptymalizowany pod kątem wydajności

### **<br>Zwięzły opis realizacji:**

### **<br>Sposób testowania:**

