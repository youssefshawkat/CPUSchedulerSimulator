
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SRTF {
    private static int time = 0;
    private float averageWaiting = 0;
    private float averageTurnaround =0;
    private int starvation =  100000; //change this value to solve starvation by my solution
    public int contextSwitch = 0;
    private int arrivedIndex = 0;
    private int completed = 0;
    private int processesNum ;
    private ArrayList<Process> SRTFProcesses = new ArrayList<>();
    private ArrayList<Process> arrivedProcesses = new ArrayList<>();

    public SRTF(ArrayList<Process> processes){
        delete();
        processesNum = processes.size();
        for (int i =0; i < processesNum; i++){
            Process temp =new Process(processes.get(i));
            SRTFProcesses.add(temp);
        }
    }

    public int sort() {
        Comparator<Process> processComparator = Comparator.comparing(Process::getArrivalTime).thenComparing(Process::getRemainingTime);
        Collections.sort(SRTFProcesses, processComparator);
        arrivedProcesses = new ArrayList<>();
        for (int i = arrivedIndex; i < SRTFProcesses.size(); i++) {
            if (time >= SRTFProcesses.get(i).getArrivalTime() && SRTFProcesses.get(i).getRemainingTime() > 0) {
                arrivedProcesses.add(SRTFProcesses.get(i));
            }
        }
        if (arrivedProcesses.isEmpty() && completed != SRTFProcesses.size()) {
            time++;
            return sort();
        }
        Comparator<Process> arrivedProcessComparator = Comparator.comparing(Process::getRemainingTime);
        Collections.sort(arrivedProcesses, arrivedProcessComparator);
        int x;
        for (int i = 0; i < SRTFProcesses.size(); i++) {
            if (SRTFProcesses.get(i).getRemainingTime() != 0) {
                if (!SRTFProcesses.get(i).getPeriods().isEmpty()) {
                    x = SRTFProcesses.get(i).getPeriods().get(SRTFProcesses.get(i).getPeriods().size() - 1)[1];
                } else {
                    x = SRTFProcesses.get(i).getArrivalTime();
                }
                if (time - x >= starvation) {
                    return i;
                }
            }
        }
        for (int i = 0; i < SRTFProcesses.size(); i++) {
            if (arrivedProcesses.size() > 0) {
                if (SRTFProcesses.get(i).getName().equals(arrivedProcesses.get(0).getName())) {
                    if (arrivedProcesses.get(0).getRemainingTime() == 0) {
                        arrivedProcesses.remove(0);
                        i = 0;
                    } else {
                        return i;
                    }
                }
            } else {
                return -1;
            }
        }
        return -1;
    }

    public void start() {
        int pastIndex = -1;
        int currentIndex = -1;
        while (completed != SRTFProcesses.size()) {
            currentIndex = sort();
            if (pastIndex != currentIndex) {
                if (pastIndex != -1) {
                    if (SRTFProcesses.get(pastIndex).getRemainingTime() != 0) {
                        time += contextSwitch;
                        int[] arr = {SRTFProcesses.get(pastIndex).getStart(), time - contextSwitch};
                        SRTFProcesses.get(pastIndex).setPeriods(arr);
                    }
                }
                SRTFProcesses.get(currentIndex).setStart(time);
                pastIndex = currentIndex;
            }
            Process temp = SRTFProcesses.get(currentIndex);
            SRTFProcesses.get(currentIndex).setRemainingTime(temp.getRemainingTime() - 1);
            time++;
            if (SRTFProcesses.get(currentIndex).getRemainingTime() == 0) {
                time += contextSwitch;
                int[] arr = {SRTFProcesses.get(currentIndex).getStart(), time - contextSwitch};
                SRTFProcesses.get(currentIndex).setPeriods(arr);
                SRTFProcesses.get(currentIndex).setTurnaroundTime(time - SRTFProcesses.get(currentIndex).getArrivalTime());
                SRTFProcesses.get(currentIndex).setWaitingTime(SRTFProcesses.get(currentIndex).getTurnaroundTime() - SRTFProcesses.get(currentIndex).getBurstTime());
                completed++;
            }
        }

        for (int i = 0; i < SRTFProcesses.size(); i++) {
            averageWaiting += SRTFProcesses.get(i).getWaitingTime();
            averageTurnaround += SRTFProcesses.get(i).getTurnaroundTime();
            //System.out.println(SRTFProcesses.get(i).toString());
        }
        averageWaiting = averageWaiting/SRTFProcesses.size();
        averageTurnaround = averageTurnaround/ SRTFProcesses.size();

        //System.out.println("Average Waiting Time: " + averageWaiting);
        //System.out.println("Average Turnaround Time: " + averageTurnaround);

    }
    public void delete(){
        time = 0;
        averageWaiting = 0;
        averageTurnaround = 0;
        starvation =  100000;
        arrivedIndex = 0;
        completed = 0;
        SRTFProcesses.clear();
        arrivedProcesses.clear();
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

    public ArrayList<Process> getSRTFProcesses() {
        return SRTFProcesses;
    }
    public int getTime() {
        return time;
    }
}
