    /*
     * This file is part of Panopto-Java-Util.
     * 
     * Panopto-Java-Util is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     * 
     * Panopto-Java-Util is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     * 
     * You should have received a copy of the GNU General Public License
     * along with Panopto-Java-Util.  If not, see <http://www.gnu.org/licenses/>.
     * 
     * Copyright: Andrew Martin, Newcastle University
     * 
     */
package uk.ac.ncl.panopto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParser
{
    private BufferedReader bufRdr;

    private CSVParser(){};
    public CSVParser(String pathAndFile) throws FileNotFoundException
    {
        System.out.print("Attempting to open file "+pathAndFile);
        this.bufRdr = new BufferedReader(new FileReader(new File(pathAndFile)));
        System.out.println(", open");
    }

    public List<List<String>> parseCSV() throws IOException
    {
        //parse csv - string tokenizer*********************************************************
        System.out.print("Attempting to parse csv");
        List<List<String>> l = new ArrayList<List<String>>();
 	
	String line = null;
        bufRdr.readLine(); //Read past first line with headers on
        while((line = bufRdr.readLine()) != null)
	{
            if(!line.isEmpty())
            {
                String[] array = line.split("\",\""); 
                array[0] = array[0].replace("\"", ""); 
                array[array.length-1] = array[array.length-1].replace("\"", ""); 
                l.add(Arrays.asList(array));
            }
	}
        System.out.println(", done");
        return l;
    }
}
