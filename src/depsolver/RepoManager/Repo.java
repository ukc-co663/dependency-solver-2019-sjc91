/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

import depsolver.Result;
import depsolver.StateManager.Manager;
import depsolver.StateManager.PackageState;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    
    public Item findPackage(String packageName){
        Item found = packages.get(packageName);
        return found;
    }
    
    public Result uninstall(Contract item, Manager curState) {
        Result resposne = new Result(curState);
        HashMap<String, PackageState> foundVersions = resposne.newState.findVersions(item);
        Iterator it = foundVersions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String pair_version = (String) pair.getKey();
            PackageState pair_state = (PackageState) pair.getValue();
            switch(pair_state)
            {
                case installed:
                    resposne.weight = resposne.weight + 10e6;
                    resposne.result.add("-" + item.name + "=" + pair_version);
                    break;
                case installling:
                    return null;

            }
        }
        
        return resposne;
    }
    
    public Result installVersion(Contract item, Item package_installing, Version package_version, Manager curState){
        //System.out.println(package_installing.getName() + "=" + package_version.getRevision().text_version);
        
        //create result obt
        Result resposne = new Result(curState);
        
        //check if already installed, or being installed
        PackageState state = resposne.newState.isInstalled(item);
        switch(state){
            case installed:
                return resposne;
            case installling:
                return null;
        }
        
        //check packages installing don't conflict with it
        if(resposne.newState.findConflicts(package_installing, package_version)){
            return null;
        }
        
        //add the package to the env as installing
        resposne.newState.AddPackage(package_installing, package_version, false);
        
        //check it doesn't conflict with packages already installed, installing
        for(Contract conflict : package_version.conflicts){
            Result temp = uninstall(conflict, resposne.newState); //try and uninstall the conflict
            if(temp==null) return null; //theres a conflict with an already installing package, cannot install this package
            resposne.result.addAll(temp.result);
            resposne.weight = resposne.weight + temp.weight;   
        }
        
        for(List<Contract> dependents : package_version.depends){
            Result best = null;
            for(Contract depend : dependents){
                Result temp = install(depend, resposne.newState.copy());  
                if(temp==null){ //cant install dependency. move to next done. dont break!
                    //break;
                }else if(best == null || temp.weight < best.weight){
                    best = temp;
                }
            }
            if(best==null){
                return null;
            }
            resposne.result.addAll(best.result);
            resposne.weight = resposne.weight + best.weight;     
        }
                
        resposne.newState.MarkInstalled(item);
        
        resposne.weight = resposne.weight + package_version.getSize();
        resposne.result.add("+" + package_installing.getName() + "=" + package_version.getRevision().pretty());
        return resposne;
    }
    
    public Result install(Contract item, Manager curState){
        Result resposne = new Result(curState);
        
        Item package_installing = packages.get(item.name);
        if(package_installing==null){
            //System.out.println("package not found");
        }
        
        List<Version> package_version = package_installing.findVersion(item.revision, item.cond);
        if(package_version.isEmpty()){
            //System.out.println("package_version not found");
        }
        
        Result best = null;
        for(Version version : package_version){
            Result temp = installVersion(item, package_installing, version, resposne.newState);  
            if(best == null || temp.weight < best.weight){
                if(temp!=null) best = temp;
            }
        }
        if(best==null) return null;
        resposne = best;
        
        return resposne;
    }

    
}
