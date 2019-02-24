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
    
    public static void main(String[] args) throws IOException {
        TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
        List<Package> repo = JSON.parseObject(readFile(args[0]), repoType);
        //TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
        //List<String> initial = JSON.parseObject(readFile(args[1]), strListType);
        //List<String> constraints = JSON.parseObject(readFile(args[2]), strListType);

        for (Package p : repo) {
            System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
            curRepo.addPackage(p);
            
//            for (List<String> clause : p.getDepends()) {
//                System.out.printf("  dep:");
//                for (String q : clause) {
//                    System.out.printf(" %s", q);
//                }
//                System.out.printf("\n");
//            }
        }
        System.out.println("Done");
    }

    static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(line -> sb.append(line));
        return sb.toString();
    }
}
