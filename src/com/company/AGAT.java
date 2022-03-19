import java.util.*;

public class AGAT {
    private float averageWaiting = 0;
    private float averageTurnaround = 0;
    private int numberProcesses;
    private ArrayList<Process> AGATProcesses = new ArrayList<>();
    //public int contextSwitch = 0;
    private Queue<Process> readyQueue = new LinkedList<Process>();
    private ArrayList<Process> DeadList = new ArrayList<Process>();
    private double v1 = 0;
    private double v2 = 0;
    private static int currTime = 0;
    private int totalProcessNum;

    public AGAT(ArrayList<Process> processes) {
        delete();
        numberProcesses = processes.size();
        totalProcessNum = processes.size();
        for (int i = 0; i < totalProcessNum; i++) {
            Process temp = new Process(processes.get(i));
            AGATProcesses.add(temp);
        }
    }

    // Sort based Arrival Time.
    public void pushIntoReadyQueue(int curr) {
        for (int i = 0; i < numberProcesses; i++) {
            if (AGATProcesses.get(i).getArrivalTime() <= curr) {
                if (!readyQueue.contains(AGATProcesses.get(i))) {
                    readyQueue.add(AGATProcesses.get(i));
                }
            }
        }
    }

    public boolean start() {
        if (numberProcesses == 0){
            return false;
        }
        AGATProcesses = sortArrivalTime(AGATProcesses);
        if (AGATProcesses.get(numberProcesses - 1).getArrivalTime() > 10) {
            v1 = (double) (AGATProcesses.get(numberProcesses - 1).getArrivalTime() / 10.0);
        } else {
            v1 = 1;
        }
        pushIntoReadyQueue(0);
        Process cpuPR = readyQueue.element();
        Process temp = check(readyQueue);
        cpuPR.setStart(currTime);
        for (int i = 0; i <= cpuPR.getQUN(); i++) {
            if (Math.round((double) (40 * cpuPR.getQUN()) / 100) < i) {
                temp = check(readyQueue);
                if (temp == cpuPR || temp == null) {
                    deleteAgat(AGATProcesses);
                    cpuPR.setRemainingTime(--cpuPR.remainingTime);
                    cpuPR.QUN--;
                    currTime++;
                } else {
                    cpuPR.setQUN((cpuPR.getQUN() + cpuPR.QUN));
                    readyQueue.remove(cpuPR);
                    readyQueue.add(cpuPR);
                    int arr[] = {cpuPR.getStart(), currTime};
                    cpuPR.setPeriods(arr);
                    //currTime += contextSwitch;
                    cpuPR = temp;
                    cpuPR.setStart(currTime);
                    i = 0;
                }
            } else {
                cpuPR.setRemainingTime(--cpuPR.remainingTime);
                cpuPR.QUN--;
                currTime++;
            }
            if (cpuPR.QUN == 0 && cpuPR.getRemainingTime() > 0) {
                if (DeadList.size() + 1 != totalProcessNum) {
                    agatFactor(readyQueue);
                    int arr[] = {cpuPR.getStart(), currTime};
                    cpuPR.setPeriods(arr);
                    //currTime += contextSwitch;
                }
                cpuPR.setQUN(cpuPR.getQUN() + 2);
                readyQueue.remove(cpuPR);
                readyQueue.add(cpuPR);
                cpuPR = readyQueue.element();
                if (DeadList.size() + 1 != totalProcessNum)
                    cpuPR.setStart(currTime);
                i = 0;
            }
            if (cpuPR.getRemainingTime() == 0) {
                cpuPR.setQUN(0);
                cpuPR.setTurnaroundTime(currTime - cpuPR.getArrivalTime());
                readyQueue.remove(cpuPR);
                AGATProcesses.remove(cpuPR);
                numberProcesses--;
                int arr[] = {cpuPR.getStart(), currTime};
                cpuPR.setPeriods(arr);
                DeadList.add(cpuPR);
                //currTime += contextSwitch;
                if (!readyQueue.isEmpty()) {
                    cpuPR = readyQueue.element();
                    cpuPR.setStart(currTime);
                    if (DeadList.size() + 1 != totalProcessNum)
                        check(readyQueue);
                } else {
                    break;
                }
                i = 0;
            }
            if (!AGATProcesses.isEmpty()) {
                pushIntoReadyQueue(currTime);
            }
        }
        for (int i = 0; i < DeadList.size(); i++) {
            DeadList.get(i).setWaitingTime(DeadList.get(i).getTurnaroundTime() - DeadList.get(i).getBurstTime());
            averageTurnaround += DeadList.get(i).getTurnaroundTime();
            averageWaiting += DeadList.get(i).getWaitingTime();
            //System.out.println(DeadList.get(i));
        }
        averageWaiting = averageWaiting/DeadList.size();
        averageTurnaround = averageTurnaround/ DeadList.size();

        //System.out.println("Average Waiting Time: " + averageWaiting);
        //System.out.println("Average Turnaround Time: " + averageTurnaround);
        return false;
    }

