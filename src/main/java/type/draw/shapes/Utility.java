/*
 * Copyright (c) 2019 - 2021. FlashMonkey Inc. (https://www.flashmonkey.xyz) All rights reserved.
 *
 * License: This is for internal use only by those who are current employees of FlashMonkey Inc, or have an official
 *  authorized relationship with FlashMonkey Inc..
 *
 * DISCLAIMER OF WARRANTY.
 *
 * COVERED CODE IS PROVIDED UNDER THIS LICENSE ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY
 *  KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES THAT THE COVERED
 *  CODE IS FREE OF DEFECTS, MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE
 *  ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY
 *  COVERED CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY OTHER
 *  CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS
 *  DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.  NO USE OF ANY COVERED
 *  CODE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 *
 */

package type.draw.shapes;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Utility {

    static double rise;
    static double run;

    /**
     * Given two points of a line(the second is an intersection), and a distance from the intersection/point,
     * return two points of a line that intersects the 1st lines 2nd point.
     * @param origin
     * @param mouse
     * @param length length of traingle
     * @return
     */
    public ArrayList<Point2D> calcRectBase(Point2D origin, Point2D mouse, double length) {
        ArrayList<Point2D> points = new ArrayList<>(4);
        // rise is 90 deg from points
        double headLength = length * .5;
        double triWidth = length * .3;
        double m = 0;
        // current slope
        m = calcSlope(origin, mouse);
        System.out.println("calcRectBase slope == " + m);
        // Returns intersecting point. intersectionPoint = pts[1]
        ArrayList<Point2D> pts = getPoints(origin, headLength, m);

        // new slope
        m = invertSlope() * -1;
        System.out.println("m = " + m);
        // triangle base points
        points.addAll(getPoints(pts.get(0), triWidth, m));
        // rectangle intersection points
        points.addAll(getPoints(pts.get(0),triWidth * .4, m));
        // rectangle end points
        points.addAll(getPoints(mouse, triWidth * .4, m));
        return points;
    }


    /**
     * @return returns M. The slope of the line.
     */
    private double calcSlope(Point2D pt1, Point2D pt2) {
        rise = pt1.getY() - pt2.getY() + .01;
        run =  pt1.getX() - pt2.getX() + .01;

        System.out.println(" rise: " + rise);
        System.out.println("------------");
        System.out.println(" run:  " + run);

        return rise / run;
    }

    private double invertSlope() {
        return run / rise;
    }



    /**
     * Helper method for calcRectBase. Calculates the
     * points
     * @param source
     * @param l
     * @param m
     */
    private ArrayList<Point2D> getPoints(Point2D source, double l, double m)
    {
        // m is the slope of line, and the
        // required Point lies distance l
        // away from the source Point
        Point a = new Point();
        Point b = new Point();

        if (run < 0){
            System.out.println("\nelse run < 0\n");
            double dx = l / Math.sqrt(1 + (m * m));
            double dy = m * dx;
            a.x = source.getX() + dx;
            a.y = source.getY() + dy;
            b.x = source.getX() - dx;
            b.y = source.getY() - dy;
        }
//final problem here.... left quadrants are wrong.
        else {
            System.out.println("\ndefault case \n");
        double dx = l / Math.sqrt(1 + (m * m));
        double dy = m * dx;
            a.x = source.getX() - dx;
            a.y = source.getY() - dy;
            b.x = source.getX() + dx;
            b.y = source.getY() + dy;
        }

        ArrayList<Point2D> line = new ArrayList<>(2);
        line.add(new Point2D(a.x, a.y));
        line.add(new Point2D(b.x, b.y));
        return line;
    }

    // Class to represent a co-ordinate
    // point
    private static class Point {
        double x, y;
        Point()
        {
            x = y = 0;
        }
        Point(double a, double b)
        {
            x = a;
            y = b;
        }
    }

    public static void main(String[] args) {
        Utility u = new Utility();
        //Point2D[] points = u.calcRectBase(new Point2D(24,13), new Point2D(24,21), 4,2);

    }
}
