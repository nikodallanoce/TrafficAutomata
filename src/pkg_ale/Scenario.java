package pkg_ale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Scenario {
    private int lanes;
    private int length;
    private double density;
    private int max_v;
    private double p_decrease_v;
    private double p_change_lane;
    private int[][] road;
    private int direction;
    private double flow = 0;

    public Scenario(int lanes, int length, double density, int max_v, double p_decrease_v, double p_change_lane, int direction) {
        this.lanes = lanes;
        this.length = length;
        this.density = density;
        this.max_v = max_v;
        this.p_decrease_v = p_decrease_v;
        this.p_change_lane = p_change_lane;
        this.direction = direction;
        //build_road();
    }

    private void change_lane(int lane, int cell) {
        for (int i=0;i<lanes;i++) {
            for (int j=0;j<length;j++) {
                if (road[i][j]!=-1) {
                    int distance_ahead = 1;
                    int distance_other_lane = 1;
                    int distance_behind_lane = 1;
                    int other_lane = 1;
                    if (i==1) {
                        other_lane = 0;
                    }

                    //How far ahead the car can move forward
                    while (road[i][j+distance_ahead]%length == -1) {
                        distance_ahead += 1;
                    }

                    //How far ahead in othe lane the can move forward
                    while (road[other_lane][j+distance_other_lane]%length == -1) {
                        distance_other_lane += 1;
                        if (distance_other_lane > length) {
                            break;
                        }
                    }

                    //How free is the other lane
                    while (road[other_lane][j-distance_behind_lane]%length == -1) {
                        distance_behind_lane -= 1;
                        if (distance_behind_lane > length) {
                            break;
                        }
                    }

                    if (distance_ahead < distance_other_lane) {
                        if (road[other_lane][j] == -1 && road[other_lane][j+1] == -1) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void run_step() {
        //Create a new road which will keep the updated scenario state
        int[][] new_road_state = new int[lanes][length];
        for (int i=0;i<lanes;i++) {
            System.arraycopy(road[i], 0, new_road_state[i], 0, length);
        }

        //Update the cars' speeds
        for (int i=0;i<lanes;i++) {
            for (int j=0;j<length;j++) {
                if (road[i][j]!=-1) {
                    int distance = 0;

                    //Check how much the car can move forward
                    while (road[i][(j + distance + 1) % length] == -1) {
                        distance += 1;
                    }

                    //Increase the car speed if the distance to cover is bigger than its actual speed, and it has not reached the maximum
                    if (distance >= road[i][j] && road[i][j] < max_v) {
                        new_road_state[i][j] += 1;
                    }

                    //Set the car speed to the distance if it could not move to his previous velocity
                    if (distance <= road[i][j]) {
                        new_road_state[i][j] = distance;
                    }

                    //Decrease the car speed with a random probability
                    Random rand = new Random();
                    if (new_road_state[i][j] > 0 && rand.nextDouble() <= p_decrease_v) {
                        new_road_state[i][j] -= 1;
                    }
                }
            }
        }

        //Update the scenario state
        for (int i=0;i<lanes;i++) {
            for (int j=0;j<length;j++) {
                if (new_road_state[i][j]!=-1) {
                    int car_steps = new_road_state[i][j];
                    road[i][(j+car_steps)%length] = new_road_state[i][j];
                    if (car_steps!=0) {
                        road[i][j] = -1;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String output = "";
        for (int[] lane: road) {
            for (int i=0;i<length;i++) {
                if (lane[i] == -1) {
                    output = output.concat("_");
                } else {
                    output = output.concat(String.valueOf(lane[i]));
                }
            }
            output = output.concat("\n");
        }
        return output;
    }
}
