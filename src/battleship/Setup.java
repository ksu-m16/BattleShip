package battleship;

import java.util.Random;

import model.Field;
import model.Ship;
import model.Ship.Course;


public class Setup {
	private Field myField; 
	private Field enemyField;
	public Field getMyField(){
		myField = new Field(true);
		setShips();
		myField.printField();
		return myField;
		
	}
	
	public Field getEnemyField(){
		enemyField = new Field(false);
//		enemyField.printField();
		return enemyField;
		
	}
	
	public void setShips(){
		
		System.out.println(myField.getFleet().length);
		
		for (Ship s: myField.getFleet()) {
			
			boolean shipPlaced = false;
			
			while (! shipPlaced) {
			
					
				Random r = new Random();
				int x = 0;
				int y = 0;
							
				s.setCourse( r.nextInt(42) < 21 ? Ship.Course.HORIZONTAL : Ship.Course.VERTICAL);
				
				if(s.getCourse() == Course.HORIZONTAL){
					x = r.nextInt(Field.SIZE - s.getSize() + 1);
					y = r.nextInt(Field.SIZE);
					
									
					for(int i = 0; i < s.getSize(); i++){
						
						if((myField.getState(x + i, y) == Field.States.NEAR_SHIP) ||
								(myField.getState(x + i, y) == Field.States.SHIP)) {
							
					
							break;
						}
						
					}
					
					s.setPosition(new int[]{x, y});
						for(int i = 0; i < s.getSize(); i++){
								myField.setState(x + i, y, Field.States.SHIP);
								System.out.println("Ship" + (x + i) + " " + y );
						}
						shipPlaced = true;
						makeShipPerimeter(s, myField);
					
					
		
				}
				if(s.getCourse() == Course.VERTICAL){
					x = r.nextInt(Field.SIZE);
					y = r.nextInt(Field.SIZE - s.getSize() + 1);
					s.setPosition(new int[]{x, y});
					
					for(int i = 0; i < s.getSize(); i++){
						
						if((myField.getState(x, y + i) == Field.States.NEAR_SHIP) ||
								(myField.getState(x, y + i) == Field.States.SHIP)) {

							break;
						}
						
					}
					

						for(int i = 0; i < s.getSize(); i++){
							myField.setState(x, y + i, Field.States.SHIP);
							System.out.println("Ship" + (x + i) + " " + y );
						}
						shipPlaced = true;
						makeShipPerimeter(s, myField);

				}
			
			
			}
			
			System.out.println("ship placed:" + s.getSize());
			myField.printField();
			System.out.println("----------------");
			
//			for(int i = 0; i < Field.SIZE; i++){
//				for(int j = 0; j < Field.SIZE; j++){
//					
//				}
//			}
			
			
		}
		
		
	}
	
	public void makeShipPerimeter(Ship s, Field f) {
		if (s.getCourse() == Ship.Course.HORIZONTAL) {
			if (s.getPosition()[0] != 0 ){
				f.setState(s.getPosition()[0] - 1, s.getPosition()[1], Field.States.NEAR_SHIP);
			} 
			if (s.getPosition()[0] + s.getSize() < Field.SIZE - 1 ){
				f.setState(s.getPosition()[0] + s.getSize(), s.getPosition()[1], Field.States.NEAR_SHIP);
			} 
			
			for(int i = 0; i < s.getSize(); i++){
				if (s.getPosition()[1] != 0 ){
					f.setState(s.getPosition()[0] + i, s.getPosition()[1] - 1, Field.States.NEAR_SHIP);
				}
				
				if (s.getPosition()[1] != (Field.SIZE - 1) ){
					f.setState(s.getPosition()[0] + i, s.getPosition()[1] + 1, Field.States.NEAR_SHIP);
				}
			}
			System.out.println("h--- " + (s.getPosition()[0] + 1));
			
		}
		
		if (s.getCourse() == Ship.Course.VERTICAL) {
			if (s.getPosition()[1] != 0 ){
				f.setState(s.getPosition()[0], s.getPosition()[1] - 1, Field.States.NEAR_SHIP);
			} 
			if (s.getPosition()[1] + s.getSize() < Field.SIZE - 1 ){
				f.setState(s.getPosition()[0], s.getPosition()[1] + s.getSize(), Field.States.NEAR_SHIP);
			} 
			
			for(int i = 0; i < s.getSize(); i++){
				if (s.getPosition()[0] != 0 ){
					f.setState(s.getPosition()[0] - 1, s.getPosition()[1] + i, Field.States.NEAR_SHIP);
				}
				
				if (s.getPosition()[0] != (Field.SIZE - 1) ){
					f.setState(s.getPosition()[0] + 1, s.getPosition()[1] + i, Field.States.NEAR_SHIP);
					System.out.println("v--- " + (s.getPosition()[0] + 1));
				}
			}
		}
		
		
//		for(int i = 0; i < Field.SIZE; i++){
//			for(int j = 0; j < Field.SIZE; j++){
//				if(f.getState(i, j) == Field.States.SHIP)
//			}
//		}
		
	}
	
	
	
	
}
