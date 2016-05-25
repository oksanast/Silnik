package gui;

import core.getDane;
import core.wPrzod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.*;

public class MainWindow extends JFrame {
    static public JTextField PodajDaneTextField;
    static public JTextField PodajRegulyTextField;
    static public JTextArea EdycjaDaneTextArea;
    static public JTextArea EdycjaRegulyTextArea;
    static public JTextField SzukaneTextField;

    static public String getDaneArea () {return EdycjaDaneTextArea.getText();}
    static public String getRegulyArea() { return EdycjaDaneTextArea.getText(); }

    public MainWindow() {
        setSize(500, 700);
        setResizable(true);
        setTitle("Silnik wnioskujący");
        setMinimumSize(new Dimension(250, 300));

        JTabbedPane mainTable = new JTabbedPane();
        JPanel mainBorderLayout = new JPanel(new BorderLayout());
        JPanel upBorderLayout = new JPanel(new BorderLayout());
        JPanel downGridLayout = new JPanel(new GridLayout(4,1));
        JPanel mainGridLayout = new JPanel(new GridLayout(3, 1));
        JPanel upGridLayout = new JPanel(new GridLayout(4, 2));
        JPanel resultsGridLayout = new JPanel(new GridLayout(0,1));

        JLabel PodajDaneLabel = new JLabel("Podaj dane:");
        PodajDaneTextField = new JTextField("dane.txt");
        EdycjaDaneTextArea = new JTextArea();
        JButton OkDaneButton = new JButton("Ok");
        JButton SaveEdycjaDaneButton = new JButton("Edytuj i zapisz");

        JLabel PodajRegulyLabel = new JLabel("Podaj reguły:");
        PodajRegulyTextField = new JTextField("reguly.txt");
        EdycjaRegulyTextArea = new JTextArea();
        EdycjaRegulyTextArea.setMinimumSize(new Dimension(100,70));
        JButton OkRegulyButton = new JButton("Ok");
        JButton SaveEdycjaRegulyButton = new JButton("Edytuj i zapisz");

        JPanel OkEditDane = new JPanel();
        OkEditDane.add(OkDaneButton);
        OkEditDane.add(SaveEdycjaDaneButton);

        JPanel OkEditReguly = new JPanel();
        OkEditReguly.add(OkRegulyButton);
        OkEditReguly.add(SaveEdycjaRegulyButton);

        upGridLayout.add(PodajDaneLabel);
        upGridLayout.add(PodajRegulyLabel);
        upGridLayout.add(PodajDaneTextField);
        upGridLayout.add(PodajRegulyTextField);
        upGridLayout.add(OkEditDane);
        upGridLayout.add(OkEditReguly);
        upGridLayout.add(EdycjaDaneTextArea);
        upGridLayout.add(new JScrollPane(EdycjaDaneTextArea));
        upGridLayout.add(EdycjaRegulyTextArea);
        upGridLayout.add(new JScrollPane(EdycjaRegulyTextArea));

        SzukaneTextField = new JTextField("D(d)");
        JTextArea ResultsOutputArea = new JTextArea();

        JPanel WTrzodTylPanel = new JPanel();
        JButton WPrzodButton = new JButton ("W przód");
        WPrzodButton.setPreferredSize(new Dimension(220,40));
        WPrzodButton.setBackground(Color.cyan);
        JButton WTylButton = new JButton ("W tył");
        WTylButton.setPreferredSize(new Dimension(220,40));
        WTylButton.setBackground(Color.getHSBColor(100,100,200));
        WTrzodTylPanel.add(WPrzodButton);
        WTrzodTylPanel.add(WTylButton);

        downGridLayout.add(new JLabel("Podaj szukane:", SwingConstants.CENTER), BorderLayout.NORTH);
        downGridLayout.add(SzukaneTextField);
        downGridLayout.add(WTrzodTylPanel);
        downGridLayout.add(new JLabel("Rezultaty", SwingConstants.CENTER));

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(1,1));

        resultsPanel.add(ResultsOutputArea);
        resultsPanel.add(new JScrollPane(ResultsOutputArea));
        resultsGridLayout.add(resultsPanel);

        upBorderLayout.add(new JLabel("Pliki", SwingConstants.CENTER), BorderLayout.NORTH);
        upBorderLayout.add(upGridLayout, BorderLayout.CENTER);
        mainGridLayout.add(upBorderLayout);
        mainGridLayout.add(downGridLayout);
        mainGridLayout.add(resultsGridLayout);
        mainBorderLayout.add(mainGridLayout);
        add(mainBorderLayout);

        mainTable.addTab("Wyszukiwanie", mainBorderLayout);
        add(mainTable);


        OkDaneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = PodajDaneTextField.getText();
                String line = null;
                try {
                    FileReader fileReader = new FileReader("data/" + fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    while((line = bufferedReader.readLine()) != null) {
                        EdycjaDaneTextArea.append(line + "\n");
                    }

                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + fileName + "'");
                }
                catch(IOException ex) {
                    System.out.println("Error reading file '" + fileName + "'");
                }
            }
        });
        OkRegulyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = PodajRegulyTextField.getText();
                String line = null;
                try {
                    FileReader fileReader = new FileReader("data/" + fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while((line = bufferedReader.readLine()) != null) {
                        EdycjaRegulyTextArea.append(line + "\n");
                    }

                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + fileName + "'");
                }
                catch(IOException ex) {
                    System.out.println("Error reading file '" + fileName + "'");
                }
            }
        });

        SaveEdycjaDaneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintWriter writer;
                String fileName = "data/" + PodajDaneTextField.getText();
                try
                {
                    writer = new PrintWriter(fileName);
                    writer.write(EdycjaDaneTextArea.getText());
                    writer.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        SaveEdycjaRegulyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintWriter writer;
                String fileName = "data/" + PodajRegulyTextField.getText();
                try
                {
                    writer = new PrintWriter(fileName);
                    writer.write(EdycjaRegulyTextArea.getText());
                    writer.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });



        //JMENUBAR//
        JMenuBar mainMenu = new JMenuBar();
        setJMenuBar(mainMenu);
        JMenu fileMenu = new JMenu("Plik");
        JMenu editMenu = new JMenu("Edycja");
        JMenu helpMenu = new JMenu("Pomoc");
        mainMenu.add(fileMenu);
        mainMenu.add(editMenu);
        mainMenu.add(helpMenu);

        AboutWindow aboutDialog = new AboutWindow();

        Action exitAction = new AbstractAction("Zakończ") {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        };

        Action clearFilesAction = new AbstractAction("Wyczyść dane") {
            public void actionPerformed(ActionEvent event) {
                PodajDaneTextField.setText("");
                EdycjaDaneTextArea.setText("");
                PodajRegulyTextField.setText("");
                EdycjaRegulyTextArea.setText("");
                SzukaneTextField.setText("");
            }
        };

        Action clearResultsAction = new AbstractAction("Wyczyść rezultaty") {
            public void actionPerformed(ActionEvent event) {
                ResultsOutputArea.setText("");
            }
        };

        Action aboutAction = new AbstractAction("O programie") {
            public void actionPerformed(ActionEvent event) {
                aboutDialog.setVisible(true);
            }
        };

        fileMenu.add(exitAction);
        editMenu.add(clearFilesAction);
        editMenu.add(clearResultsAction);
        helpMenu.add(aboutAction);



        //REZULTATY//
        WPrzodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDane.putSzukane();
                getDane.putDane();
                wPrzod.find();
            }
        });
    }
}
