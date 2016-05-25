package core;

import gui.MainWindow;
import javax.swing.*;

public class Silnik implements Runnable {
    public static void main(String[] args) {
        new Silnik();
    }
    public static MainWindow frame;

    public Silnik() {
        frame = new MainWindow();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(this).start();
    }

    public void run(){}
}
