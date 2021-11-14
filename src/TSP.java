import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class is responsible for calculating the shortest hamiltonian path
 * for visiting all cities. It is also an observable that notifies observers
 * when it's done calculating. It runs on it's own thread.
 *
 * @author amaryadav
 * @author kaichen
 * @version 1.0
 * @since 2021-10-08
 */
public class TSP extends Observable implements Runnable,BaseAlgorthim {

    public boolean keepRunning;

    /**
     * Creates an instance of TSP class initialized
     * to keep running when requested.
     *
     */
    public TSP() {
        this.keepRunning = true;
    }

    private void calculate(List<City> cityList) { //reorder the cities

        List<City> path = new ArrayList<>();

        if (cityList.isEmpty()) {
            Blackboard.getInstance().path = path;
            setChanged();
            notifyObservers();
            return;
        }

        int visitedCount = 0;
        boolean[] visited = new boolean[cityList.size()];
        int[][] distance = new int[cityList.size()][cityList.size()];

        for (int i = 0; i < cityList.size(); i++) {
            City city1 = cityList.get(i);
            for (int j = 0; j < cityList.size(); j++) {
                City city2 = cityList.get(j);
                if (i != j) {
                    distance[i][j] = (int) Math.sqrt(Math.pow(city1.bounds.x - city2.bounds.x, 2) + Math.pow(city1.bounds.y - city2.bounds.y, 2));
                    distance[j][i] = distance[i][j];
                }
            }
        }


        path.add(cityList.get(0));
        visitedCount++;
        visited[0] = true;
        int prevCityIndex = 0;

        while(visitedCount != cityList.size()){
            //find next unvisited closest nbr
            int closestNbrIndex = -1;
            int minDistance = Integer.MAX_VALUE;
            for(int i = 0; i < cityList.size(); i++){
                if(!visited[i] && distance[prevCityIndex][i] < minDistance){
                    minDistance = distance[prevCityIndex][i];
                    closestNbrIndex = i;
                }
            }

            path.add(cityList.get(closestNbrIndex));
            visitedCount++;
            visited[closestNbrIndex] = true;
        }

        Blackboard.getInstance().path = path;
        setChanged();
        notifyObservers();

    }

    /**
     * This method helps TSP scan for data changes in the list of cities
     * and calculate the new path if needed.
     *
     */
    @Override
    public void run() {

        while (this.keepRunning) {
            try {
                Thread.sleep(1); //without this code won't work, need to ask prof. why
                if (Blackboard.getInstance().dataChanged) {
                    System.out.println("dataChanged=" + Blackboard.getInstance().dataChanged);
                    this.calculate(Blackboard.getInstance().cityList);
                    Blackboard.getInstance().dataChanged = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
