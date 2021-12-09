import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParameterWindow extends JFrame implements ActionListener {

    int width;
    int length;

    JTextField text = new JTextField();
    GraphRepresentationWindow GRW;

    JButton saveButton = new JButton("Save");
    JButton loadButton = new JButton("Load");
    JButton SPButton = new JButton("Submit");
    JButton SPDButton = new JButton("Submit");
    JButton ANButton = new JButton("Add");
    JButton AEButton = new JButton("Add");
    JButton RNButton = new JButton("Remove");
    JButton REButton = new JButton("Remove");
    JButton TSPButton = new JButton("Submit");


    public ParameterWindow(String flag, GraphRepresentationWindow GRW){
        this.GRW = GRW;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        if (flag.equals("s")){
            this.setLayout(new FlowLayout());
            text.setPreferredSize(new Dimension(400,35));
            saveButton.addActionListener(this);
            this.add(saveButton);
            this.add(new Label("Path:"));
            this.add(text);
            this.pack();
        }

        else if (flag.equals("l")){
            this.setLayout(new FlowLayout());
            text.setPreferredSize(new Dimension(400,35));
            loadButton.addActionListener(this);
            this.add(loadButton);
            this.add(new Label("Path:"));
            this.add(text);
            this.pack();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton){
            GRW.save(text.getText());
            this.dispose();
        }
        else if (e.getSource() == loadButton){
            GRW.load(text.getText());
            this.dispose();
        }
    }
}
