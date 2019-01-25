package com.mastercom.bigdata;

import com.mastercom.bigdata.view.MainFrame;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import javax.swing.*;
import java.awt.*;
/**
 * Created by Kwong on 2017/9/22.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel(new SubstanceLookAndFeel());
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());

                    SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
                    SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
                    SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
                    SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
                    SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
//                    SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitlePainter());

                    MainFrame frame = MainFrame.getInstance();

                    frame.setVisible(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
