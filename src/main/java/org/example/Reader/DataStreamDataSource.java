package org.example.Reader;

import java.io.IOException;

public class DataStreamDataSource implements DataSource{

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
