
#ifndef SR_DEMO_ICE
#define SR_DEMO_ICE

//#include <Ice/BuiltinSequences.ice> 

module Airfield
{
	exception RequestCanceledException
	{
	};
	
	class Airship;
	class Passenger;
	class Travel;
			
	class AirportInfo
	{
		string code;		
		int load;		
		int getLoad(string code);
		void addLoad(string code, int load);
	};	
};

#endif
