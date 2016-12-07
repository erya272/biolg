package com.jiao.file;
import java.io.File;  
import java.io.IOException;  
/** 
 * This class provides methods to create new file/directory 
 * @author kingsleylong 
 * 
 */  
public class DirMaker {  
      
    /** 
     * Enhancement of java.io.File#createNewFile() 
     * Create the given file. If the parent directory  don't exists, we will create them all. 
     * @param file the file to be created 
     * @return true if the named file does not exist and was successfully created; false if the named file already exists 
     * @see java.io.File#createNewFile 
     * @throws IOException 
     */  
    public  boolean createFile(File file) throws IOException {  
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    }  
      
    /** 
     * Enhancement of java.io.File#mkdir() 
     * Create the given directory . If the parent folders don't exists, we will create them all. 
     * @see java.io.File#mkdir() 
     * @param dir the directory to be created 
     */  
    public void makeDir(File dir) {  
        if(! dir.getParentFile().exists()) {  
            makeDir(dir.getParentFile());  
        }  
        dir.mkdir();  
    }  
      
    public static void main(String args[]) throws IOException {  
    	DirMaker mdir = new DirMaker();
        String filePath = "C:/temp/a/b/c/d/e/f/";  
        File file = new File(filePath);  
		mdir.makeDir(file); 
    }  
}  
