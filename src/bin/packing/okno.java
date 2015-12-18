package bin.packing;

import static bin.packing.BinPacking.czytaj_plik;
import static bin.packing.BinPacking.generuj_index_element;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.softtechdesign.ga.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Keymap;

public class okno extends JFrame {

    boolean isStandalone = false;
    //String[] galist = {"Binary All Ones", "Curve Fit", "Traveling Salesman", "Trig Func", "Maze", ""};
    //JList jListGA = new JList(galist);
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();

    JLabel jLabel_ilosc_elementow = new JLabel();
    JLabel jLabel_rozmiar_pudelka = new JLabel();

    JButton jButtonchoosefile = new JButton();
    JButton jButtonRun = new JButton();
    Image imgDev = null;
    Image imgFit = null;
    JLabel jLabelBestChromosome = new JLabel();

    JPanel panel_WybierzPlik = new JPanel();
    JPanel panel_WyswietlPlik = new JPanel();
    JPanel panel_Wykresy = new JPanel();
    //Panel panel_WybierzPlik = new Panel();

    int[] dane_z_pliku;

    public okno() {

        super("Algorytmy genetyczne - BinPacking");

        setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocation(50, 50);
        
        setResizable(false);
        
        panel_WybierzPlik.setBorder(BorderFactory.createLineBorder(Color.gray));
        
        add(panel_WybierzPlik);
        
        
        
        
        jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
       // jLabel1.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

        jLabel1.setText("Plik: ");

        jLabel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel2.setBorder(null);

        jLabel_ilosc_elementow.hide();
        jLabel_rozmiar_pudelka.hide();

        jButtonchoosefile.setText("Wczytaj plik");
        jButtonchoosefile.setBounds(new Rectangle(130, 50, 120, 27));
        jButtonchoosefile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Wczytaj_plik(e);
            }
        });

        jButtonRun.hide();
        jButtonRun.setText("START!");
        jButtonRun.setBounds(new Rectangle(200, 100, 120, 27));
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButtonRun_actionPerformed(e);
            }
        });

       
        jLabelBestChromosome.setForeground(Color.red);
        jLabelBestChromosome.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelBestChromosome.setBounds(new Rectangle(2, 45, 486, 17));

        panel_WybierzPlik.add(jLabel1, null);
        panel_WybierzPlik.add(jLabel2, null);

        panel_WybierzPlik.add(jButtonchoosefile, null);

        panel_WybierzPlik.add(jButtonRun, null);
        panel_WybierzPlik.add(jLabelBestChromosome, null);

        //add(new JButton("Przycisk 1"));
        //add(new JButton("Przycisk 2"));
        //add(new JButton("Przycisk 3"));
        setVisible(true);
    }

    public void init() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        Font f = jLabel1.getFont();

        this.setSize(new Dimension(500, 700));

        Container contentPane = this.getContentPane();
        contentPane.setSize(500, 70);
        contentPane.setLocation(0, 20);
        contentPane.setLayout(new GridLayout(3, 1));

        panel_WybierzPlik.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel_WyswietlPlik.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel_Wykresy.setBorder(BorderFactory.createLineBorder(Color.gray));

        contentPane.add(panel_WybierzPlik);
        contentPane.add(panel_WyswietlPlik);
        contentPane.add(panel_Wykresy);

        Container contentdane_z_pliku = this.getRootPane();
        contentdane_z_pliku.setLocation(70, 500);

        jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel1.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

        jLabel1.setText("Plik: ");

        jLabel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel2.setBorder(null);

        jLabel_ilosc_elementow.hide();
        jLabel_rozmiar_pudelka.hide();

        jButtonchoosefile.setText("Wczytaj plik");
        jButtonchoosefile.setBounds(new Rectangle(130, 50, 120, 27));
        jButtonchoosefile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Wczytaj_plik(e);
            }
        });

        jButtonRun.hide();
        jButtonRun.setText("START!");
        jButtonRun.setBounds(new Rectangle(200, 100, 120, 27));
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButtonRun_actionPerformed(e);
            }
        });

        contentPane.setBackground(Color.white);
        jLabelBestChromosome.setForeground(Color.red);
        jLabelBestChromosome.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelBestChromosome.setBounds(new Rectangle(2, 45, 486, 17));

        panel_WybierzPlik.add(jLabel1, null);
        panel_WybierzPlik.add(jLabel2, null);

        panel_WybierzPlik.add(jButtonchoosefile, null);

        panel_Wykresy.add(jButtonRun, null);
        panel_WybierzPlik.add(jLabelBestChromosome, null);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (imgDev != null) {
            g.drawImage(imgDev, 10, 175, this);
        }
        if (imgFit != null) {
            g.drawImage(imgFit, 10, 375, this);
        }
    }

    void plotDeviationAndFitness(GA ga) {
        int xPos, yPos;
        double xScaleFactor, yScaleFactor;
        int xDim = 470;
        int yDim = 170;

        //PLOT THE AVERAGE DEVIATION OF CHROMOSOMES
        imgDev = createImage(xDim, yDim);
        imgDev.getGraphics().drawRect(10, 0, xDim - 11, yDim - 13);

        double maxDeviation = ga.getAvgDeviation(0);
        yScaleFactor = (double) (yDim - 24) / maxDeviation;
        xScaleFactor = (double) (xDim - 10) / ga.getMaxGenerations();

        imgDev.getGraphics().drawString("Average Deviation of Chromosomes", xDim / 2 - 90, 12);
        imgDev.getGraphics().drawString("0", 9, yDim);
        imgDev.getGraphics().drawString("" + ga.getMaxGenerations(), xDim - 30, yDim);
        imgDev.getGraphics().drawString("Number of Generations", xDim / 2 - 46, yDim);

        for (int i = 0; i < ga.getMaxGenerations(); i++) {
            xPos = 10 + (int) (xScaleFactor * (double) i);
            yPos = yDim - ((int) (yScaleFactor * ga.getAvgDeviation(i)) + 18);
            imgDev.getGraphics().drawOval(xPos, yPos, 2, 2);
        }

        //NOW PLOT THE AVERAGE FITNESS OF THE CHROMOSOMES
        imgFit = createImage(xDim, yDim);
        imgFit.getGraphics().drawRect(10, 0, xDim - 11, yDim - 13);

        double maxFitness = ga.getFittestChromosomesFitness();
        yScaleFactor = (double) (yDim - 10) / maxFitness;
        xScaleFactor = (double) (xDim - 10) / ga.getMaxGenerations();

        imgFit.getGraphics().drawString("Average Fitness of Chromosomes", xDim / 2 - 90, 12);
        imgFit.getGraphics().drawString("0", 9, yDim);
        imgFit.getGraphics().drawString("" + ga.getMaxGenerations(), xDim - 30, yDim);
        imgFit.getGraphics().drawString("Number of Generations", xDim / 2 - 46, yDim);

        for (int i = 0; i < ga.getMaxGenerations(); i++) {
            xPos = 10 + (int) (xScaleFactor * (double) i);
            yPos = 8 + yDim - ((int) (yScaleFactor * ga.getAvgFitness(i)));
            imgFit.getGraphics().setColor(Color.blue);
            imgFit.getGraphics().drawOval(xPos, yPos, 2, 2);
        }

        repaint(); //calls paint() and displays plot images
    }

    void Wczytaj_plik(ActionEvent e) {

        try {

            final JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            File plik = fc.getSelectedFile();

            //this.jLabel1.setText("Plik: ");
            this.jLabel2.setText(plik.toString());

            this.jLabel1.show();
            this.jLabel2.show();
            this.jButtonRun.show();

            dane_z_pliku = czytaj_plik(plik);//dziala(bez obslugi wyjatkow}

            //String indeksy = generuj_index_element(dane_z_pliku[1]);
        } catch (FileNotFoundException ex) {
            JOptionPane frame = new JOptionPane();
            JOptionPane.showMessageDialog(frame,
                    "Brak pliku.",
                    "Podany plik nie istnieje",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    void jButtonRun_actionPerformed(ActionEvent e) {

        imgDev = imgFit = null;
        paint(this.getContentPane().getGraphics());

        String indeksy = generuj_index_element(dane_z_pliku[1]);

        try {

            BinPacking test = new BinPacking(dane_z_pliku, indeksy);
            test.evolve();
            plotDeviationAndFitness(test);
            jLabelBestChromosome.setText(
                    "Best Chrom= "
                    + test.getFittestChromosome().toString()
                    + " (fitness= "
                    + test.getFittestChromosomesFitness()
                    + ")");
            
            for (int i = 1; i <= (int) (test.getFittestChromosomesFitness() * -1); i++) {

                System.out.print(i + ".  ");

                for (int j = 1; j <= test.ilosc_elementow_w_pudelku[i]; j++) {

                    System.out.print((char) test.zawartosc_pudelka[i][j] + " | ");
                }
                System.out.println(" ");

            }
            
        } catch (GAException gae) {
            System.out.println("Będzie błąd");
            System.out.println(gae.getMessage());
        }

    }
}
