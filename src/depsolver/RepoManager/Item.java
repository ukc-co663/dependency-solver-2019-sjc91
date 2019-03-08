/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import depsolver.BuildVersion;
import depsolver.Conditions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 *
 * @author Sam
 */
public class Item {
    private String _name;
    public void setName(String name){ this._name = name; };
    public String getName(){ return _name; };    
    public List<Version> versions = new ArrayList<>();
    
    public Item(String name, String version, List<List<String>> depends, List<String> conflicts, int size){
        this.setName(name);
        this.versions.add(new Version(new BuildVersion(version), conflicts, depends, size));
    }
    
    public void AddVersion(String version, List<List<String>> depends, List<String> conflicts, int size){
        this.versions.add(new Version(new BuildVersion(version), conflicts, depends, size));
    }
    
    public List<Version> findVersion(BuildVersion findVersion, Conditions cond){
        
        
        List<Version> temp = new ArrayList<Version>();
        if(findVersion == null || findVersion.text_version == "") return this.versions;
        for(Version version : versions){
            switch(cond){
                case LessThan:
                    if(version.getRevision().isLessThan(findVersion)) temp.add(version);
                    break;
                case LessEqualThan:
                    if(version.getRevision().isLessEqualThan(findVersion)) temp.add(version);
                    break;
                case GreaterThan:
                    if(version.getRevision().isGreaterThan(findVersion)) temp.add(version);
                    break;
                case GreaterEqualThan:
                    if(version.getRevision().isGreaterEqualThan(findVersion)) temp.add(version);
                    break;
                case EqualThan:
                    if(version.getRevision().isEqual(findVersion)) temp.add(version);
                    break;
            }
        }
        return temp;
    }
    
    public Version findSmallestVersion(){
        Version response = null;
        for(Version version : versions){
            if(response==null || response.getSize() > version.getSize()){
                response = version;
            }
        }
        return response;
    }
}
