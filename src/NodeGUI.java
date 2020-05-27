import javax.swing.*;

public class NodeGUI {
    JTextArea printingArea;
    NodeGUI(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        printingArea = new JTextArea(20, 40);
        printingArea.setEditable(false);
        JScrollPane scrollPane =
                new JScrollPane(printingArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    public void print(String s) {
        printingArea.append(s);
        printingArea.setCaretPosition(printingArea.getDocument().getLength());
    }

    public void println(String s) { print(s+"\n"); }

    public void println() { print("\n"); }
}
