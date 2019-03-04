/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateManager;

import depsolver.Constraint;
import depsolver.RepoManager.Contract;
import depsolver.RepoManager.Item;
import depsolver.RepoManager.Repo;
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
    
    public boolean isInstalled(Constraint item){
        Package found = this.packages.get(item.name);
        if(found==null) return false;
        return found.hasVersion(item.version);
    }

    public ArrayList<String> install(Constraint curConstraint, Repo curRepo) {
        return curRepo.install(new Contract(curConstraint.name+(curConstraint.version==null ? "" : "=" + curConstraint.version))).result;
    }

    public void uninstall(Constraint curConstraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
