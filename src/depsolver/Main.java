/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver;

/**
 *
 * @author Sam
 */
import depsolver.StateManager.Manager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import depsolver.RepoManager.Repo;
import depsolver.RepoManager.Package;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Repo curRepo = new Repo();
    public static Manager initialState = new Manager();
    public static List<Constraint> constraintsState = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
        List<Package> repo = JSON.parseObject(readFile(args[0]), repoType);
        TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
        List<String> initial = JSON.parseObject(readFile(args[1]), strListType);
        List<String> constraints = JSON.parseObject(readFile(args[2]), strListType);

        for (Package p : repo) {
            curRepo.addPackage(p);           
        }
        
        for (String p : initial) {
            initialState.AddPackage(p);
        }
        
        for (String p : constraints) {
            constraintsState.add(new Constraint(p));
        }
        
        ArrayList<String> commands = new ArrayList<String>();
        
        for (Constraint curConstraint : constraintsState) {
            if(curConstraint.action == Action.install){
                if(!initialState.isInstalled(curConstraint)){
                    ArrayList<String> output;
                    output = initialState.install(curConstraint, curRepo);
                    commands.addAll(output);
                }
            }else if(curConstraint.action == Action.uninstall){
                if(initialState.isInstalled(curConstraint)){
                    initialState.uninstall(curConstraint);
                }
            }
        }
        String jsonString = JSON.toJSONString(commands,true);
        System.out.print(jsonString);
    }
   

    static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(line -> sb.append(line));
        return sb.toString();
    }
}
