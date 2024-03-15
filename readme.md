# **PROJEKT TKOM**

**Autor**: Krzysztof Kluczyński 318672<br>
**Prowadząca**: Agnieszka Malanowska<br>
**Temat nr 8**: Język z wbudowanym typem słownika, którego zawartość można sortować.<br>

### **<br>Opis:**
Tematem projektu jest realizacja interpreTERA języka z wbudowanym typem słownika. 
Zawartość struktury można sortować, wykonując dedykowaną do tego metodę, przyjmującą wyrażenie w stylu _lambda_ jako parametr określający sposób sortowania.
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
* **Typy danych:** //TODO dopisac operacje dla kazdego z typow
    * `bool`: Reprezentuje wartości logiczne, które mogą być `true` lub `false`.
    * `int`: Reprezentuje liczby całkowite z zakresu od -2147483648 do 2147483648.
    * `float`: Reprezentuje liczby zmiennoprzecinkowe pojedynczej precyzji z zakresu od 1.40129846432481707e-45 do 3.40282346638528860e+38.
    * `String`: Reprezentuje sekwencję znaków, która może zawierać litery, cyfry, białe znaki oraz znaki specjalne, o maksymalnej długości 200 znaków.
      ```
      bool var_bool = true;
      int var_int = 2;
      float var_float = 3.2;
      String var_string = "Hello";
      ```

    <br>
* **Kolekcje:** //TODO DOPISAC OPERACJE NA KAZDYM Z TYCH
    * `List`: Kolekcja elementów uporządkowanych, która pozwala na przechowywanie wielu elementów o różnych typach.
      ```
      List<int> var_list = [1, 2, 3, 4, 5];
      ```
    * `Tuple`: Krotka jest kolekcją elementów o różnych typach, które są traktowane jako pojedyncza jednostka.
      ```
      Tuple<String, int> var_tuple = #"dog", 3#;
      ```
    * `Dictionary`: Kolekcja par klucz-wartość, gdzie każdy klucz musi być unikalny, a wartości mogą być dowolnego typu.
      ```
      Dictionary<String, int> var_dict = |
          "dog": 3,
          "cat": 4,
          "cow": 5,
          "hamster": 6 
      |;
      ```

    <br>
* **Operatory logiczne:**
  * `and` - operator koniunkcji
  * `or` - operator alternatywy
  * `not` - operaor negacji
  
    Przykładowe operacje: 

    ```
    bool var_true = true;
    bool var_false = false;
    
    //operatory można łączyć w bardziej zaawanasowane wyrażenia używając nawiasów
    bool a = var_true and (not var_false); // a = true
    bool b = var_true and (var_true or var_false); // b = false
    ```

    <br>
* **Operatory arytmetyczne:**
  * `*`      - operator mnożenia
  * `/`       - operator dzielenia
  * `+`      - operator dodawania
  * `-`    - operator odejmowania
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
    * `==`: Oznacza równość, sprawdza, czy dwa elementy są równe.
    * `!=`: Oznacza nierówność, sprawdza, czy dwa elementy nie są równe.
    * `<`: Oznacza mniej niż, sprawdza, czy pierwszy element jest mniejszy niż drugi.
    * `<=`: Oznacza mniejsze bądź równe, sprawdza, czy pierwszy element jest mniejszy lub równy drugiemu.
    * `>`: Oznacza większe niż, sprawdza, czy pierwszy element jest większy niż drugi.
    * `>=`: Oznacza większe bądź równe, sprawdza, czy pierwszy element jest większy lub równy drugiemu.
  
      ```
      int a = 2;
      bool var = a < 1;
      bool var1 = a != 2;
      bool var2 = a >= 2;
      
      //przyklady rowniez w sekcji "petle warunkowe"
      ```

    <br>
