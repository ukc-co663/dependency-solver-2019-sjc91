/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver;

import depsolver.StateManager.Manager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 *
 * @author Sam
 */
public class Result {
    public LinkedHashSet<String> result = new LinkedHashSet<String>();
    public Manager newState = new Manager();
    public double weight = 0;
    
    public Result(Manager curState){
        this.newState = curState.copy();
    }
    
    
    
}
