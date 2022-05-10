package org.suai.trySwing.human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelData extends JPanel implements ActionListener {

    Container container = new Container();

    JLabel nameLabel = new JLabel("Имя: ");
    JTextField nameTextField = new JTextField("", 20);

    JLabel lastNameLabel = new JLabel("Фамилия: ");
    JTextField lastNameTextField = new JTextField("", 20);

    JLabel gender = new JLabel("Пол: ");
    JRadioButton genderMRadio = new JRadioButton("Мужской");
    JRadioButton genderFRadio = new JRadioButton("Женский");

    JLabel country = new JLabel("Страна: ");
    JComboBox countryComboBox;

    JLabel ageLabel = new JLabel("Возраст: ");
    JTextField ageTextField = new JTextField("", 20);

    public PanelData() {

        setLayout(new GridLayout(5,2));

        add(nameLabel);
        add(nameTextField);

        add(lastNameLabel);
        add(lastNameTextField);

        add(gender);
        container.setLayout(new GridLayout(1, 2));
        container.add(genderFRadio);
        container.add(genderMRadio);
        add(container);

        genderFRadio.addActionListener(this);
        genderMRadio.addActionListener(this);
        genderMRadio.setSelected(true);

        String countries[] = {"Россия", "Америка", "Германия", "Польша"};
        countryComboBox = new JComboBox(countries);
        add(country);
        add(countryComboBox);

        add(ageLabel);
        add(ageTextField);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == genderMRadio) {
            if (genderFRadio.isSelected()) {
                genderFRadio.setSelected(false);
            }
        }
        else if (e.getSource() == genderFRadio) {
            if (genderMRadio.isSelected()) {
                genderMRadio.setSelected(false);
            }
        }
    }


    public String getData() {
        String genderString;
        if (genderMRadio.isSelected()) {
            genderString = "Мужской";
        }
        else {
            genderString = "Женский";
        }

        return nameTextField.getText() + ", " + lastNameTextField.getText() + ", " + genderString + ", " +
                countryComboBox.getSelectedItem().toString() + ", " + ageTextField.getText();
    }
}
