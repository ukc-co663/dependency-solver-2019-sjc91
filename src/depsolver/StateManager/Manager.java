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
import depsolver.RepoManager.Repo;
import depsolver.Result;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Sam
 */
public class Manager {
    public HashMap<String, Package> packages = new HashMap<>();
    
    
    public Manager copy(){
        Manager response = new Manager();
        response.packages.putAll(this.packages);
        return response;
    }
    
    public void AddPackage(String input, boolean markInstalled){
        String name = "";
        String version = "";
        
        String[] temp = input.split("=");
        name = temp[0];
        version = temp[1];
        
        Package found = packages.get(name);
        if(found==null){
            found = new Package(name, version, markInstalled);
        }else{
            found.AddVersion(version, markInstalled);
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
            found = new Package(name, version, false);
        }else{
            found.AddVersion(version, false);
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
