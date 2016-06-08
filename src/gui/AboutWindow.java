package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by WTC-Team on 21.04.2016.
 * Project WhatToCook
 */
/*
    WYŚWIETLA INFORMACJE O AUTORACH PROGRAMU I WERSJI
 */
public class AboutWindow extends JDialog{
    public AboutWindow()
    {
        setSize(250,250);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("O programie");
        JButton exit = new JButton("Zamknij");
        exit.addActionListener(e -> setVisible(false));
        JLabel name = new JLabel("<html><h1><center>Silnik wnioskujący<center></h1></html>");
        JLabel description = new JLabel("<html><br>Oksana Stechkevych<br><br>"+"</html>");
        JPanel mainBorderLayout = new JPanel(new BorderLayout());
        mainBorderLayout.add(exit,BorderLayout.SOUTH);
        mainBorderLayout.add(description,BorderLayout.CENTER);
        mainBorderLayout.add(name,BorderLayout.NORTH);
        add(mainBorderLayout);
    }
}