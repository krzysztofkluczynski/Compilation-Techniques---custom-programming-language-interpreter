package org.example.Reader;

import java.io.IOException;

public interface DataSource {
        int read() throws IOException; // Zwraca kolejny znak lub -1, gdy koniec danych
        void close() throws IOException; // Zamyka źródło danych
}
