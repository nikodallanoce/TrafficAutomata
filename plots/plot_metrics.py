import numpy as np
import matplotlib.pyplot as plt


def read_metrics(file: str):
    flows = list()
    densities = list()
    average_speeds = list()
    changes_of_lane = list()
    with open(file) as datafile:
        datafile.readline()  # Skip first line
        for metric in datafile:
            metric = metric.split(";")
            flows.append(float(metric[-1][:-1]))
            densities.append(float(metric[2]))
            average_speeds.append(float(metric[1]))
            changes_of_lane.append(float(metric[0]))

    flows = np.round(np.array(flows), 2)
    densities = np.round(np.array(densities), 2)
    average_speeds = np.round(np.array(average_speeds), 2)
    changes_of_lane = np.array(changes_of_lane)
    return flows, densities, average_speeds, changes_of_lane


def plot_metric_comparison(metric_values: list, metric: str, interval_sampling: int = 10, is_incoming: bool = True):
    steps_plot = np.arange(interval_sampling, len(flows_second_roads[0]) * interval_sampling + 1, interval_sampling)
    lanes = 2
    for metric_value in metric_values:
        label = "{0} lanes".format(lanes)
        if not is_incoming:
            label = "Previous road with " + label

        plt.plot(steps_plot, metric_value, label=label, marker="o")
        lanes += 1

    plt.xlabel("Step")
    plt.ylabel(metric)
    plt.legend()
    plt.show()


if __name__ == "__main__":
    first_roads = ["../metrics_datasets/S0_L2_C50_27431.csv", "../metrics_datasets/S0_L3_C50_43488.csv",
                   "../metrics_datasets/S0_L4_C50_56603.csv", "../metrics_datasets/S0_L5_C50_67463.csv"]
    second_roads = ["../metrics_datasets/S1_L2_C50_27431.csv", "../metrics_datasets/S1_L2_C50_43488.csv",
                    "../metrics_datasets/S1_L2_C50_56603.csv", "../metrics_datasets/S1_L2_C50_67463.csv"]

    # Retrieve computed metrics at various configurations
    flows_first_roads = [read_metrics(first_road)[0] for first_road in first_roads]
    flows_second_roads = [read_metrics(second_road)[0] for second_road in second_roads]
    densities_first_roads = [read_metrics(first_road)[1] for first_road in first_roads]
    densities_second_roads = [read_metrics(second_road)[1] for second_road in second_roads]
    speeds_first_roads = [read_metrics(first_road)[2] for first_road in first_roads]
    speeds_second_roads = [read_metrics(second_road)[2] for second_road in second_roads]

    # Plot flows
    plot_metric_comparison(flows_first_roads, "Flow", 10)
    plot_metric_comparison(flows_second_roads, "Flow", 10, False)

    # Plot densities
    plot_metric_comparison(densities_first_roads, "Density", 10)
    plot_metric_comparison(densities_second_roads, "Density", 10, False)

    # Plot flows
    plot_metric_comparison(speeds_first_roads, "Average speed", 10)
    plot_metric_comparison(speeds_second_roads, "Average speed", 10, False)
