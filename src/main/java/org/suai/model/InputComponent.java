package org.suai.model;

import java.io.Serializable;

public class InputComponent implements Serializable {

    public int numberOfPlayer;

    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean downPressed = false;
    public boolean upPressed = false;
    public boolean space = false;

    public InputComponent(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }
}
