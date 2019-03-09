/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.StateManager;

import depsolver.BuildVersion;
import depsolver.Conditions;
import depsolver.Constraint;
import depsolver.RepoManager.Contract;
import depsolver.RepoManager.Item;
import depsolver.RepoManager.Repo;
import depsolver.RepoManager.Version;
import depsolver.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sam
 */
public class Manager {
    public HashMap<String, Package> packages = new HashMap<>();
    public HashMap<String, ArrayList<Contract>> conflicters = new HashMap<>();
    
    public Manager copy(){
        Manager response = new Manager();
        response.packages.putAll(this.packages);
        response.conflicters.putAll(this.conflicters);
        return response;
    }
        
    public boolean findConflicts(Item package_installing, Version package_version){
        
        
        ArrayList<Contract> contracts = conflicters.get(package_installing.getName());
        if(contracts==null) return false;
        
        for(Contract contract : contracts){
            switch(contract.cond){
                case LessThan:
                    if(package_version.getRevision().isLessThan(contract.revision)){
                        return true;
                    }
                    break;
                case LessEqualThan:
                    if(package_version.getRevision().isLessEqualThan(contract.revision)){
                        return true;
                    }
                    break;
                case GreaterThan:
                    if(package_version.getRevision().isGreaterThan(contract.revision)){
                        return true;
                    }
                    break;
                case GreaterEqualThan:
                    if(package_version.getRevision().isGreaterEqualThan(contract.revision)){
                        return true;
                    }
                    break;
                case EqualThan:
                    if(package_version.getRevision().isEqual(contract.revision)){
                        return true;
                    }
                    break;
                case All:
                    return true;
                
            }
        }
        
        return false;
    }
    
    public void AddPackage(Item package_installing, Version package_version, boolean markInstalled) {
        Package found = packages.get(package_installing.getName());
        if(found==null){
            found = new Package(package_installing.getName(), package_version.getRevision().text_version, markInstalled);
        }else{
            found.AddVersion(package_version.getRevision().text_version, markInstalled);
        }
        
        for(Contract curContract : package_version.conflicts){
            ArrayList<Contract> foundConflicter = conflicters.get(curContract.name);
            if(foundConflicter!=null){
                foundConflicter.add(curContract);
                conflicters.put(curContract.name,foundConflicter);
            }else{
                ArrayList<Contract> tmp = new ArrayList<Contract>();
                tmp.add(curContract);
                conflicters.put(curContract.name,tmp);
            }
        }
        
        packages.put(package_installing.getName(), found);
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
    
    public PackageState isInstalled(Constraint item){
        Package found = this.packages.get(item.name);
        if(found == null) return PackageState.notFound; 
        return found.hasVersion(item.version);
    }
    
    public HashMap<String, PackageState> findVersions(Contract item){
        HashMap<String, PackageState> response = new HashMap<>();
        
        Package found = this.packages.get(item.name);
        if(found == null) return response; 
        if(item.revision != null){
            if(item.cond == Conditions.EqualThan){
                PackageState temp = found.hasVersion(item.revision.text_version);
                if(temp != PackageState.notFound){
                    response.put(item.revision.text_version, temp);
                };
            }else if(item.cond == Conditions.All){
                response = found.versions;
            }else{
                Iterator it = found.versions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String pair_version = (String) pair.getKey();
                    PackageState pair_state = (PackageState) pair.getValue();
                    BuildVersion version = new BuildVersion(pair_version);
                    switch (item.cond){
                        case LessThan:
                            if(version.isLessThan(item.revision)){
                                response.put(pair_version, pair_state);
                            }
                            break;
                        case LessEqualThan:
                            if(version.isLessEqualThan(item.revision)){
                                response.put(pair_version, pair_state);
                            }
                            break;
                        case GreaterThan:
                            if(version.isGreaterThan(item.revision)){
                                response.put(pair_version, pair_state);
                            }
                            break;
                        case GreaterEqualThan:
                            if(version.isGreaterEqualThan(item.revision)){
                                response.put(pair_version, pair_state);
                            }
                            break;
                    }
                }
            }
        }else{
            response = found.versions;
        }
        return response;
    }
    
    public PackageState isInstalled(Contract item){
        Package found = this.packages.get(item.name);
        if(found == null) return PackageState.notFound; 
        if(item.revision != null){
            if(item.cond == Conditions.EqualThan){
                return found.hasVersion(item.revision.text_version);
            }
            if(item.cond == Conditions.All){
                Iterator it = found.versions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String pair_version = (String) pair.getKey();
                    PackageState pair_state = (PackageState) pair.getValue();
                    if(pair_state == PackageState.installed){
                        return PackageState.installed;
                    }
                }
                if(found.versions.isEmpty()) return PackageState.notFound;
                return PackageState.installling;
            }
            
            Iterator it = found.versions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String pair_version = (String) pair.getKey();
                PackageState pair_state = (PackageState) pair.getValue();
                BuildVersion version = new BuildVersion(pair_version);
                switch (item.cond){
                    case LessThan:
                        if(version.isEqual(item.revision)){
                            return pair_state;
                        }
                        break;
                    case LessEqualThan:
                        if(version.isLessEqualThan(item.revision)){
                            return pair_state;
                        }
                        break;
                    case GreaterThan:
                        if(version.isGreaterThan(item.revision)){
                            return pair_state;
                        }
                        break;
                    case GreaterEqualThan:
                        if(version.isGreaterEqualThan(item.revision)){
                            return pair_state;
                        }
                        break;
                }
            }
            return PackageState.notFound;
        }else{
            return PackageState.installed;  //fix - possible installed state when should be installing?
        }
    }

    public Result install(Constraint curConstraint, Repo curRepo) {
        return curRepo.install(new Contract(curConstraint.name+(curConstraint.version==null ? "" : "=" + curConstraint.version)), this);
    }

    public void uninstall(Constraint curConstraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
