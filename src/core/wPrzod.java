package core;

import gui.MainWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wPrzod extends MainWindow {
    static TreeMap<String, Character> regulyParametry = new TreeMap<>();
    static TreeMap<String, Map<String, Character>> regulyMap = new TreeMap<>();
    //Array of values 'T' and 'F' in each line
    static ArrayList<Character> newLineArray = new ArrayList<>();
    static boolean fail = false;
    static boolean win = false;

    static public void find() {
        try {
            File regulyFile = new File("data/" + PodajRegulyTextField.getText());
            FileInputStream regulyStream = new FileInputStream(regulyFile);
            char current;
            int noLine = 0;
            int noOr = 0;
            int noAnd = 0;
            ResultsOutputArea.setText("");

            while (regulyStream.available() > 0 && !fail && !win) {
                if (newLineArray.isEmpty()) {
                    noLine++;
                    StringBuilder sb = new StringBuilder();
                    sb.append(noLine);
                    String noLineStr = sb.toString();
                    ResultsOutputArea.append("> " + noLineStr + " linia \n");
                }
                current = (char) regulyStream.read();

                if (Character.isLetter(current)) {
                    ifLetter(current, regulyStream, newLineArray);
                }

                if (current == '!') {
                    ifExclam(current, regulyStream, newLineArray);
                }

                if (current == '(') {
                    ifBraces(current, regulyStream, newLineArray);
                }

                if (current == '=' && (current = (char) regulyStream.read()) == '>') {
                    if (newLineArray.size() - 1 == noOr) {
                        if (newLineArray.contains('T'))
                            newLineArray.add('T');
                        else
                            newLineArray.add('F');
                    }

                    if (newLineArray.size() - 1 == noAnd) {
                        if (newLineArray.contains('F'))
                            newLineArray.add('F');
                        else
                            newLineArray.add('T');
                    }
                    if (ifSzukane(current, regulyStream, newLineArray))
                        win = true;
                }

                if (current == '|' && (current = (char) regulyStream.read()) == '|')
                    noOr++;

                if (current == '&' && (current = (char) regulyStream.read()) == '&')
                    noAnd++;

                if (current == '\n') {
                    newLineArray.clear();
                    noOr = 0;
                    noAnd = 0;
                }
            }
            if (!ResultsOutputArea.getText().contains("Znaczenie szukanego"))
                ResultsOutputArea.append("\nZnaczenia szukanego nie znaleziono");
            fail = false;
            win = false;
            newLineArray.clear();
            regulyStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static private void ifLetter(char current, FileInputStream regulyStream, ArrayList<Character> newLineArray) {
        String string = "";
        string += current;
        try {
            while (current != ')') {
                current = (char) regulyStream.read();
                string += current;
            }
            Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
            Matcher m = p.matcher(string);
            while (m.find()) {
                if (getDane.daneMap.containsKey(m.group(1)) && (getDane.daneMap.get(m.group(1)).get(m.group(2)) != null)) {
                    regulyParametry.put(m.group(2), getDane.daneMap.get(m.group(1)).get(m.group(2)));
                    regulyMap.put(m.group(1), regulyParametry);
                    newLineArray.add(getDane.daneMap.get(m.group(1)).get(m.group(2)));
                    ResultsOutputArea.append("Znaleziono znaczenie dla " + m.group(1) + '(' + m.group(2) + "): " + getDane.daneMap.get(m.group(1)).get(m.group(2)) + '\n');
                } else if (regulyMap.containsKey(m.group(1)) && (regulyMap.get(m.group(1)).get(m.group(2)) != null)) {
                    newLineArray.add(regulyMap.get(m.group(1)).get(m.group(2)));
                    ResultsOutputArea.append("Znaleziono znaczenie dla " + m.group(1) + '(' + m.group(2) + "): " + regulyMap.get(m.group(1)).get(m.group(2)) + '\n');
                } else {
                    ResultsOutputArea.append("\nNie ma znaczenia dla danej: " + m.group(1) + '(' + m.group(2) + ')');
                    fail = true;
                }
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    static private void ifExclam(char current, FileInputStream regulyStream, ArrayList<Character> newLineArray) {
        try {
            current = (char) regulyStream.read();
            if (Character.isLetter(current)) {
                ifLetter(current, regulyStream, newLineArray);
                if (!fail) {
                    Map.Entry<String, Map<String, Character>> lastEntry = regulyMap.lastEntry();
                    Map<String, Character> lastParametry = lastEntry.getValue();
                    if (lastParametry.values().contains('T'))
                        ResultsOutputArea.append("... i zostało ono zmienione na F \n");
                    else
                        ResultsOutputArea.append("... i zostało ono zmienione na T \n");
                    if (newLineArray.get(newLineArray.size() - 1) == 'T') {
                        newLineArray.set(newLineArray.size() - 1, 'F');
                    } else {
                        newLineArray.set(newLineArray.size() - 1, 'T');
                    }
                }
            }
            if (current == '(') {
                ifBraces(current, regulyStream, newLineArray);
                if (newLineArray.get(newLineArray.size() - 1) == 'T') {
                    newLineArray.set(newLineArray.size() - 1, 'F');
                } else {
                    newLineArray.set(newLineArray.size() - 1, 'T');
                }
                ResultsOutputArea.append("... i zostało ono zmienione na " + newLineArray.get(newLineArray.size()-1) + '\n');
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    static private void ifBraces(char current, FileInputStream regulyStream, ArrayList<Character> newLineArray) {
        try {
            ArrayList<Character> newBraceArray = new ArrayList<>();
            int noOr = 0;
            int noAnd = 0;

            while ((current = (char) regulyStream.read()) != ')') {
                if (!fail) {
                    if (Character.isLetter(current)) {
                        ifLetter(current, regulyStream, newLineArray);
                        if (!fail) {
                            newBraceArray.add(newLineArray.get(newLineArray.size() - 1));
                            newLineArray.remove(newLineArray.size() - 1);
                        }
                    } else if (current == '!') {
                        ifExclam(current, regulyStream, newLineArray);
                        if (!fail) {
                            newBraceArray.add(newLineArray.get(newLineArray.size() - 1));
                            newLineArray.remove(newLineArray.size() - 1);
                        }
                    } else if (current == '|' && (char) regulyStream.read() == '|')
                        noOr++;
                    else if (current == '&' && (char) regulyStream.read() == '&')
                        noAnd++;
                }
            }

            if (newBraceArray.size() - 1 == noOr) {
                if (newBraceArray.contains('F'))
                    newLineArray.add('F');
                else
                    newLineArray.add('T');
            }

            if (newBraceArray.size() - 1 == noAnd) {
                if (newBraceArray.contains('F'))
                    newLineArray.add('F');
                else
                    newLineArray.add('T');
            }
            ResultsOutputArea.append("Znaczenie w nawiasach: "+newLineArray.get(newLineArray.size()-1)+'\n');
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    static private boolean ifSzukane (char current, FileInputStream regulyStream, ArrayList<Character> newLineArray) {
        try {
            String string = "";
            current = (char) regulyStream.read();
            while (current != ';') {
                current = (char) regulyStream.read();
                string += current;
            }
            Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
            Matcher m = p.matcher(string);
            while (m.find()) {
                String newString = m.group(1) + '(' + m.group(2) + ')';
                if (newString.equals(SzukaneTextField.getText())) {
                    ResultsOutputArea.append("\nZnaczenie szukanego " + newString + ": " + newLineArray.get(newLineArray.size() - 1));
                    return true;
                } else {
                    ResultsOutputArea.append("Znaleziono znaczenie dla " + m.group(1) + '(' + m.group(2) + "): " + newLineArray.get(newLineArray.size() - 1) + '\n');
                    regulyParametry.put(m.group(2), newLineArray.get(newLineArray.size() - 1));
                    regulyMap.put(m.group(1), regulyParametry);
                }
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
        return false;
    }
}