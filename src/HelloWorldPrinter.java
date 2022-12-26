import java.awt.*;
import java.awt.print.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelloWorldPrinter implements Printable {


    public int print(Graphics g, PageFormat pf, int page) throws
            PrinterException {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now we perform our rendering */
        g.drawString("Hello world, with BLACK!", 100, 100);
        g.setColor(Color.MAGENTA);
        g.drawString("Hello World, with MAGENTA!", 120,120);
        g.setColor(Color.CYAN);
        g.drawString("Hello world, with CYAN!", 140,140);
        g.setColor(Color.YELLOW);
        g.drawString("Hello world, with YELLOW!", 160,160);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    Runnable autoPrint = new Runnable() {
        @Override
        public void run() {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(HelloWorldPrinter.this);
            try {
                job.print();
            }
            catch(PrinterException e){
                /* The job did not successfully complete */
            }
        }
    };

    public static void main(String args[]) {
        /* To get the current timestamp */
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        HelloWorldPrinter hwp = new HelloWorldPrinter();

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(hwp.autoPrint, 0, 24, TimeUnit.HOURS);
        System.out.println(timestamp);
    }
}