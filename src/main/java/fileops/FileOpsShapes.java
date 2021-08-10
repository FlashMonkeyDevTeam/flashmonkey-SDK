package fileops;



import type.draw.shapes.GenericShape;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

//import sun.net.www.content.text.Generic;

/**
 * This class outputs files used for FMShapes into the program, and
 * inputs draw.shapes into files.
 *
 *     @author Lowell Stadelman
 */
public class FileOpsShapes//<T extends GenericShape>
{
    
    /**
     * Saves the draw.shapes array to the file in the parameter
     * @param shapes The array of draw.shapes
     * @param shapePath The relitive path to the file.
     */
    public void setShapesInFile(ArrayList<GenericShape> shapes, String shapePath) {
        //System.out.println("\n\n~^~^~^~^~^~^~^~^~ in shapes setListInFile() ~^~^~^~^~^~^~^~^\n\n");
    
        //File check = new File(shapePath);
       /* if(shapes.size() == 0) {
            System.err.println("No shapes are saved");
        }

        */
    
    
        try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(shapePath), 512))) {
            
            //System.out.println("Shapes size: " + shapes.size());
        
            for (int i = 0; i < shapes.size(); i++) {
                output.writeObject(shapes.get(i));
            }
        
            //System.out.println("\n\n~~~~~~~~ setListInFile() Completed ~~~~~~~\n\n");
        } catch (EOFException e) {
            //System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            //System.out.println(e.getMessage());
        } catch (IOException e) {
            //System.out.println("***** IOException: in connectBinaryOut Saving to file *******");
            //System.out.println(e.getMessage());
        } catch (Exception e) {
            //System.out.println("Unknown Exception: ");
            //System.out.println(e.getCause());
            //System.out.println(Arrays.toString(e.getStackTrace()));
            //System.out.println("FileName: " + shapePath);
        }
    }
    


    /**
     * Returns the flashCard list from the currently selected file
     * @return Returns the flashCard list from the currently selected file
     */
    public ArrayList getListFromFile(String shapePath)
    {
        return connectBinaryIn(shapePath);
    }

    /**
     * Inputs binary data from a file to the computer.
     * @param shapePath
     * @return
     */
    private ArrayList<GenericShape> connectBinaryIn(String shapePath)
    {
        //System.out.println("\n\n ~^~^~^~^~^~^~^~ in FileOpsShapes.connectBinaryIn, fileName: " + shapePath + " ~^~^~^~^~^~^~^~\n");

        ArrayList<GenericShape> shapes = new ArrayList<>(3);

        try(ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(shapePath))))
        {
           // for the println
           int i = 0;
            while(true)
            {
                shapes.add( (GenericShape) input.readObject());
                //System.out.println("\tshape " + i + ": " + (shapes.get(i++)).getClass() + "\n");
            }
        }
        catch(EOFException e)
        {
            // an expensive way to end a loop.
        }
        catch(FileNotFoundException e)
        {
            //System.err.println("\tFileOpsShapes File Not Found exception: connectBinaryIn() Line 106 ");
            //System.err.println(e.getMessage());
        }
        catch(IOException e)
        {
            //System.err.println("\t****** IO Exception in FlashCard.connectBinaryIn() *******");
            //e.getStackTrace();
            //e.getCause();
            //e.printStackTrace();
        }
        catch(Exception e)
        {
            // Set errorMsg in FlashMonkeyMain and let the ReadFlash class
            // handle the error in createReadScene()

            //System.err.println("\tUnknown Exception in connectBinaryIn: ");
            //System.err.println("\tFileName: " + shapePath);
            //System.err.println(e.getCause());
            //System.err.println(e.getStackTrace());

        }

        return shapes;
    }
}
