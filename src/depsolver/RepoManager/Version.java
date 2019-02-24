/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import depsolver.BuildVersion;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class Version {
    private BuildVersion _revision;
    private int _size;
    
    public BuildVersion getRevision(){ return this._revision; };
    public void setRevision(BuildVersion revision){ this._revision = revision; };
    public void setSize(int size){ this._size = size; };
    public int getSize(){ return this._size; };
    public List<Contract> conflicts = new ArrayList<>();
    public List<List<Contract>> depends = new ArrayList<>();
    
    
    public Version(BuildVersion version, List<String> conflicts, List<List<String>> depends, int size){
        this.setRevision(version);
        this.setConflicts(conflicts);
        this.setDepends(depends);
        this.setSize(size);
    }
    
    private void setConflicts(List<String> conflicts){
        for (String p : conflicts) {            
            this.conflicts.add(new Contract(p));
        }
    }
    
    private void setDepends(List<List<String>> depends){
        for (List<String> p : depends) {  
            List<Contract> temp = new ArrayList<>();
            for (String v : p) {  
                temp.add(new Contract(v));
            }    
            this.depends.add(temp);
        }
    }
}
