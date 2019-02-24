/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class Item {
    private String _name;
    public void setName(String name){ this._name = name; };
    public String getName(){ return _name; };    
    public List<Version> versions = new ArrayList<>();
    
    public Item(String name, String version, List<List<String>> depends, List<String> conflicts){
        this.setName(name);
        this.versions.add(new Version(version, conflicts, depends));
    }
    
    public void AddVersion(String version, List<List<String>> depends, List<String> conflicts){
        this.versions.add(new Version(version, conflicts, depends));
    }
}
