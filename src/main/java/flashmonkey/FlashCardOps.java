

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

package flashmonkey;
import common.FMFields;
import java.io.*;
import java.util.*;
import org.slf4j.LoggerFactory;



/*******************************************************************************
 *
 * <p>Explanation: This class was formerly the original FlashCard
 * class that contained all varaibles and methods related
 * to FlashCards and FlashCard operations. To minimize the
 * number of variables that are instantiated and saved
 * FlashCardOps or FlashCard operations remained in this class. It was
 * renamed to FlashCardOps, and FlashCard specific varaibles
 * were moved to a new flashcard class.</p>
 *
 *
 * <p>Not compatible with Version 20170512 and earlier do
 * to flashCard class restructuring. </p>
 *
 * <p>CLASS DESCRIPTION: This is the FlashCard class and is a child to the AnswerMM
 * class. It contains variables and methods for the the FlashCard class.</p>
 * <ul>
 * <li> This class contains methods and variables specific to create a FlashCard. This
 * class may at a future date, be seperated into seperate elements to prevent
 * unneccessary variables from being saved to the flashList and simplify
 * modifications and management. </li>
 *
 * <li>This class outputs flashcards to a binary file, it contains an array that
 * is used to read the flashcards. This is done so that this class may
 * remain server side. The EncryptedUser.EncryptedUser will not have access to the correct answer
 * unless they are using it for study sessions. </li>
 * </ul>
 *
 * @version iOOily FlashMonkeyMM Date: 2018/08/04
 * @author Lowell Stadelman
 * @TODO deserialize files with restrictions. ValidatingObjectInputStream in = new ValidatingObjectInputStream(fileInput); in.accept(Foo.class);
 ******************************************************************************/

public class FlashCardOps//< T extends Comparable<T>> extends FlashCardMM<T> implements Comparable<T>
{
    /* SINGLETON */
    //private volatile static FlashCardOps CLASS_INSTANCE;
    private static FlashCardOps CLASS_INSTANCE;
    
