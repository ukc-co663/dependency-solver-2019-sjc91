/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import java.util.HashMap;

/**
 *
 * @author Sam
 */
public class Repo {
    HashMap<String, Item> packages = new HashMap<>();
    
    public void addPackage(Package p){
        Item found = packages.get(p.getName());
        if(found==null){
            found = new Item(p.getName(),p.getVersion(),p.getDepends(),p.getConflicts());
        }else{
            found.AddVersion(p.getVersion(), p.getDepends(), p.getConflicts());
        }
        packages.put(p.getName(), found);
    }
}
