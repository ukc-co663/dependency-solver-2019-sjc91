/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import depsolver.Result;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sam
 */
public class Repo {
    HashMap<String, Item> packages = new HashMap<>();
    
    public void addPackage(Package p){
        Item found = packages.get(p.getName());
        if(found==null){
            found = new Item(p.getName(),p.getVersion(),p.getDepends(),p.getConflicts(), p.getSize());
        }else{
            found.AddVersion(p.getVersion(), p.getDepends(), p.getConflicts(), p.getSize());
        }
        packages.put(p.getName(), found);
    }
    
    public Result install(Contract item){
        Item package_installing = packages.get(item.name);
        if(package_installing==null){
            System.out.println("package not found");
        }
        Version package_version = package_installing.findVersion(item.revision, item.cond);
        
        //String installList = "";
        Result resposne = new Result();
        
        for(List<Contract> dependents : package_version.depends){
            Result best = null;
            for(Contract depend : dependents){
                Result temp = install(depend);  
                if(best == null || temp.weight < best.weight) best = temp;
            }
            resposne.result = resposne.result + best.result;
            resposne.weight = resposne.weight + best.weight;
        }
        resposne.weight = resposne.weight + package_version.getSize();
        resposne.result = resposne.result + "+" + package_installing.getName() + "=" + package_version.getRevision().pretty() + "\n";
        return resposne;
    }
}
