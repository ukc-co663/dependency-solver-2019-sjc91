/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateManager;

import java.util.HashSet;

/**
 *
 * @author Sam
 */
public class Package {
    public String name;
    public HashSet<String> versions = new HashSet<>();
    
    public Package(String name, String version){
        this.name = name;
        this.versions.add(version);
    }
    
    public void AddVersion(String version){
        if(!this.versions.contains(version)){
            this.versions.add(version);
        }
    }
}
