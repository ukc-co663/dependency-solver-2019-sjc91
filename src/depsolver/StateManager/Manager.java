/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.StateManager;

import depsolver.Constraint;
import depsolver.RepoManager.Contract;
import depsolver.RepoManager.Item;
import depsolver.RepoManager.Repo;
import depsolver.Result;
import java.util.ArrayList;
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
    
    public void MarkInstalled(Contract item){
        String name = "";
        String version = "";
        
        name = item.name;
        version = item.revision.text_version;
        
        Package found = packages.get(name);
        if(found==null){
            System.out.print("not found to mark installed");
        }else{
            found.setInstalled(version);
        }
        packages.put(name, found);
    }
    
    public void AddPackage(Contract item){
        String name = "";
        String version = "";
        
        name = item.name;
        version = item.revision.text_version;
        
        Package found = packages.get(name);
        if(found==null){
            found = new Package(name, version);
        }else{
            found.AddVersion(version);
        }
        packages.put(name, found);
    }
    
    public PackageState isInstalled(Constraint item){
        Package found = this.packages.get(item.name);
        if(found == null) return PackageState.notFound; 
        return found.hasVersion(item.version);
    }
    
    public PackageState isInstalled(Contract item){
        Package found = this.packages.get(item.name);
        if(found == null) return PackageState.notFound; 
        if(item.revision != null){
            return found.hasVersion(item.revision.text_version);
        }else{
            return PackageState.notFound; 
        }
    }

    public Result install(Constraint curConstraint, Repo curRepo) {
        return curRepo.install(new Contract(curConstraint.name+(curConstraint.version==null ? "" : "=" + curConstraint.version)), this);
    }

    public void uninstall(Constraint curConstraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
