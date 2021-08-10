package type.tools.calculator;

import uicontrols.UIColors;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Graph {

    // For number of grid lines
    private int countX;
    private int countY;
    // For X & Y axis lines
    private double centerX;
    private double centerY;
    //private DijkstraParser djParser;
    // Invalid flag is set when DijkstraParser has been given
    // invalid input. When this flag is true, show a
    // message to the EncryptedUser.EncryptedUser.
    //private boolean invalidFlag;
    //private String message;

    /**
     * Full constructor. Sets the center of the pane
     * @param centX
     * @param centY
     */
    public Graph(double centX, double centY) {

        this.centerX = centX;
        this.centerY = centY;
        //this.djParser = new DijkstraParser();
    }

    /**
     * Sets the centerX of the pane
     * @param centX
     */
    public void setCenterX(double centX) {
        this.centerX = centX;
    }

    /**
     * Sets the center Y of the pane.
     * @param centY
     */
    public void setCenterY(double centY) {
        this.centerY = centY;
    }



    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                                  GRID AND AXIS                                                  *
     *                                                                                                                 *
     ******************************************************************************************************************/



    /**
     * Call to Create the graphPane and the x and y axis
     * @param colr
     * @param ht
     * @param wd
     * @return
     */
    public Pane createXYAxis(Color colr, double wd, double ht) {

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:" + UIColors.FM_GREY);
        double zeroY = centerY;
        double zeroX = centerX;

        Line xAxis = new Line(0 , zeroY , wd, zeroY);
        xAxis.setStroke(colr);
        Line yAxis = new Line(zeroX, 0 ,zeroX, ht );
        yAxis.setStroke(colr);

        pane.getChildren().addAll(xAxis, yAxis);
        return pane;
    }

    /**
     * Do not call... use createXYAxis
     * Creates the grid lines parallel to x and y axis
     * @param colr
     * @param ht
     * @param wd
     * @return
     */
    public Pane createGrid( Color colr, double wd, double ht) {

        System.err.println("*!*!*!* called createGrid() *!*!*!*");

        Pane pane = new Pane();
        countX = 0;
        countY = 0;
        // Zero is 1/2 of remainder for x and y
        int zeroWd = (int) (wd % 40) / 2;
        int zeroHt = (int) (ht % 40) / 2;

        // vertical grid lines
        for(int i = zeroWd; i < wd; i += 20) {

            Line vertL = new Line(i,0, i, ht);
            vertL.setStrokeWidth(.25);
            vertL.setStroke(colr);
            pane.getChildren().add(vertL);
            countX++;
        }
        // horizontal grid lines
        for(int i = zeroHt; i < ht; i += 20) {

            Line horzL = new Line(0,i, wd, i);
            horzL.setStrokeWidth(.25);
            horzL.setStroke(colr);
            pane.getChildren().add(horzL);
            countY++;
        }
        return pane;
    }

    public void axisNums(Pane pane, double wd, double ht) {

        double zeroY = centerY;
        double zeroX = centerX;

        double yPixel = (int) (ht % 40) / 2;// 9.5;
        double xPixel = (int) (wd % 40) / 2;// 12.5;
        yPixel += 9.5;
        //xPixel += ;

        /**   Y Axis Count  **/
        // Count down from ( countY - 1/2 ), a positive number,
        // to ( 0 - 1/2 of countY ) a negitive number.
        int iStart  = countY / 2; // upper Y or 0
        int iEnd    = 0 - iStart; // lower Y

        for(int i = iStart ; i >= iEnd; i--)
        {
            Text number = new Text( zeroX + 3, yPixel, Integer.toString(i) );
            number.setId("graphNumber");
            //number.setFont(Font.loadFont("File:font/Raleway-Medium.ttf", 16));
            pane.getChildren().add(number);
            yPixel += 20;
        }

        /** X Axis Count **/
        // Count from the left to right for X numbers
        // from 0 - countX / 2 (a negative number, to countX / 2 (A positive number)
        iStart = 0 - countX / 2;
        iEnd = countX / 2;

        for(int i = iStart; i < iEnd + 1; i++)
        {
            if(i != 0) {
                Text number = new Text(xPixel, zeroY + 10, Integer.toString(i));
                number.setId("graphNumber");
                pane.getChildren().add(number);
            }
            xPixel += 20;
        }
    }

}