    public Process check(Queue<Process> rq) {
        if (rq.isEmpty()) {
            return null;
        } else {
            return agatFactor(rq);
        }
    }

    public Process agatFactor(Queue<Process> ready) {
        int minFac = 1000000000;
        Process Minproc = ready.element();
        ArrayList<Process> temp = new ArrayList<Process>();
        for (Process proc : AGATProcesses) {
            temp.add(proc);
        }
        double MaxBurst = 0;
        if (!AGATProcesses.isEmpty()) {
            MaxBurst = sortRemainingTime(AGATProcesses).get(AGATProcesses.size() - 1);
        } else {
            return null;
        }
        if (MaxBurst > 10) {
            v2 = MaxBurst / 10;
        } else {
            v2 = 1;
        }
        for (int i = 0; i < AGATProcesses.size(); i++) {
            int Agatfac = (int) ((10 - temp.get(i).getPriority()) + (Math.ceil((temp.get(i).getArrivalTime() / v1))) + (Math.ceil(temp.get(i).getRemainingTime() / v2)));
            temp.get(i).setFactor(Agatfac);
            if (minFac > Agatfac) {
                if (ready.contains(temp.get(i))) {
                    minFac = Agatfac;
                    Minproc = temp.get(i);
                }
            }
        }
        return Minproc;
    }

    public static ArrayList<Process> sortArrivalTime(ArrayList<Process> procs) {

        ArrayList<Integer> ArrivalTime = new ArrayList<Integer>();
        ArrayList<Process> temp = new ArrayList<Process>();

        // getting all the arrival times
        for (Process proc : procs) {
            ArrivalTime.add(proc.getArrivalTime());
        }

        // sorting Arrival Time
        Collections.sort(ArrivalTime);

        // storing the sorted AGATProcesses in temp
        for (Integer ArrivalTime_Value : ArrivalTime) {
            for (int i = 0; i < procs.size(); i++) {

                if (procs.get(i).getArrivalTime() == ArrivalTime_Value) {
                    temp.add(procs.get(i));
                    procs.remove(i);

                    i--;
                }
            }
        }

        procs = temp;
        return procs;
    }

    public static ArrayList<Integer> sortRemainingTime(ArrayList<Process> procs) {

        ArrayList<Integer> Burst = new ArrayList<Integer>();

        for (Process proc : procs) {
            Burst.add(proc.getRemainingTime());
        }
        Collections.sort(Burst);

        return Burst;
    }

    public void deleteAgat(ArrayList<Process> p) {
        for (int i = 0; i < p.size(); i++) {
            p.get(i).AGATfactor.remove(p.get(i).AGATfactor.size() - 1);
        }
    }

    public void delete() {
        averageWaiting = 0;
        averageTurnaround = 0;
        numberProcesses = 0;
        AGATProcesses.clear();
        readyQueue.clear();
        DeadList.clear();
        v1 = 0;
        v2 = 0;
        currTime = 0;
        totalProcessNum = 0;
    }
    /*public int getContextSwitch() {
        return contextSwitch;
    }

    public void setContextSwitch(int contextSwitch) {
        this.contextSwitch = contextSwitch;
    }*/

    public float getAverageWaiting() {
        return averageWaiting;
    }

    public float getAverageTurnaround() {
        return averageTurnaround;
    }

    public ArrayList<Process> getAGATrocesses() {
        return DeadList;
    }

    public int getTime() {
        return currTime;
    }
}
