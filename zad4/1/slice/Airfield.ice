
#ifndef SR_DEMO_ICE
#define SR_DEMO_ICE

//#include <Ice/BuiltinSequences.ice> 

module Airfield
{
	exception RequestCanceledException
	{
	};
	
	class Runway;
	class Airship;
	class Passenger;
	class Travel;
		
	class AirportInfo
	{
		string code;		
		int load;
		Airship occupant;		
		string getCode();		
		int getLoad();
	};
	
	sequence<Runway*> Runways;	
		
	class Airship
	{
		string name;	
		Travel travel;		
		bool land(Runway r);
		bool start();
		bool taxi();	
	};	
		
	sequence<Airship*> Airships;	
		
	class Travel
	{
		string departure;
		string arrival;
	};
	
	class Passenger
	{
		Travel getTravel();
		void redirect(Travel travel1); 
	};	
	
	sequence<Passenger*> Passengers;
	
	struct Wind {
		float direction;
		float strength;
	};

};

#endif
