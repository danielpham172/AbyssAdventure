package com.abyad.utils;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
public class FileReads
{
    public static String[] readFileToArray(String address){
        String[] output = { };
        
        try {
        	FileHandle file = Gdx.files.internal(address);
        	BufferedReader read = new BufferedReader(file.reader());
            //BufferedReader read = new BufferedReader(new FileReader(address));
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;
            
            while ((line = read.readLine()) != null){
                lines.add(line);
            }
            read.close();
            
            output = new String[lines.size()];
            for (int i = 0; i < output.length; i++){
                output[i] = lines.get(i);
            }
            return output;
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();             
        }
        catch(IOException ex) {
            ex.printStackTrace();   
        }
        
        return output;
    }
    
    public static void writeFileToArray(String[] array, String address){
        try{
        	File file = new File(address);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
            BufferedWriter write = new BufferedWriter(new FileWriter(address));
            
            for (int i = 0; i < array.length; i++){
                write.write(array[i]);
                if (i != array.length - 1) write.newLine();
            }
            
            write.close();
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace(); 
        }
        catch(IOException ex) {
            ex.printStackTrace();   
        }
    }
    
    public static ArrayList<String> stringParser(String str, String parseBy){
        ArrayList<String> tokens = new ArrayList<String>();
        recursiveParsing(str, parseBy, tokens);
        return tokens;
    }
    
    private static void recursiveParsing(String str, String parseBy, ArrayList<String> array){
        if (str.indexOf(parseBy) != -1){
            array.add(str.substring(0, str.indexOf(parseBy)));
            recursiveParsing(str.substring(str.indexOf(parseBy) + parseBy.length()), parseBy, array);
        }
        else{
            array.add(str);
        }
    }
}
