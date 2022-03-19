
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PS {
    private static int time = 0;
    private float averageWaiting = 0;
    private float averageTurnaround = 0;
    private int starvation = 100000; //change this value to solve starvation by my solution
    public int contextSwitch = 0;
    private ArrayList<Process> PSProcesses = new ArrayList<>();
    private int processesNum ;
    public PS(ArrayList<Process> processes){
        delete();
        processesNum = processes.size();
        for (int i =0; i < processesNum; i++){
            Process temp =new Process(processes.get(i));
            PSProcesses.add(temp);
        }
    }

    public void sort(int n) {
        for (int i = n; i < PSProcesses.size(); i++) {
            if (time - PSProcesses.get(i).getArrivalTime() >= starvation) {
                PSProcesses.get(i).setPriority(PSProcesses.get(i).getPriority()-1);
            }
        }
        if (n == 0) {
            Comparator<Process> processComparator = Comparator.comparing(Process::getArrivalTime).thenComparing(Process::getPriority);
            Collections.sort(PSProcesses.subList(n, PSProcesses.size()), processComparator);
        } else {
            int arrived = n;
            for (int i = n; i < PSProcesses.size(); i++) {
                if (time >= PSProcesses.get(i).getArrivalTime())
                    arrived++;
            }
            if (arrived > n) {
                Comparator<Process> processComparator = Comparator.comparing(Process::getPriority).thenComparing(Process::getArrivalTime);
                Collections.sort(PSProcesses.subList(n, arrived), processComparator);
            }

        }
    }

    public void start() {
        sort(0);
        for (int i = 0; i < PSProcesses.size(); i++) {
            if (time >= PSProcesses.get(i).getArrivalTime()) {
                time += PSProcesses.get(i).getBurstTime() + contextSwitch;
                PSProcesses.get(i).setRemainingTime(0);
                int[] arr = {time - PSProcesses.get(i).getBurstTime() - contextSwitch, time -contextSwitch};
                PSProcesses.get(i).setPeriods(arr);
                PSProcesses.get(i).setTurnaroundTime(time - PSProcesses.get(i).getArrivalTime());
                PSProcesses.get(i).setWaitingTime(PSProcesses.get(i).getTurnaroundTime() - PSProcesses.get(i).getBurstTime());
            } else {
                time += PSProcesses.get(i).getArrivalTime() - time;
                i--;
                continue;
            }
            sort(i + 1);
        }
        for (int i = 0; i < PSProcesses.size(); i++) {
            averageTurnaround += PSProcesses.get(i).getTurnaroundTime();
            averageWaiting += PSProcesses.get(i).getWaitingTime();
            //System.out.println(PSProcesses.get(i).toString());
        }
        averageWaiting = averageWaiting/PSProcesses.size();
        averageTurnaround = averageTurnaround/ PSProcesses.size();

        //System.out.println("Average Waiting Time: " + averageWaiting);
        //System.out.println("Average Turnaround Time: " + averageTurnaround);
    }
    public void delete(){
        time = 0;
        averageWaiting = 0;
        averageTurnaround = 0;
        starvation =  100000;
        PSProcesses.clear();

    }
    public int getContextSwitch() {
        return contextSwitch;
    }

    public void setContextSwitch(int contextSwitch) {
        this.contextSwitch = contextSwitch;
    }

    public float getAverageWaiting() {
        return averageWaiting;
    }

    public float getAverageTurnaround() {
        return averageTurnaround;
    }
    public ArrayList<Process> getPSFProcesses() {
        return PSProcesses;
    }
    public int getTime() {
        return time;
    }
}

