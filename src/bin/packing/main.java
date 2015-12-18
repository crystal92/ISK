/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.packing;

import java.awt.EventQueue;

/**
 *
 * @author Crystal
 */
public class main {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new okno();
            }
        });
    }
}
