package notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.awt.print.PrinterException;
import javax.swing.event.*;
import java.util.regex.Pattern;

public class Notepad extends JFrame implements ActionListener{
    JTextArea area;
    String text;
    JLabel statusBar; 

    Notepad() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Notepad");

        ImageIcon notepadIcon = new ImageIcon(getClass().getResource("/notepad/icons/notepad.png"));
        Image icon = notepadIcon.getImage();
        setIconImage(icon);

        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(Color.PINK);

        JMenu file = new JMenu("File");
        JMenuItem newdoc = new JMenuItem("New");
        newdoc.addActionListener(this);
        newdoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(this);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(this);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        file.add(newdoc);
        file.add(open);
        file.add(save);
        file.add(print);
        file.add(exit);
        menubar.add(file);

        JMenu edit = new JMenu("Edit");

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem find = new JMenuItem("Find");
        find.addActionListener(this);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        JMenuItem replace = new JMenuItem("Replace");
        replace.addActionListener(this);
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectAll);
        edit.add(find);
        edit.add(replace);
        menubar.add(edit);

        JMenu helpmenu = new JMenu("Help");
        JMenuItem help = new JMenuItem("About");
        help.addActionListener(this);
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        helpmenu.add(help);
        menubar.add(helpmenu);

        setJMenuBar(menubar);

        area = new JTextArea();
        area.setFont(new Font("SansSerif", Font.PLAIN, 18));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

      
        statusBar = new JLabel("Ln 1, Col 1 | Words: 0 | Chars: 0");
        add(statusBar, BorderLayout.SOUTH);

       
        area.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int pos = area.getCaretPosition();
                try {
                    int line = area.getLineOfOffset(pos) + 1;
                    int col = pos - area.getLineStartOffset(line - 1) + 1;
                    String textContent = area.getText();
                    int wordCount = textContent.isEmpty() ? 0 : textContent.trim().split("\\s+").length;
                    int charCount = textContent.length();
                    statusBar.setText("Ln " + line + ", Col " + col + " | Words: " + wordCount + " | Chars: " + charCount);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("New")){
            area.setText("");
        }

        else if (e.getActionCommand().equals("Open")){
            JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Only .txt files","txt"));
            int action = chooser.showOpenDialog(this);
            if(action!= JFileChooser.APPROVE_OPTION) return;
            try{
                BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                area.read(reader,null);
            } catch(Exception a){
                a.printStackTrace();
            }
        }

        else if (e.getActionCommand().equals("Save")){
            JFileChooser saveas = new JFileChooser();
            saveas.setFileFilter(new FileNameExtensionFilter("Text Files","txt"));
            int action = saveas.showSaveDialog(this);
            if(action != JFileChooser.APPROVE_OPTION) return;
            File file = saveas.getSelectedFile();
            if(!file.getName().endsWith(".txt")) file = new File(file.getAbsolutePath()+".txt");
            try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
                area.write(out);
            } catch(Exception ex){ ex.printStackTrace(); }
        }

        else if (e.getActionCommand().equals("Print")){
            try{ area.print(); } catch(PrinterException ex){ ex.printStackTrace(); }
        }

        else if (e.getActionCommand().equals("Exit")){
            System.exit(0);
        }

        else if (e.getActionCommand().equals("Copy")){
            text = area.getSelectedText();
        }

        else if (e.getActionCommand().equals("Paste")){
            area.insert(text, area.getCaretPosition());
        }

        else if (e.getActionCommand().equals("Cut")){
            text = area.getSelectedText();
            area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
        }

        else if (e.getActionCommand().equals("Select All")){
            area.selectAll();
        }

        
        else if (e.getActionCommand().equals("Find")){
            JTextField field = new JTextField();
            JCheckBox caseCheck = new JCheckBox("Case Sensitive");

            Object[] message = {"Find:", field, caseCheck};
            int option = JOptionPane.showConfirmDialog(this, message, "Find", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION){
                String findWord = field.getText();
                if(findWord != null && !findWord.isEmpty()){
                    String textContent = area.getText();
                    int index;
                    if(caseCheck.isSelected()){
                        index = textContent.indexOf(findWord);
                    } else {
                        index = textContent.toLowerCase().indexOf(findWord.toLowerCase());
                    }

                    if(index != -1){
                        area.select(index, index + findWord.length());
                    } else {
                        JOptionPane.showMessageDialog(this, "Word not found");
                    }
                }
            }
        }

        else if (e.getActionCommand().equals("Replace")){
            JTextField findField = new JTextField();
            JTextField replaceField = new JTextField();
            JCheckBox caseCheck = new JCheckBox("Case Sensitive");

            Object[] message = {"Find:", findField, "Replace with:", replaceField, caseCheck};
            int option = JOptionPane.showConfirmDialog(this, message, "Replace", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.OK_OPTION){
                String findWord = findField.getText();
                String replaceWord = replaceField.getText();
                if(findWord != null && replaceWord != null){
                    String textContent = area.getText();
                    if(caseCheck.isSelected()){
                        area.setText(textContent.replace(findWord, replaceWord));
                    } else {
                        
                        area.setText(textContent.replaceAll("(?i)"+ Pattern.quote(findWord), replaceWord));
                    }
                }
            }
        }

        else if (e.getActionCommand().equals("About")){
            new About().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
