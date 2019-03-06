/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver;

import depsolver.StateManager.Manager;
import java.util.ArrayList;

/**
 *
 * @author Sam
 */
public class Result {
    public ArrayList<String> result = new ArrayList<String>();
    public Manager newState;
    public double weight = 0;
    
    public Result(Manager curState){
        this.newState = curState;
    }
    
}