* **Instrukcje warunkowe:** //TODO PRZEMYSLEC PETLE FOR
    * `if`: Instrukcja warunkowa, która wykonuje określony blok kodu, jeśli warunek jest spełniony.
      ```
      int a = 3;
        
      if a != 2 {
          a = a + 2;
      }
      ```
    * `if-else`: Instrukcja warunkowa, która wykonuje określony blok kodu, jeśli warunek jest spełniony, a inny blok kodu, jeśli warunek nie jest spełniony.
      ```
      int a = 2;
      int b;
      
      if a >= 2 {
            b = 2;
      } else {
            b = 0;  
      }
      ```
    * `if-elseif`: Instrukcja warunkowa, która wykonuje różne bloki kodu w zależności od spełnienia warunków.
      ```
      int a = 2;
      int b;
      
      if a == 2 {
            b = 2;
      } elseif a < 2 {
            b = -3;  
      } elseif a > 2 {
            b = 3;
      ```

    <br>
* **Pętle warunkowe:**
    * `while`: Pętla, która wykonuje określony blok kodu dopóki podany warunek jest spełniony.
      ```
      int x = 2;
    
      while x != 5 {
          x = x + 1
      }
      ```

  <br>
* **Funkcje:**
<br> W języku, każda funkcja zaczyna się od słowa kluczowego `fn` (skrót od function), po którym następuje deklaracja typu zwracanego funkcji - jeśli funkcja nic nie zwraca, typ ten jest `void`. Każdy argument funkcji musi być również opisany przez swój typ. Ciało funkcji znajduje się w nawiasach klamrowych.

    <br> Zmienne przekazywane do funkcji są przekazywane przez referencję, co oznacza, że funkcja może modyfikować ich wartość.

    <br>Aby program działał poprawnie, musi zawierać dokładnie jedną specjalnie zdefiniowaną funkcję, zwyczajowo nazywaną `main`. Jest to funkcja, od której zaczyna się wykonywanie programu. W języku `main` zazwyczaj zwraca wartość całkowitą (typ `int`) jako kod wyjścia programu.<br><br>

     * funkcja niezwracająca wartości

       ```
       //funkcja wypisująca na ekran czy podana liczba jest parzysta oraz jej wywołanie w main
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
      //funkcja zwracająca maksymalną możliwą liczbę typu int oraz jej wywowłanie w funkcji main
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
    <br> W języku możliwe jest także tworzenie klas poprzez użycie słowa kluczowego `class` <br>
    Wewnątrz klasy możemy tworzyć pola oraz metody, nie występują modyfikatory dostępu, zakładamy, że wszystkie pola oraz metody są publiczne.
    Możemy odwoływać się do pól danej klasy wewnątrz tej klasy poprzez użycie słowa kluczowego `this`.<br><br>

     * definicja klasy

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
                print("current value: " + ($String this.number);
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
    <br> Z racji tego, że język jest statycznie typowany zostały utowrzone mechanizamy rzutowania. Aby rzutować zmienną na inny typ musimy użyć `$` oraz nazwy typu na jaki rzutujemy.<br>
    W przypadku rzutowania `float` na `int` cyfry po przecinku są ucinane. 

    * int na float: rzutowanie bezstratne
      ```
      fn int main() {
          int x = 2;
          float y = ($float x); // y = 2.0
          return 0;
      }
      ```
    * float na int: obcięcie cyfr po przecinku
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
* **Zapytania na słownikach:** //TODO PRZEMYSLEC TE ZAPYTANIA I OPISAC
    * zapytania deklaratywne
      ```
      Dictionary<String, int> var_dict = |
          "dog": 3,
          "cat": 4,
          "cow": 1,
          "hamster": 6
      |;
      
      List<Tuple<String, int>> query_result = 
                                SELECT 
                            var_dict.key
                            var_dict.value
                                FROM
                            var dict
                                WHERE
                            var_dict.value > 2
                                ORDER BY
                            var_dict.value
                                ASC

      List<String> query_result2 = 
                                SELECT 
                            var_dict.key
                                FROM
                            var dict
                                WHERE
                            var_dict.value > 2
                                ORDER BY
                            var_dict.value
                                DESC
      ```
  

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

