package net.masterpiece.m_fundi.model;

public class ExpertsModel {

    String code = null;
    String name = null;
    String arrivalTime = null;
    String age = null;
    String ID = null;
    String tag = null;

    boolean selected = false;

    public  ExpertsModel (String arrivalTime, String name, String age, String ID, String tag){
        super();
        this.arrivalTime = arrivalTime;
        this.name = name;
        this.age = age;
        this.ID = ID;
        this.tag = tag;
    }
/*    public ExpertsModel(String code, String name, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
    }*/

    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }



/*    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }*/

}