/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatapplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data implements Serializable {
    public String message;
    public boolean sendAll;
    public List<String> clientlist;

   public ArrayList<Integer> clientNum;

   public Data()
   {
       message="";
       sendAll = false;
       clientNum  = new ArrayList<>();
       clientlist = new ArrayList<>();
   }
}
