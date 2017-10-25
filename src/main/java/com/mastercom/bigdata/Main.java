package com.mastercom.bigdata;

import com.mastercom.bigdata.UI.MainFrame;
import com.mastercom.bigdata.db.EmbeddDBTable;
import com.mastercom.bigdata.logic.Constants;
import com.mastercom.bigdata.tools.sql.DBType;
import com.mastercom.bigdata.tools.sql.DBUtil;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //建表
        if (!checkTableFounded(Collections.singletonList(EmbeddDBTable.TB_CFG_JOB_INFORMATION))){
            System.out.println("没有在本地找到相应表，自动建表中...");
            int num = DBUtil.execute( EmbeddDBTable.getCreateSQL(EmbeddDBTable.TB_CFG_JOB_INFORMATION), DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD));
            System.out.println("自动建表完成");
        }

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
                    // SystemoutLog.printLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        /*final List<Future> futures = new ArrayList<>();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        Future future = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("-----进入定时任务-----");
                System.out.println("任务列表大小："+futures.size());
                futures.remove(0).cancel(true);
                System.out.println("-----定时任务被取消-----");
                System.out.println("任务列表大小："+futures.size());
                System.out.println("-----结束定时任务-----");
            }
        }, 5,5, TimeUnit.SECONDS);

        futures.add(future);*/
    }

    private static boolean checkTableFounded(List<EmbeddDBTable> tables){
        if(tables == null || tables.isEmpty()) return true;

        StringBuilder sb = new StringBuilder();
        for(EmbeddDBTable table : tables){
            sb.append("'").append(table.getTableName()).append("'").append(",");
        }
        String tableNameStr = sb.substring(0, sb.length()-1).toString();

        Connection conn = null;
        List<String[]> results = null;
        try {
            conn = DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD);
            results = DBUtil.executeQuery(String.format("SELECT DISTINCT TABLENAME FROM SYS.SYSTABLES WHERE TABLENAME IN (%s)",tableNameStr), conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                DBUtil.close(conn, null, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results.size() == tables.size();
    }
}
