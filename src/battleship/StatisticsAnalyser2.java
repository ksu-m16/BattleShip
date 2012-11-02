package battleship;

import java.util.ArrayList;
import java.util.Arrays;

import model.IPoint;
import model.IShipDescription;
import model.Point;
import model.ShipDescription;
import model.State;
import model.ShipDescription.Course;

public class StatisticsAnalyser2 {

	public IPoint attack(int size, State[][] field) {

		return getMostProbablePoint(getSetupStatistics(
				getPossiblePlacementList(size, field), field));

	}

	private ArrayList<ArrayList<IPoint>> getPossiblePlacementList(int size,
			State[][] field) {
		ArrayList<IShipDescription> descriptionList = new ArrayList<IShipDescription>();

		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {

				IShipDescription s = new ShipDescription(new Point(i, j), size,
						Course.HORIZONTAL);
				if (SetupHelper.checkShipPlacement(s, field)) {
					descriptionList.add(s);
				}

				s = new ShipDescription(new Point(i, j), size, Course.VERTICAL);
				if (SetupHelper.checkShipPlacement(s, field)) {
					descriptionList.add(s);
				}
			}
		}

		ArrayList<ArrayList<IPoint>> placementList = new ArrayList<ArrayList<IPoint>>();

		for (IShipDescription s : descriptionList) {
			placementList.add((ArrayList<IPoint>) s.getPosition());
		}
		return placementList;
	}

	private int[][] getSetupStatistics(
			ArrayList<ArrayList<IPoint>> placementList, State[][] field) {
		int[][] statistics = new int[field.length][field[0].length];

		for (ArrayList<IPoint> placement : placementList) {
			for (IPoint p : placement) {
				statistics[p.getX()][p.getY()]++;
			}
		}
		
	
		return statistics;
	}

	private IPoint getMostProbablePoint(int[][] statistics) {
		IPoint pmax = new Point(0, 0);
		for (int i = 0; i < statistics.length; i++) {
			for (int j = 0; j < statistics[0].length; j++) {
				if (statistics[i][j] > statistics[pmax.getX()][pmax.getY()]) {
					pmax = new Point(i, j);
				}
			}
		}
		return pmax;
	}

}
