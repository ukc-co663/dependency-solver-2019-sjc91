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
    
    public Package(String name, String version, PackageState iniState){
        this.name = name;
        this.AddVersion(version, iniState);
    }
    
    public void AddVersion(String version, PackageState iniState){
        if(!this.versions.containsKey(version)){
            this.versions.put(version,iniState);
        }
    }
    
    public boolean isInstalled(String version){
        return (this.versions.get(version) == PackageState.installed);
    }
    
    public void setInstalled(String version){
        this.versions.put(version, PackageState.installed);        
    }
    
    public PackageState hasVersion(String version){
        PackageState response = this.versions.get(version);
        if(response != null) return response;
        return PackageState.notFound;
    }
}
