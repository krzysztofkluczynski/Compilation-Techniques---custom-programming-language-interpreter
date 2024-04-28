package org.example.token;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class Position {


    @Getter
    @Setter
    private int line;

    @Getter
    @Setter
    private int characterNumber;

    public Position() {
        this.line = 1;
        this.characterNumber = 0;
    }


    public String toPositionString() {
        return "line: " + line + ", character: " + characterNumber;
    }
}