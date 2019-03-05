/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.StateManager;

import java.util.HashMap;
import java.util.HashSet;


/**
 *
 * @author Sam
 */
public class Package {
    public String name;
    
    public HashMap<String, PackageState> versions = new HashMap<>();
    
    public Package(String name, String version){
        this.name = name;
        this.AddVersion(version);
    }
    
    public void AddVersion(String version){
        if(!this.versions.containsKey(version)){
            this.versions.put(version,PackageState.installling);
        }
    }
    
    public boolean isInstalled(String version){
        return (this.versions.get(version) == PackageState.installed);
    }
    
    public void setInstalled(String version){
        this.versions.put(version, PackageState.installed);        
    }
    
    public PackageState hasVersion(String version){
        return this.versions.get(version);
    }
}