    // Logging reporting level is set in src/main/resources/logback.xml
    private final static ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FlashCardOps.class);


    private static final long serialVersionUID = FlashMonkeyMain.VERSION;
    // count, To keep track of when flashCards are added to an array.
    // Prevents unneccessary communications with
    // cloud servers.

    private static String fileName = "default";


    /**
     * Stores the flashcards that are created from the binary file,
     * that are added at the head of the list. And that are
     * stored back to the file.
     */
    private static ArrayList<FlashCardMM> flashListMM = new ArrayList<>();


    private FlashCardOps() {
        //LOGGER.setLevel(Level.DEBUG);
    }
    
    
    public static synchronized FlashCardOps getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new FlashCardOps();
        }
        return CLASS_INSTANCE;
    }


    // ************************************** ******* **************************************
    // ************************************** SETTERS **************************************
    // ************************************** ******* **************************************
    
    
    /**
     * Sets the flashList to the parameter
     * @param flashList
     */
    public void setFlashList(ArrayList<FlashCardMM> flashList) {
        this.flashListMM = flashList;
    }




    // ************************************** ******* **************************************
    // ************************************** GETTERS **************************************
    // ************************************** ******* **************************************
    
    /**
     * getFlashList
     * @return ArrayList of FlashCards
     */
    public final ArrayList<FlashCardMM> getFlashList() {
        return flashListMM;
    }
    

    /**
     * getElement() 
     * @param i
     * @return returns a new FlashCardMM object cloned from the ArrayList
     */
    public FlashCardMM getElement(int i) {
        try {
            return new FlashCardMM(flashListMM.get(i));
        }
        catch(IndexOutOfBoundsException e) {
            return null;
        }                
    }

    
    
    
    
    // ************************************** ******* **************************************
    // ************************************** OTHERS  **************************************
    // ************************************** ******* **************************************
    
    

    
    /**
     * copyList() Creates a deep copy of the ArrayList.
     * @param original
     * @return a copy of the FlashList of type {@link FlashCardMM} in the
     * argument
     */
    public ArrayList cloneList(ArrayList<FlashCardMM> original) {
        LOGGER.info("---&&&--- cloneList called ---&&&---");
        ArrayList<FlashCardMM> cloneList = new ArrayList<>(original.size());
        original.forEach((element) -> {
            FlashCardMM fcClone = element.clone();
            cloneList.add(fcClone);
        });
        
        return cloneList;
    }
    
    
    /**
     * Checks if file exists. If not then create it.
     * @param fileName
     * @param folder
     * @return
     */
    public boolean fileExists(String fileName, File folder ) {
        boolean bool = false;
        File fullPathFileName = new File(folder + "/" + fileName);
        LOGGER.info("fileExists() fullPathFileName: {}", fullPathFileName.getPath());
        try {
            if(!folder.isDirectory()) {
                folder.mkdirs();
                fullPathFileName.createNewFile();
                bool = true;
            }
            else if(fullPathFileName.exists()) {
                bool = true;
            }
            else {
                fullPathFileName.createNewFile();
                bool = true;
            }
        }
        catch(NullPointerException e) {
            //System.out.println(e.getMessage());
            LOGGER.warn("\tError no files in directory ");
            System.exit(0);
        }
        catch(IOException e) {
            //System.out.println("\tProblems creating " + fullPathFileName);
            LOGGER.warn("ERROR {}\n{}",e.getMessage());
            e.printStackTrace();
        }
        return bool;
    }
    


    
    /**
     * Clears the current card deck. Used when a new card deck should be created
     * from list of flashCards existing in the selected file. Does not recreate
     * the flashList from the file.
     */
    public void clearFlashList() {
    	flashListMM.clear();
    }

    /**
     * Saves the current flashList to file and the cloud(if connected),
     * if the file is > 0 cards. Used by
     * ReadFlash.
     */
    public void saveFlashList() {
        LOGGER.debug("*!*!* FlashOps saveFlashlist *!*!*");

        if(flashListMM.size() > 0) {
            // keep the last element
            setListinFile(flashListMM, '+');
        }
        LOGGER.debug("in saveFlashList and flashListMM is size 0");
    }

    /**
     * @return Returns the flashCard list from the currently selected file
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FlashCardMM> getListFromFile() {
        final File check = new File(FMFields.DECK_FULL_NAME.get());
        LOGGER.info("calling getListFromFile, filePath: " + check.getPath() );

        if(check.exists()) {
            LOGGER.debug("fileName exists {}", fileName);
            return connectFileIn(fileName);
        }
        else {
            LOGGER.debug("fileName does not exist, creating new arrayList");
            return new ArrayList();
        }
    }

    ObjectInputStream input;
    /**
     * Inputs from a local binary/serialized file to an ArrayList.
     * @param fileName
     * @return
     */
    protected ArrayList<FlashCardMM> connectFileIn(String fileName) {
        LOGGER.info("\n\n~~~~  In connectBinaryIn  ~~~~");
        LOGGER.debug("Calling method. ???: " + Thread.currentThread().getStackTrace()[3].getMethodName());
        try {


            FileInputStream fileIn = new FileInputStream(FMFields.DECK_FULL_NAME.get());

            input = new ObjectInputStream(new BufferedInputStream(fileIn));
            flashListMM.clear();

            while(flashListMM.add((FlashCardMM)input.readObject()));

            LOGGER.info("flashlist size: {}", flashListMM.size());

        } catch(ClassNotFoundException e) {
            LOGGER.warn("\tClassNotFound Exception: connectBinaryIn() Line 1134 FlashCard");

        } catch(EOFException e) {
            //LOGGER.warn("\tEnd of file Exception: connectBinaryIn() Line 1134 FlashCard");

        } catch(FileNotFoundException e) {
            LOGGER.warn("\tFile Not Found exception: connectBinaryIn() Line 1134 FlashCard");
            // System.out.println(e.getMessage());
            //e.printStackTrace();

        } catch(IOException e) {
            LOGGER.warn("\t****** IO Exception in FlashCard.connectBinaryIn() *******");
            e.printStackTrace();

        } catch(Exception e) {
            // Set errorMsg in FlashMonkeyMain and let the ReadFlash class
            // handle the error in createReadScene()
            //        FlashMonkeyMain.setErrorMsg(" --   Oooph my bad!!!   --\n" +
            //                "The flash card deck you chose \n\"" + ReadFlash.getDeckName()
            //                + "\"\n will not work with this version of FlashMonkey" );

            LOGGER.warn("\tUnknown Exception in connectBinaryIn: ");
            e.printStackTrace();
        } finally {
            closeInStream(input, null);
        }

        return flashListMM;
    }


    ObjectOutputStream output;
    /**
     * <P><b>USE</b> when an empty element exists at the end of the list. </P>
     * <p>Outputs any arraylist including FlashList (given in the parameter) to a serialized
     *             file from the arrayList in the parameter.</p>
     * @param arrayList FlashList or other ArrayList
     * @param minus The minus that defines the folder where this
     *             file will be created. If char = '-' then
     *             the last element is subtracted from the list. This should be used
     *             when there is an empty element at the end of the list.
     *
     */
    public void setListinFile(ArrayList arrayList, char minus) {

        LOGGER.info("\nsetListInFile with TYPE\n");

        File folder = new File(FMFields.DECK_FULL_NAME.get());

        //       UserData userData = new UserData();
        //        CloudOps co = new CloudOps();
        //fileName = ReadFlash.getInstance().getDeckName() + ".dat";

        LOGGER.info(" in setListInFile(ArrayList). path is: " + folder + "/" + fileName +
                "\n and numCards: " + arrayList.size());

        LOGGER.info("setListInFile with minus: {}", minus);

        // action
        try
        {
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(folder + "/" + fileName), 512));
            if(minus == '-') {
                for (int i = 0; i < arrayList.size() - 1; i++) {
                    output.writeObject(arrayList.get(i));
                }
            } else {

                LOGGER.info("setListInFile(w/ minus: {}) building list to full size", minus);

                for (int i = 0; i < arrayList.size(); i++) {
                    output.writeObject(arrayList.get(i));
                }
            }
            // Send the String file
            //co.connectCloudOut('t', userData.getUserName(), fileName);
        }
        catch(EOFException e) {
            //System.out.println(e.getMessage() + "\n" + e.getStackTrace());
            //e.printStackTrace();
        }
        catch(FileNotFoundException e) {
            LOGGER.warn(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
        }
        catch(IOException e) {
            LOGGER.warn(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
        }
        catch(Exception e ) {
            LOGGER.warn("Unknown Exception: " + "\n" + e.getStackTrace());
            e.printStackTrace();
        }
        finally{
            closeOutStream(output, null);
        }
    }


    
    /**
     * Closes the input stream
     * @param closeMe
     * @param message
     */
	private void closeInStream(ObjectInputStream closeMe, String message) {
		try {
			//System.out.println("closeInStream() method in FlashCard");
			closeMe.close();  // To prevent the compiler from
			// complaining and runtime errors
		}
		catch(NullPointerException e) {
			if (message == null) {
				// do nothing
			}
			else {
				//System.out.println(message);
			}
		}
		catch(EOFException e) {
			LOGGER.warn("End Of File Exception while closing");
			//System.out.println(e.getMessage());
			//System.out.println("Exiting ...");
			System.exit(0);
		}
		catch(IOException e) {
			//System.out.println("IOFile Exception: while closing" + e.getMessage());
			//System.out.println("Exiting ...");
			System.exit(0);
		}
	}
	
	private void closeOutStream(ObjectOutputStream closeMe, String message) {
		try {
			closeMe.close();  // To prevent the compiler from
			// complaining and runtime errors
		}
		catch(NullPointerException e) {
			if (message == null) {
				// do nothing
			}
			else {
				//System.out.println(message);
			}
		}
		catch(EOFException e) {
			System.exit(0);
		}
		catch(IOException e) {
			System.exit(0);
		}
	}
	
	



    /****** TESTING METHODS *****/
/*
    @FMAnnotations.DoNotDeployMethod
    public Point2D getNewNameFieldXYforTest() {
        FileSelectPane fsp = new FileSelectPane();
    return new Point2D(fsp.newNameField.getBoundsInLocal().getMinX() + 10, fsp.newNameField.getBoundsInLocal().getMinY() + 10);
    }

 */
}



