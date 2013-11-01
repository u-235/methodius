import logic.Application;

import forms.MainForm;

public class Main
{
    public static void main(String[] args) {
        final MainForm t;
        Runtime r;
        long us;
        t = new MainForm();

        for (String s : args) {
            System.out.println(s);
        }

        Application.run();

        t.frame.setVisible(true);
        r = Runtime.getRuntime();

        while (true) {
            us = r.totalMemory() - r.freeMemory();
            t.lblUsage.setText("heap : " + us / 1000 + " kb ("
                            + (us * 100 / r.totalMemory()) + "%)");
            try {
                java.lang.Thread.sleep(750);
            }
            catch (InterruptedException e) {
            }
            if (t.exit == true) {
                break;
            }
        }/* */
    }
}
