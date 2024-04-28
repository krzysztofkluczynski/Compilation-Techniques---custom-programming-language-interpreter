package org.example.token;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class Position implements Comparable<Position> {


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

    public void nextCharacter() {
        characterNumber++;
    }

    public void nextLine() {
        characterNumber = 0;
        line++;
    }

    public Position copy() {
        return this.toBuilder().build();
    }


    @Override
    public int compareTo(Position position) {
        if (this.line > position.line || this.characterNumber > position.characterNumber) {
            return 1;
        } else if (this.line == position.line && this.characterNumber == position.characterNumber) {
            return 0;
        } else {
            return -1;
        }
    }

    public String toPositionString() {
        return "line: " + line + ", character: " + characterNumber;
    }
}