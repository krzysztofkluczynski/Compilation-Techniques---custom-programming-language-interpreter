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
* Iterowanie po słowniku

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
* **Silne typowanie**, automatyczne konwersje międzu typami nie są obsługiwane, takie operacje wymagają jasnego zdefiniowania za pomocą wbudowanych w język mechanizmów 
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
  * String
  * Dictionary
  * List
  * Tuple
  

  
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
    ```
    int first = 4;
    int second = 3;
    int third = 2;
    
    // dzielenie dwóch zmiennych typu integer również zwraca integer, czyli po wykonaniu poniższej linii zmienna result1 = 1
    int result1 = second / third;  
    
    //zachowana jest kolejność działań zgodna z matematyką, czyli po wykonaniu poniższej linii zmienna result2 = 14
    int result2 = 2 + first * second;
    ```

    <br>
* **Operatory porównania:**
    * ==
    * !=
    * <
    * <=
    * \>
    * \>=
  
      ```
      int a = 2;
      bool var = a < 1;
      bool var1 = a != 2;
      bool var2 = a >= 2;
      
      //przyklady rowniez w sekcji "petle warunkowe"
      ```

    <br>
* **Instrukcje warunkowe:**
    * if
      ```
      int a = 3;
      
      if a != 2 {
          a = a + 2;
      }
      ```
    * if-else
      ```
      int a = 2;
      int b;
      
      if a >= 2 {
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
      } elseif a < 2 {
            b = -3;  
      } elseif > 2 {
            b = 3;
      ```

    <br>
* **Pętle warunkowe:**
    * while
      ```
      int x = 2;
  
      while x != 5 {
          x = x + 1
      }
      ```

  <br>
* **Funkcje:**
    * funkcja niezwracająca wartości
      ```
      fn void printIfEven(int number) {
          if (number / 2) * 2 != number {
              print("The number is odd.");
          } else {
              print("The number is even.");
          }
      }
      
      
      fn int main() {
          int x = 26;
          printIfEven(x);
    
          return 0;
      }
      ```

  * funkcja bezargumentowa
    ```
    fn int getMaxInt() {
        return 2147483647;
    }
    
    
    fn int main() {
        int x = getmaxInt();
        print(($String x))
        return 0;
    }
    ```

    <br>
* **Klasy:**
    * definicja klasy
      Zakładamy, że wszystkie pola oraz metody są publiczne, język nie posiada modyfikatorów dostępu
      ```
      class Counter {
        
        Counter(int number) {
        int number = 0;
        }
      
        fn int getNumber() {
            return this.number;
        }
      
        fn void setNumber(int number) {
            this.number = number;
        }
      
        fn void increment() {
             this.number = this.number + 1; 
        }
      
        fn void decrement() {
            this.number = this.number - 1;
        }
      
        
        fn void printNumber() {
            print("current value: " + (@String this.number);
        }
      
      }
      
      
      fn int main() {
          int x = 1;
          Counter counter = Counter(x);
          counter.increment;
          counter.printNumber();
      }
      ```

  <br>
* **Funkcje wbudowane:**
    * print
      <br>Funkcja print powoduje wypisanie tekstu w konsoli, przyjmuje jedynie argumenty typu String 
      ```
      fn int main() {
          print("Hello");
          return 0;
      }
      ```

    <br>
* **Operatory rzutowania:**
    * int na float
      ```
      fn int main() {
          int x = 2;
          float y = ($float x); // y = 2.0
          return 0;
      }
      ```
    * float na int
      ```
      fn int main() {
          float x = 3.33;
          int y = ($int x); // y = 3
          return 0;
      }
      ```
    * int na string
      ```
      fn int main() {
          int x = 3;
          String y = ($String x);
          return 0;
      }
      ```
    * float na string
      ```
      fn int main() {
          float x = 3.2;
          String y = ($String x);
          return 0;
      }
      ```
    * string na int
      ```
      fn int main() {
          String x = "3";
          int y = ($String x);
          return 0;
      }
      ```
    * string na float
      ```
      fn int main() {
          String x = "3.2";
          float y = ($String x);
          return 0;
      }
      ```

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

