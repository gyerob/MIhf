package hu.gyerob.mihf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Frame extends JFrame {
	private static final long serialVersionUID = 7836500609945813342L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton tanit1;
	private JButton tanit2;
	private JButton tanit3;

	private ArrayList<float[]> m1;
	private ArrayList<float[]> m2;
	private ArrayList<float[]> m3;

	public Frame() {
		super("MI HF");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1300, 400));
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		table = new JTable();

		tanit1 = new JButton();
		tanit1.setText("Tanít 1-2");
		tanit1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tanit(1);
			}
		});

		tanit2 = new JButton();
		tanit2.setText("Tanít 1-3");

		tanit2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tanit(2);
			}
		});

		tanit3 = new JButton();
		tanit3.setText("Tanít 2-3");

		tanit3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tanit(3);
			}
		});

		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout(0, 0));
		subpanel.add(tanit1, BorderLayout.LINE_START);
		subpanel.add(tanit2, BorderLayout.CENTER);
		subpanel.add(tanit3, BorderLayout.LINE_END);

		contentPane.add(subpanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);

		m1 = new ArrayList<float[]>();
		m2 = new ArrayList<float[]>();
		m3 = new ArrayList<float[]>();

		readData();
		setTable();
	}

	private float ertekel(float[] suly, float[] adat) {
		float sum = suly[0] * adat[0] + suly[1] * adat[1] + suly[2] * adat[2]
				+ suly[3] * adat[3] + suly[4] * adat[4] + suly[5] * adat[5]
				+ suly[6] * adat[6] + suly[7] * adat[7] + suly[8] * adat[8]
				+ suly[9] * adat[9] + suly[10] * adat[10] + suly[11] * adat[11]
				+ suly[12] * adat[12];
		return (sum >= 0f) ? 1f : -1f;
	}

	private void tanit(int mode) {
		// d(k) az elvárt kimenet, +-1 értékek
		ArrayList<Integer> d = new ArrayList<Integer>();
		if (mode == 1) {
			// elsõ osztály értékeihez 1
			for (int i = 0; i < m1.size(); i++) {
				d.add(1);
			}
			// másodikéhoz -1 értéket rendelünk
			for (int i = 0; i < m2.size(); i++) {
				d.add(-1);
			}

			int input_meret = d.size();
			// kezdeti súlyozás 0;
			float[] sulyok = { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
					0f, 0f };
			float tanulasi_tenyezo = 0.1f;
			float hiba, epszilon, y;
			int iteracio = 0;

			do {
				hiba = 0;

				for (int i = 0; i < input_meret; i++) {

					// kimenet kiszámítása
					if (i < m1.size()) {
						y = ertekel(sulyok, m1.get(i));
						tableModel.setValueAt(y, i, 14);
					} else {
						y = ertekel(sulyok, m2.get(i - m1.size()));
						tableModel.setValueAt(y, i, 14);
					}

					epszilon = (float) d.get(i) - y;
					float[] adat;

					for (int j = 0; j < 13; j++) {
						if (i < m1.size()) {
							adat = m1.get(i);
						} else {
							adat = m2.get(i - m1.size());
						}

						sulyok[j] += tanulasi_tenyezo * epszilon * adat[j];
					}

					hiba += epszilon * epszilon;
				}
				System.out.println(iteracio);
				iteracio++;
			} while (hiba != 0f && iteracio < 10000);

		} else if (mode == 2) {
			for (int i = 0; i < m1.size(); i++) {
				d.add(1);
			}
			for (int i = 0; i < m3.size(); i++) {
				d.add(-1);
			}

			int input_meret = d.size();
			// kezdeti súlyozás 0;
			float[] sulyok = { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
					0f, 0f };
			float tanulasi_tenyezo = 0.1f;
			float hiba, epszilon, y;
			int iteracio = 0;

			do {
				hiba = 0;

				for (int i = 0; i < input_meret; i++) {

					// kimenet kiszámítása
					if (i < m1.size()) {
						y = ertekel(sulyok, m1.get(i));
						tableModel.setValueAt(y, i, 15);
					} else {
						y = ertekel(sulyok, m3.get(i - m1.size()));
						tableModel.setValueAt(y, i + m2.size(), 15);
					}

					epszilon = (float) d.get(i) - y;
					float[] adat;

					for (int j = 0; j < 13; j++) {
						if (i < m1.size()) {
							adat = m1.get(i);
						} else {
							adat = m3.get(i - m1.size());
						}

						sulyok[j] += tanulasi_tenyezo * epszilon * adat[j];
					}

					hiba += epszilon * epszilon;
				}
				System.out.println(iteracio);
				iteracio++;
			} while (hiba != 0f && iteracio < 10000);

		} else if (mode == 3) {
			for (int i = 0; i < m2.size(); i++) {
				d.add(1);
			}
			for (int i = 0; i < m3.size(); i++) {
				d.add(-1);
			}

			int input_meret = d.size();
			// kezdeti súlyozás 0;
			float[] sulyok = { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
					0f, 0f };
			float tanulasi_tenyezo = 0.1f;
			float hiba, epszilon, y;
			int iteracio = 0;

			do {
				hiba = 0;

				for (int i = 0; i < input_meret; i++) {

					// kimenet kiszámítása
					if (i < m2.size()) {
						y = ertekel(sulyok, m2.get(i));
						tableModel.setValueAt(y, i + m1.size(), 16);
					} else {
						y = ertekel(sulyok, m3.get(i - m2.size()));
						tableModel.setValueAt(y, i + m1.size(), 16);
					}

					epszilon = (float) d.get(i) - y;
					float[] adat;

					for (int j = 0; j < 13; j++) {
						if (i < m2.size()) {
							adat = m2.get(i);
						} else {
							adat = m3.get(i - m2.size());
						}

						sulyok[j] += tanulasi_tenyezo * epszilon * adat[j];
					}

					hiba += epszilon * epszilon;
				}
				System.out.println(iteracio);
				iteracio++;
			} while (hiba != 0f && iteracio < 10000);

		}
	}

	private void setTable() {
		String[] oszlopok = new String[] { "Class", "Alcohol", "Malic acid",
				"Ash", "Alcalinity of ash", "Magnesium", "Total phenols",
				"Flavanoids", "Nonflavanoid phenols", "Proanthocyanins",
				"Color intensity", "Hue", "OD280/OD315 of diluted wines",
				"Proline", "Tanít1-2", "Tanít1-3", "Tanít2-3" };
		int sorok = m1.size() + m2.size() + m3.size();
		int i = 0;

		tableModel = new DefaultTableModel(oszlopok, sorok);

		float data[] = null;

		while (i < sorok) {
			data = null;
			if (i < m1.size()) {
				data = m1.get(i);
				tableModel.setValueAt(1, i, 0);
			} else if (i < (m2.size() + m1.size())) {
				data = m2.get(i - m1.size());
				tableModel.setValueAt(2, i, 0);
			} else {
				data = m3.get(i - m1.size() - m2.size());
				tableModel.setValueAt(3, i, 0);
			}

			tableModel.setValueAt(data[0], i, 1); // Alcohol
			tableModel.setValueAt(data[1], i, 2); // Malic acid
			tableModel.setValueAt(data[2], i, 3); // Ash
			tableModel.setValueAt(data[3], i, 4); // Alcalinity of ash
			tableModel.setValueAt(data[4], i, 5); // Magnesium
			tableModel.setValueAt(data[5], i, 6); // Total phenols
			tableModel.setValueAt(data[6], i, 7); // Flavanoids
			tableModel.setValueAt(data[7], i, 8); // Nonflavanoid phenols
			tableModel.setValueAt(data[8], i, 9); // Proanthocyanins
			tableModel.setValueAt(data[9], i, 10); // Color intensity
			tableModel.setValueAt(data[10], i, 11); // Hue
			tableModel.setValueAt(data[11], i, 12); // OD280/OD315 of diluted
													// wines
			tableModel.setValueAt(data[12], i, 13); // Proline
			i++;
		}

		table.setModel(tableModel);
	}

	private void readData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"res/wine.data"));
			String line;
			String linearray[];
			float data[] = new float[13];

			while ((line = br.readLine()) != null) {
				linearray = null;
				linearray = line.split(",");

				data = new float[13];

				data[0] = Float.parseFloat(linearray[1]);
				data[1] = Float.parseFloat(linearray[2]);
				data[2] = Float.parseFloat(linearray[3]);
				data[3] = Float.parseFloat(linearray[4]);
				data[4] = Float.parseFloat(linearray[5]);
				data[5] = Float.parseFloat(linearray[6]);
				data[6] = Float.parseFloat(linearray[7]);
				data[7] = Float.parseFloat(linearray[8]);
				data[8] = Float.parseFloat(linearray[9]);
				data[9] = Float.parseFloat(linearray[10]);
				data[10] = Float.parseFloat(linearray[11]);
				data[11] = Float.parseFloat(linearray[12]);
				data[12] = Float.parseFloat(linearray[13]);

				if (linearray[0].equals("1")) {
					m1.add(data);
				} else if (linearray[0].equals("2")) {
					m2.add(data);
				} else if (linearray[0].equals("3")) {
					m3.add(data);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
