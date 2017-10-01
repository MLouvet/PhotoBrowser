import photoComponent.PhotoComponent;
import photoComponent.view.PhotoUI;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {
    private ButtonGroup buttonGroup;
    private JLabel jLabelStatusBar;
    private JMenu jMenuFile;
    private JMenu jMenuView;
    private JMenuBar jMenuBar;
    private JMenuItem jMenuItemImport;
    private JMenuItem jMenuItemDelete;
    private JMenuItem jMenuItemQuit;
    private JPanel jPanelMain;
    private JRadioButtonMenuItem jRBMIPhotoViewer;
    private JRadioButtonMenuItem jRBMIBrowser;
    private JRadioButtonMenuItem jRBMISplitMode;
    private JScrollPane jScrollPane;
    private JToggleButton jToggleButton1;
    private JToggleButton jToggleButton2;
    private JToggleButton jToggleButton3;
    private JToolBar jToolBar;
    private PhotoComponent myPhotoComponent;

    public MainFrame() {

        //<editor-fold desc="Objects initialization">
        super("PhotoBrowser");
        buttonGroup = new ButtonGroup();
        jLabelStatusBar = new JLabel("JLABEL");

        jMenuFile = new JMenu("File");
        jMenuView = new JMenu("View");

        jMenuBar = new JMenuBar();

        jMenuItemDelete = new JMenuItem("Delete");
        jMenuItemImport = new JMenuItem("Import");
        jMenuItemQuit = new JMenuItem("Quit");

        jPanelMain = new JPanel();

        jRBMIPhotoViewer = new JRadioButtonMenuItem("Photo Viewer");
        jRBMIBrowser = new JRadioButtonMenuItem("Browser");
        jRBMISplitMode = new JRadioButtonMenuItem("Split Mode");

        jScrollPane = new JScrollPane();

        jToggleButton1 = new JToggleButton("Family");
        jToggleButton2 = new JToggleButton("Vacation");
        jToggleButton3 = new JToggleButton("School");

        jToolBar = new JToolBar();

        myPhotoComponent = new PhotoComponent();
        //</editor-fold>

        //<editor-fold desc="Organizing the frame">
        jMenuFile.add(jMenuItemImport);
        jMenuFile.add(jMenuItemDelete);
        jMenuFile.add(jMenuItemQuit);

        jMenuView.add(jRBMIPhotoViewer);
        jMenuView.add(jRBMIBrowser);
        jMenuView.add(jRBMISplitMode);
        buttonGroup.add(jRBMIPhotoViewer);
        buttonGroup.add(jRBMIBrowser);
        buttonGroup.add(jRBMISplitMode);

        jRBMIPhotoViewer.setSelected(true);

        jPanelMain.setLayout(new BorderLayout());
        jPanelMain.add(jScrollPane, BorderLayout.CENTER);
        jScrollPane.setViewportView(myPhotoComponent);

        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuView);

        setJMenuBar(jMenuBar);

        jToolBar.add(jToggleButton1);
        jToolBar.add(jToggleButton2);
        jToolBar.add(jToggleButton3);

        Container pane = getContentPane();

        pane.setLayout(new BorderLayout());
        pane.add(jToolBar, BorderLayout.NORTH);
        pane.add(jPanelMain, BorderLayout.CENTER);
        pane.add(jLabelStatusBar, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(500, 500));
        this.pack();
        //</editor-fold>

        //<editor-fold desc="Setting up events">
        JFrame frame = this;

        jMenuItemImport.addActionListener(event -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                myPhotoComponent.loadImage(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        jMenuItemQuit.addActionListener(event -> {
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        ActionListener actionUnimplementedListerner = e -> {
            Object o = e.getSource();
            String msg = "Unexpected event raised";

            if (o.equals(jMenuItemDelete)) msg = "Not implemented: Deletion";
            else if (o.equals(jRBMIBrowser)) msg = "Not implemented: Browser Mode";
            else if (o.equals(jRBMIPhotoViewer)) msg = "Not implemented: Photo viewer Mode";
            else if (o.equals(jRBMISplitMode)) msg = "Not implemented: Split Mode";
            else if (o.equals(jToggleButton1)) msg = "Not implemented: Family Mode";
            else if (o.equals(jToggleButton2)) msg = "Not implemented: Vacation Mode";
            else if (o.equals(jToggleButton3)) msg = "Not implemented: School Mode";

            jLabelStatusBar.setText(msg);
        };

        jMenuItemDelete.addActionListener(actionUnimplementedListerner);
        jRBMIBrowser.addActionListener(actionUnimplementedListerner);
        jRBMIPhotoViewer.addActionListener(actionUnimplementedListerner);
        jRBMISplitMode.addActionListener(actionUnimplementedListerner);
        jToggleButton1.addActionListener(actionUnimplementedListerner);
        jToggleButton2.addActionListener(actionUnimplementedListerner);
        jToggleButton3.addActionListener(actionUnimplementedListerner);
        //</editor-fold>


    }

    public static void main(String args[]) {
        UIManager.put(PhotoUI.UI_CLASS_ID, "PhotoBrowserUI");
        //Uses system look&feel instead of the base one.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }

        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
