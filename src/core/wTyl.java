package core;

import gui.MainWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wTyl {
    static TreeMap<String, Character> regulyParametry = new TreeMap<>();
    static TreeMap<String, Map<String, Character>> regulyMap = new TreeMap<>();
    static ArrayList<String> lines = new ArrayList<>();

    static public void find() {
        try {
            File regulyFile = new File("data/" + MainWindow.PodajRegulyTextField.getText());
            FileReader fileReader = new FileReader(regulyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            MainWindow.ResultsOutputArea.setText("");
            String szukaneText = MainWindow.SzukaneTextField.getText();

            findLoop(szukaneText);

            if (!MainWindow.ResultsOutputArea.getText().contains("Znaczenie szukanego"))
                MainWindow.ResultsOutputArea.append("\nZnaczenia szukanego nie znaleziono");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void findLoop(String string) {
        ArrayList<Character> LineArray = new ArrayList<>();
        MainWindow.ResultsOutputArea.append("Szukamy znaczenia dla " + string + '\n');
        for (String l : lines) {
            if (l.substring(l.indexOf("=")).contains("> " + string + ";")) {
                System.out.println("lines: " + lines);
                MainWindow.ResultsOutputArea.append("\n> Znaleziono znaczenie w linii " + (lines.indexOf(l) + 1) + '\n');
                char[] chars = l.toCharArray();
                int i = 0;
                int noOr = 0;
                int noAnd = 0;
                while (chars[i] != ';') {
                    System.out.println(chars[i] + " " + i);
                    if (Character.isLetter(chars[i])) {
                        i = ifLetter(i, chars, LineArray);
                    }

                    if (chars[i] == '!') {
                        System.out.println("exclam");
                        i = ifExclam(i, chars, LineArray);
                    }

                    if (chars[i] == '(') {
                        i = ifBraces(i, chars, LineArray);
                    }

                    if (chars[i] == '=' && chars[++i] == '>') {
                        if (LineArray.size() - 1 == noOr) {
                            if (LineArray.contains('T'))
                                LineArray.add('T');
                            else
                                LineArray.add('F');
                        }

                        if (LineArray.size() - 1 == noAnd) {
                            if (LineArray.contains('F'))
                                LineArray.add('F');
                            else
                                LineArray.add('T');
                        }
                        i = ifSzukane(i, chars, LineArray);
                        System.out.println("LineArray: " + LineArray);
                        LineArray.clear();
                        break;

                    }

                    if (chars[i] == '|' && chars[i++] == '|')
                        noOr++;

                    if (chars[i] == '|' && chars[i++] == '&')
                        noAnd++;

                    i++;
                }
            }
        }
        if (!MainWindow.ResultsOutputArea.getText().contains("Znaleziono znaczenie w linii ")) {
            MainWindow.ResultsOutputArea.append("Nie ma znaczenia tej funkcji");
        }
        LineArray.clear();
        lines.clear();
    }


    static private int ifLetter(int i, char[] chars, ArrayList<Character> newLineArray) {
        String s = "";
        do {
            System.out.println("chars[i]: " + chars[i] + " " + i);
            s += chars[i];
            i++;
        } while (chars[i] != ')');
        s += ')';
        i++;
        Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
        Matcher m = p.matcher(s);
        while (m.find()) {
            if (getDane.daneMap.containsKey(m.group(1)) && (getDane.daneMap.get(m.group(1)).get(m.group(2)) != null)) {
                System.out.println("grupy:" + m.group(1) + " " + m.group(2) + " " + getDane.daneMap.get(m.group(1)).get(m.group(2)));
                regulyParametry.put(m.group(2), getDane.daneMap.get(m.group(1)).get(m.group(2)));
                regulyMap.put(m.group(1), regulyParametry);
                newLineArray.add(getDane.daneMap.get(m.group(1)).get(m.group(2)));
                MainWindow.ResultsOutputArea.append("Znaleziono znaczenie dla " + m.group(1) + '(' + m.group(2) + "): " + getDane.daneMap.get(m.group(1)).get(m.group(2)) + '\n');
            } else if (regulyMap.containsKey(m.group(1)) && (regulyMap.get(m.group(1)).get(m.group(2)) != null)) {
                newLineArray.add(regulyMap.get(m.group(1)).get(m.group(2)));
                MainWindow.ResultsOutputArea.append("Znaleziono znaczenie dla " + m.group(1) + '(' + m.group(2) + "): " + regulyMap.get(m.group(1)).get(m.group(2)) + '\n');
            } else {
                String str = m.group(1) + '(' + m.group(2) + ')';
                System.out.println("Str: " + str);
                findLoop(str);
                Map.Entry<String, Map<String, Character>> lastEntry = regulyMap.lastEntry();
                Map<String, Character> lastParametry = lastEntry.getValue();
                if (lastParametry.values().contains('T')) {
                    MainWindow.ResultsOutputArea.append("Znaleziono znaczenie dla " + str + ": T\n");
                    newLineArray.add('T');
                }
                else {
                    MainWindow.ResultsOutputArea.append("Znaleziono znaczenie dla " + str + ": F\n");
                    newLineArray.add('F');
                }
            }
        }
        return i;
    }

    static private int ifExclam(int i, char[] chars, ArrayList<Character> newLineArray) {
        i++;
        if (Character.isLetter(chars[i])) {
            i = ifLetter(i, chars, newLineArray);
            Map.Entry<String, Map<String, Character>> lastEntry = regulyMap.lastEntry();
            Map<String, Character> lastParametry = lastEntry.getValue();
            if (lastParametry.values().contains('T'))
                MainWindow.ResultsOutputArea.append("... i zostało ono zmienione na F \n");
            else
                MainWindow.ResultsOutputArea.append("... i zostało ono zmienione na T \n");
            if (newLineArray.get(newLineArray.size() - 1) == 'T') {
                newLineArray.set(newLineArray.size() - 1, 'F');
            } else {
                newLineArray.set(newLineArray.size() - 1, 'T');
            }

            System.out.println("newLineArray after ifExclam: "+ newLineArray);
        }
        if (chars[i] == '(') {
            System.out.println("IFBRACES");
            i = ifBraces(i, chars, newLineArray);

            if (newLineArray.get(newLineArray.size() - 1) == 'T') {
                newLineArray.set(newLineArray.size() - 1, 'F');
            } else {
                newLineArray.set(newLineArray.size() - 1, 'T');
            }
            MainWindow.ResultsOutputArea.append("... i zostało ono zmienione na " + newLineArray.get(newLineArray.size() - 1) + '\n');
        }
        return i;
    }

    static private int ifBraces(int i, char[] chars, ArrayList<Character> newLineArray) {
        ArrayList<Character> newBraceArray = new ArrayList<>();
        int noOr = 0;
        int noAnd = 0;
        System.out.println(newBraceArray);
        System.out.println(newLineArray);
        i++;
        while (chars[i] != ')') {
            System.out.println("i=" + i);
            if (Character.isLetter(chars[i])) {
                i = ifLetter(i, chars, newLineArray);
                System.out.println("i=" + i);
                newBraceArray.add(newLineArray.get(newLineArray.size() - 1));
                newLineArray.remove(newLineArray.size() - 1);
                System.out.println("newBraceArray: "+newBraceArray);
                System.out.println("newLineArray: "+newLineArray);
            } else if (chars[i] == '!') {
                i = ifExclam(i, chars, newLineArray);
                System.out.println("i=" + i);
                newBraceArray.add(newLineArray.get(newLineArray.size() - 1));
                newLineArray.remove(newLineArray.size() - 1);
                System.out.println("newBraceArray: "+newBraceArray);
                System.out.println("newLineArray: "+newLineArray);
            } else if (chars[i] == '|' && chars[++i] == '|')
                noOr++;
            else if (chars[i] == '&' && chars[++i] == '&')
                noAnd++;
            else i++;
        }
        System.out.println("or: " + noOr + ", and: " + noAnd);
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

        MainWindow.ResultsOutputArea.append("Znaczenie w nawiasach: " + newLineArray.get(newLineArray.size() - 1) + '\n');
        System.out.println("newBraceArray: "+newBraceArray);
        System.out.println("newLineArray: "+newLineArray);
        return i;
    }

    static private int ifSzukane(int i, char[] chars, ArrayList<Character> newLineArray) {
        String s = "";
        do {
            System.out.println("chars[i]: " + chars[i] + " " + i);
            s += chars[i];
            i++;
        } while (chars[i] != ')');
        s += ')';
        //i++;
        Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
        Matcher m = p.matcher(s);
        while (m.find()) {
            String newString = m.group(1) + '(' + m.group(2) + ')';
            if (newString.equals(MainWindow.SzukaneTextField.getText())) {
                MainWindow.ResultsOutputArea.append("\nZnaczenie szukanego " + newString + ": " + newLineArray.get(newLineArray.size() - 1) + '\n');
            } else {
                MainWindow.ResultsOutputArea.append("\nZnaczenie szukanego " + newString + ": " + newLineArray.get(newLineArray.size() - 1) + '\n');
                regulyParametry.put(m.group(2), newLineArray.get(newLineArray.size() - 1));
                regulyMap.put(m.group(1), regulyParametry);
            }
        }
        return i;
    }

}
