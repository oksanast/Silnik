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

public class wPrzod {
    static TreeMap<String, Character> regulyParametry = new TreeMap<>();
    static TreeMap<String, Map<String, Character>> regulyMap = new TreeMap<>();
    //Array of values 'T' and 'F' in each line
    static ArrayList<Character> newLineArray = new ArrayList<>();

    static public void find() {
        try {
            File regulyFile = new File("data/" + MainWindow.PodajRegulyTextField.getText());
            FileInputStream regulyStream = new FileInputStream(regulyFile);
            char current;

            while (regulyStream.available() > 0) {
                current = (char) regulyStream.read();
                //System.out.print(current);

                if (Character.isLetter(current)) {
                    ifLetter(current, regulyStream);
                }

                if (current == '!') {
                    ifExclam(current, regulyStream, newLineArray);
                }

                if (current == '(') {
                    current = (char) regulyStream.read();
                    if (Character.isLetter(current)) {
                        ifLetter(current, regulyStream);
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void ifLetter (char current, FileInputStream regulyStream) {
        String string = "";
        string += current;
        try {
            while (current != ')') {
                current = (char) regulyStream.read();
                //System.out.print(current);
                string += current;
            }
            Pattern p = Pattern.compile("([a-zA-Z]+)\\(([a-zA-Z]+)\\)");
            Matcher m = p.matcher(string);
            while (m.find()) {
                //System.out.println("group:" + m.group(1) + " " + m.group(2));
                if (getDane.daneMap.containsKey(m.group(1)) && (getDane.daneMap.get(m.group(1)).get(m.group(2)) != null)) {
                    //System.out.println(m.group(1) + " " + m.group(2)  + " " + getDane.daneMap.get(m.group(1)).get(m.group(2)));
                    regulyParametry.put(m.group(2), getDane.daneMap.get(m.group(1)).get(m.group(2)));
                    regulyMap.put(m.group(1), regulyParametry);
                    newLineArray.add(getDane.daneMap.get(m.group(1)).get(m.group(2)));
                } else {
                    System.out.println("Nie na danej: " + m.group(1));
                    System.exit(1);
                }
            }
        } catch (IOException ex) {}
    }

    static private void ifExclam (char current, FileInputStream regulyStream, ArrayList<Character> newLineArray) {
        try {
            current = (char) regulyStream.read();
            if (Character.isLetter(current)) {
                ifLetter(current, regulyStream);
                //Map.Entry<String, Map<String, Character>> lastEntry = regulyMap.lastEntry();
                //Map<String, Character> lastParametry = lastEntry.getValue();
                //if ( lastParametry.values().contains('T') )
                if (newLineArray.get(newLineArray.size()-1) == 'T')
                { newLineArray.set(newLineArray.size()-1, 'F'); }
                else
                { newLineArray.set(newLineArray.size()-1, 'T'); }
                //System.out.println(newLineArray);
            }

            /*if (current == '(') {
                ifBraces(current, regulyStream);

            }*/
        } catch (IOException ex) {}
    }

    static private void ifEqual (char current, FileInputStream regulyStream) {

    }

}

