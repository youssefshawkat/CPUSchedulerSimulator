import java.awt.*;
import java.util.ArrayList;

public class Process {
    private String name;
    private int burstTime;
    private int waitingTime;
    private int turnaroundTime;
    private int arrivalTime;
    protected int remainingTime;
    private int start = 0;
    private int priority;
    protected int QUN;
    private ArrayList<int[]> periods = new ArrayList<>();
    protected ArrayList<Integer>QuntamList=new ArrayList<Integer>();
    protected ArrayList<Integer>AGATfactor=new ArrayList<Integer>();
    public Process(){
        name = "";
        burstTime = 0;
        waitingTime = 0;
        turnaroundTime = 0;
        arrivalTime = 0;
        remainingTime = 0;
    }

    public Process(String name,int arrivalTime , int burstTime ){
        this.name = name;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        remainingTime = burstTime;
    }
    public Process(String name, int arrivalTime, int burstTime ,int priority){
        this.name = name;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        remainingTime = burstTime;
        this.priority = priority;
    }
    public Process(String name, int arrivalTime, int burstTime, int priority,int QunNumber) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        remainingTime=burstTime;
        this.priority = priority;
        setQUN(QunNumber);
        this.waitingTime=0;
    }
    public Process(Process that){
        this(that.getName(), that.getArrivalTime(), that.getBurstTime(), that.getPriority(),that.getQUN());
    }


    public int getQUN() {
        return QuntamList.get(QuntamList.size()-1);
    }

    public void setQUN(int QUN) {
        this.QUN = QUN;
        QuntamList.add(QUN);
    }
    public ArrayList<Integer> getAGATfactor() {
        return AGATfactor;
    }
    public ArrayList<Integer> getQuntam() {
        return QuntamList;
    }
    public void setQuntam(ArrayList<Integer> quntam) {
        QuntamList = quntam;
    }
    public void setAGATfactor(ArrayList<Integer> AGATfactor) {
        this.AGATfactor = AGATfactor;
    }
    public void setFactor (int n){
        this.AGATfactor.add(n);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ArrayList<int[]> getPeriods() {
        return periods;
    }

    public void setPeriods(int[] periods) {
        this.periods.add(periods);
    }

    /*@Override
    public String toString(){
        String details = ("Name: " + this.name +
                "\nBurst Time: " + this.burstTime +
                "\nArrival Time: " + this.arrivalTime +
                "\nWaiting Time: " + this.waitingTime +
                "\nTurnaround Time: " + this.turnaroundTime +
                "\nPriority: " + this.priority +
                "\nQuantam List: " + this.QuntamList.toString() +
                "\nAGAT Factor: " + this.AGATfactor.toString() +
                "\nPeriods:\n");
        String workTime = "";
        for (int i=0; i < periods.size(); i++){
            workTime += "\t" +periods.get(i)[0] + " " + periods.get(i)[1] + "\n";
        }
        return details + workTime;
    }*/
}
