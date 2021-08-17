package alquiler.controller;

import java.text.ParseException;
import java.util.List;

import org.json.JSONObject;

public interface Controller<T> {
    public List<T> list();

    public List<T> list(List<?> list) throws ParseException;

    public boolean add(JSONObject data) throws Exception;

    public boolean add(String strData) throws Exception; 

    public int indexOf(JSONObject data);

    public int indexOf(String strData);

    public T get(int indice);

    public T set(int indice, JSONObject data) throws Exception;

    public T set(int indice, String strData) throws Exception;

    public T remove(int indice) throws Exception;

    public int size();
    
}
