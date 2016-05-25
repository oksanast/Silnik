package core;

import gui.MainWindow;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getDane {
    static HashMap<String, Character> szukaneParametry = new HashMap<>();
    static HashMap<String, Map<String, Character>> szukaneMap = new HashMap<>();
    static public void putSzukane() {
        Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
        Matcher m = p.matcher(MainWindow.SzukaneTextField.getText());
        while (m.find()) {
            //System.out.println(m.group(1) + " " + m.group(2));
            szukaneParametry.put(m.group(2), null);
            szukaneMap.put(m.group(1), szukaneParametry);
        }
    }

    static HashMap<String, Character> daneParametry = new HashMap<>();
    static HashMap<String, Map<String, Character>> daneMap = new HashMap<>();
    static public void putDane() {
        String daneText = MainWindow.EdycjaDaneTextArea.getText();
        String[] splits = daneText.split("; ");
        for (String split: splits) {

            Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\) = ([a-zA-Z])");
            Matcher m = p.matcher(split);

            while (m.find()) {
                //System.out.println(m.group(1) + " " + m.group(2) +  " " + m.group(3));
                daneParametry.put(m.group(2), m.group(3).charAt(0));
                daneMap.put(m.group(1), daneParametry);
            }
        }
    }
}
