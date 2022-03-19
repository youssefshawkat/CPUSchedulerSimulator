import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SJF {
    private static int  time=0;
    private float averageWaiting = 0;
    private float averageTurnaround = 0;
    private int starvation =  100000; //change this value to solve starvation by my solution
    private ArrayList<Process>  SJFProcesses = new ArrayList<>();
    private int processesNum ;
    public SJF(ArrayList<Process> processes){
        delete();
        processesNum = processes.size();
        for (int i =0; i < processesNum; i++){
            Process temp =new Process(processes.get(i));
            SJFProcesses.add(temp);
        }
    }
    public void sort(int n){
        if (n==0){
            Comparator<Process> processComparator = Comparator.comparing(Process::getArrivalTime).thenComparing(Process::getBurstTime);
            Collections.sort(SJFProcesses.subList(n, SJFProcesses.size()), processComparator);
        }else{
            int arrived = n;
            for (int i =n; i< SJFProcesses.size();i++){
                if (time >= SJFProcesses.get(i).getArrivalTime())
                    arrived++;
            }
            if (arrived > n) {
                Comparator<Process> processComparator = Comparator.comparing(Process::getBurstTime).thenComparing(Process::getArrivalTime);
                Collections.sort(SJFProcesses.subList(n, arrived), processComparator);
            }
            for (int i =n; i < SJFProcesses.size(); i++){
                if (time - SJFProcesses.get(i).getArrivalTime() >= starvation){
                    SJFProcesses.add(n,SJFProcesses.get(i));
                    SJFProcesses.remove(i+1);
                }
            }
        }
    }
    public void start(){
        sort(0);
        for (int i =0; i < SJFProcesses.size(); i++){
            if (time >= SJFProcesses.get(i).getArrivalTime()) {
                time += SJFProcesses.get(i).getBurstTime();
                SJFProcesses.get(i).setRemainingTime(0);
                int[] arr = {time - SJFProcesses.get(i).getBurstTime(), time};
                SJFProcesses.get(i).setPeriods(arr);
                SJFProcesses.get(i).setTurnaroundTime(time - SJFProcesses.get(i).getArrivalTime());
                SJFProcesses.get(i).setWaitingTime(SJFProcesses.get(i).getTurnaroundTime() - SJFProcesses.get(i).getBurstTime());
            }else{
                time += SJFProcesses.get(i).getArrivalTime()-time;
                i--;
                continue;
            }
            sort(i+1);
        }
        for (int i=0; i < SJFProcesses.size(); i++){
            averageWaiting += SJFProcesses.get(i).getWaitingTime();
            averageTurnaround += SJFProcesses.get(i).getTurnaroundTime();
            //System.out.println(SJFProcesses.get(i).toString());
        }
        averageWaiting = averageWaiting/SJFProcesses.size();
        averageTurnaround = averageTurnaround/ SJFProcesses.size();

        //System.out.println("Average Waiting Time: " + averageWaiting);
        //System.out.println("Average Turnaround Time: " + averageTurnaround);
    }
    public void delete(){
        time = 0;
        averageWaiting = 0;
        averageTurnaround = 0;
        starvation =  100000;
        SJFProcesses.clear();

    }
    public float getAverageWaiting() {
        return averageWaiting;
    }

    public float getAverageTurnaround() {
        return averageTurnaround;
    }

    public ArrayList<Process> getSJFProcesses() {
        return SJFProcesses;
    }
    public int getTime() {
        return time;
    }
}
