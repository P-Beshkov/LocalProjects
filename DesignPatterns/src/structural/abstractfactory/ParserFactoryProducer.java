package structural.abstractfactory;

import structural.abstractfactory.ny.NYParserFactory;
import structural.abstractfactory.tw.TWParserFactory;

public final class ParserFactoryProducer {

	private ParserFactoryProducer(){
		throw new AssertionError();
	}

	public static AbstractParserFactory getFactory(String factoryType){
		
		switch(factoryType)
		{
			case "NYFactory": return new NYParserFactory();
			case "TWFactory": return new TWParserFactory();
		}

		return null;
	}

}
