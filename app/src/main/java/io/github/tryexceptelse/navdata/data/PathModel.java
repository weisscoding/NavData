package io.github.tryexceptelse.navdata.data;

import android.support.annotation.Nullable;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Used to access, create, and modify paths.
 */
public final class PathModel {
    // todo: write test class for PathModel
    private static final String PATH_DIR_NAME = "Paths"; // name of dir storing path files.

    private final File pathDir; // File

    public PathModel(File pathDir){
        this.pathDir = pathDir;
    }

    public String[] getPathNames(){
        File files[] = pathDir.listFiles();
        // listFiles() returns null if File pathDir is not a directory or does not exist.
        if (files == null){
            return new String[0];
        }
        String names[] = new String[files.length];
        for(int i = 0; i < files.length; i++){
           names[i] = files[i].getName();
        }
        return names;
    }

    /**
     * Returns boolean of whether or not pathModel contains a path of passed name.
     * @param pathName: Object
     * @return boolean
     */
    public boolean contains(@Nullable Object pathName){
        if (pathName instanceof String){
            return new File(pathDir, (String) pathName).exists();
        } else {
            return false;
        }
    }

    /**
     * Returns boolean of whether model is empty of all paths.
     * @return boolean
     */
    public boolean isEmpty(){
        return getPathNames().length == 0;
    }

    /**
     * NoSuchElementException.
     * @param pathName: String
     * @return Path
     * @throws java.util.NoSuchElementException
     */
    public Path get(String pathName){
        final File pathFile = new File(pathDir, pathName);
        // if passed pathName does not exist, complain.
        if (!pathFile.exists()){
            throw new NoSuchElementException(
                    "PathModel.get: No path contained has name: " + pathName
            );
        }
        // otherwise, attempt to parse file for path
        try {
            return Path.fromJsonFile(pathFile);
        } catch (JSONException | IOException e){
            e.printStackTrace();
            throw new NoSuchElementException(String.format(
                    "PathModel.get: An exception occurred while attempting to parse file: %s " +
                            "in order to get path: %s",
                    pathFile.getAbsolutePath(), pathName
            ));
        }
    }

    // private / protected

    private void ensureDirExists(){
        if (!pathDir.exists() && !pathDir.mkdir()){
            throw new RuntimeException("Path Directory did not exist and could not be created.");
        }
    }
}