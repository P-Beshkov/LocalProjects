package structural.bridge;

public class TestBridgePattern {

	public static void test() {
		Product centralLocking = new CentralLocking("Central Locking System");
		Product gearLocking = new GearLocking("Gear Locking System");
		Car bigWheelCar = new BigWheel(centralLocking , "BigWheel xz model");
		bigWheelCar.produceProduct();
		bigWheelCar.assemble();
		bigWheelCar.printDetails();
		
		System.out.println();
		
		bigWheelCar = new BigWheel(gearLocking , "BigWheel xz model");
		bigWheelCar.produceProduct();
		bigWheelCar.assemble();
		bigWheelCar.printDetails();
		
		System.out.println("-----------------------------------------------------");
		
		bigWheelCar = new Motoren(centralLocking, "Motoren lm model");
		bigWheelCar.produceProduct();
		bigWheelCar.assemble();
		bigWheelCar.printDetails();
		
		System.out.println();
		
		bigWheelCar = new Motoren(gearLocking, "Motoren lm model");
		bigWheelCar.produceProduct();
		bigWheelCar.assemble();
		bigWheelCar.printDetails();		
	}

}
