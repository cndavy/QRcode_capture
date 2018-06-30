package com.han;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by han on 2018/6/30.
 */
public class ttt {
    private JButton button1;
    private JPanel panel1;

    public ttt() {
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ttt");
        frame.setContentPane(new ttt().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
