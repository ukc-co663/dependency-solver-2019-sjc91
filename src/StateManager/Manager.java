/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateManager;

import depsolver.RepoManager.Item;
import java.util.HashMap;

/**
 *
 * @author Sam
 */
public class Manager {
    public HashMap<String, Package> packages = new HashMap<>();
    
    public void AddPackage(String input){
        String name = "";
        String version = "";
        
        String[] temp = input.split("=");
        name = temp[0];
        version = temp[1];
        
        Package found = packages.get(name);
        if(found==null){
            found = new Package(name, version);
        }else{
            found.AddVersion(version);
        }
        packages.put(name, found);
    }
}
