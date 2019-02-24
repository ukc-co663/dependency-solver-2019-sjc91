/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depsolver.RepoManager;

enum Conditions{
    LessThan,
    LessEqualThan,
    GreaterThan,
    GreaterEqualThan,
    EqualThan,
    All
}

/**
 *
 * @author Sam
 */
public class Contract {
    public String name;
    public Conditions cond;
    public String revision;
    
    public Contract(String p){
        String[] split_items;
        if(p.contains("<=")){
            split_items = p.split("<=");
            this.name = split_items[0];
            this.cond = Conditions.LessEqualThan;
            this.revision = split_items[1];
        }else if(p.contains(">=")){
            split_items = p.split(">=");
            this.name = split_items[0];
            this.cond = Conditions.GreaterEqualThan;
            this.revision = split_items[1];
        }else if(p.contains("=")){
            split_items = p.split("=");
            this.name = split_items[0];
            this.cond = Conditions.EqualThan;
            this.revision = split_items[1];
        }else if(p.contains("<")){
            split_items = p.split("<");  
            this.name = split_items[0];
            this.cond = Conditions.LessThan;
            this.revision = split_items[1];
        }else if(p.contains(">")){
            split_items = p.split(">");
            this.name = split_items[0];
            this.cond = Conditions.GreaterThan;
            this.revision = split_items[1];
        }else{
            this.name = p;
            this.cond = Conditions.All;
        }            
    }
}
